package ai.fd.mimi.client.engine.ktor

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion

class MimiKtorWebSocketSession<T>(
    private val ktorWebSocketSession: DefaultClientWebSocketSession,
    converter: MimiModelConverter.JsonString<T>
) : MimiWebSocketSessionInternal<T>(converter) {

    // note: `MutableStateFlow` is used for convenient thread safe value holder. Replace with any atomic value holder
    // once a stable atomic operation library is available.
    private val isActiveMutableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val isActive: Boolean
        get() = isActiveMutableStateFlow.value

    override val rxFlow: Flow<T> = ktorWebSocketSession.incoming
        .consumeAsFlow()
        .map { converter.decode(it.data.decodeToString()) }
        .onCompletion {
            isActiveMutableStateFlow.value = false
        }

    override suspend fun sendBinary(binaryData: ByteArray) =
        ktorWebSocketSession.send(Frame.Binary(true, binaryData.copyOf()))

    override suspend fun sendText(text: String) {
        ktorWebSocketSession.send(Frame.Text(true, text.encodeToByteArray()))
    }

    override fun cancel() = ktorWebSocketSession.cancel()
}
