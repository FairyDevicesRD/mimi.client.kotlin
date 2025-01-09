package ai.fd.mimi.client.service.asr.core

import ai.fd.mimi.client.engine.MimiWebSocketSession
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class MimiAsrWebSocketSession<T>(
    private val webSocketSession: MimiWebSocketSessionInternal<T>
) : MimiWebSocketSession<T> by webSocketSession {

    // note: `MutableStateFlow` is used for convenient thread safe value holder. Replace with any atomic value holder
    // once a stable atomic operation library is available.
    private val hasRecogBreakSentMutableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val isActive: Boolean
        get() = webSocketSession.isActive && !hasRecogBreakSentMutableStateFlow.value

    suspend fun stopRecognition() {
        webSocketSession.sendJsonText(ControlCommand.RECOG_BREAK, ControlCommand.serializer())
        hasRecogBreakSentMutableStateFlow.value = true
    }

    @Serializable
    private data class ControlCommand(
        @SerialName("command")
        val command: String
    ) {
        companion object {
            val RECOG_BREAK = ControlCommand("recog-break")
        }
    }
}
