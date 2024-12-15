package ai.fd.mimi.client.engine.ktor

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import okio.ByteString

class MimiKtorWebSocketSession<T>(
    private val ktorWebSocketSession: DefaultClientWebSocketSession,
    converter: MimiModelConverter<T>
) : MimiWebSocketSessionInternal<T>(converter) {

    override val rxFlow: Flow<T> = ktorWebSocketSession.incoming
        .consumeAsFlow()
        .map { converter.decode(it.data.decodeToString()) }

    override suspend fun sendBinary(binaryData: ByteString) =
        ktorWebSocketSession.send(Frame.Binary(true, binaryData.toByteArray()))

    override suspend fun sendText(text: String) {
        ktorWebSocketSession.send(Frame.Text(true, text.encodeToByteArray()))
    }

    override fun cancel() = ktorWebSocketSession.cancel()
}
