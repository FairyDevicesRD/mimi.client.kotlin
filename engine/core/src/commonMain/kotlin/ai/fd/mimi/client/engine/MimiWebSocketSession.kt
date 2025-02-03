package ai.fd.mimi.client.engine

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import okio.ByteString

/**
 * An interface of a WebSocket session for mimi service.
 *
 * Data sent from the server side is notified through [rxFlow]. It is disconnected when this [rxFlow] subscription is
 * canceled or when [cancel] is called. In other words, it disconnects without graceful shutdown.
 * Each mimi service has its own graceful shutdown feature, please refer to the documentation of each mimi service's
 * session implementation class.
 */
interface MimiWebSocketSession<T> {

    /**
     * Whether the WebSocket connection has been successfully created and can communicate to the server.
     * This becomes `false` when the connection is disconnected or graceful shutdown is initiated as defined by each
     * service.
     */
    val isActive: Boolean

    val rxFlow: Flow<T>

    /**
     * Sends voice binary data to the server side.
     * If there is room in the buffer, this function is executed immediately.
     * If there is no buffer space, the function suspends until one is available.
     */
    suspend fun sendBinary(binaryData: ByteString)
    fun cancel()

    fun sendBinaryBlocking(binaryData: ByteString) = runBlocking {
        sendBinary(binaryData)
    }
}

abstract class MimiWebSocketSessionInternal<T>(
    protected val converter: MimiModelConverter.JsonString<T>
) : MimiWebSocketSession<T> {

    protected abstract suspend fun sendText(text: String)
    suspend fun <R> sendJsonText(data: R, serializer: KSerializer<R>) =
        sendText(converter.encode(data, serializer))
}
