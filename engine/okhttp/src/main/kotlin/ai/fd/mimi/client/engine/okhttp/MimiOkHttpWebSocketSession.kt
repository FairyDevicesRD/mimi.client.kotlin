package ai.fd.mimi.client.engine.okhttp

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString.Companion.toByteString

internal class MimiOkHttpWebSocketSession<R>(
    private val request: Request,
    private val okHttpClient: OkHttpClient,
    converter: MimiModelConverter.JsonString<R>
) : MimiWebSocketSessionInternal<R>(converter) {

    @OptIn(DelicateCoroutinesApi::class)
    override val isActive: Boolean
        get() = !rxChannel.isClosedForSend

    private val rxChannel: Channel<R> = Channel()
    override val rxFlow: Flow<R> = rxChannel.consumeAsFlow()

    private val onConnectedDeferred: CompletableDeferred<MimiIOException?> = CompletableDeferred()
    private val webSocketDeferred: CompletableDeferred<WebSocket> = CompletableDeferred()

    @Throws(MimiIOException::class, CancellationException::class)
    suspend fun connect() {
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                onConnectedDeferred.complete(null)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val response = try {
                    converter.decode(text)
                } catch (e: MimiJsonException) {
                    rxChannel.close(e)
                    return
                }
                rxChannel.trySendBlocking(response)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(code, reason)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                rxChannel.close()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // If an error occurs before the WebSocket session handshake is complete.
                if (!onConnectedDeferred.isCompleted) {
                    onConnectedDeferred.complete(MimiIOException("Failed to open WebSocket session", t))
                    rxChannel.close()
                    return
                }
                rxChannel.close(MimiIOException("WebSocket session is closed unexceptionally", t))
            }
        }
        val webSocket = okHttpClient.newWebSocket(request, webSocketListener)
        rxChannel.invokeOnClose {
            webSocket.cancel()
        }
        webSocketDeferred.complete(webSocket)
        val exceptionDuringOpen = onConnectedDeferred.await()
        if (exceptionDuringOpen != null) {
            throw exceptionDuringOpen
        }
    }

    override suspend fun sendBinary(binaryData: ByteArray) {
        webSocketDeferred.await().send(binaryData.toByteString())
    }

    override suspend fun sendText(text: String) {
        webSocketDeferred.await().send(text)
    }

    override fun cancel() {
        rxChannel.close()
    }
}
