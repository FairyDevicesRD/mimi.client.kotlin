package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.engine.MimiWebSocketSession
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class MimiAsrWebSocketSession(
    private val webSocketSession: MimiWebSocketSessionInternal<MimiAsrResult>
) : MimiWebSocketSession<MimiAsrResult> by webSocketSession {

    suspend fun stopRecognition() =
        webSocketSession.sendJsonText(ControlCommand.RECOG_BREAK, ControlCommand.serializer())

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
