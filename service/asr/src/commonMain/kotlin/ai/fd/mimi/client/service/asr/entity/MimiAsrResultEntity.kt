package ai.fd.mimi.client.service.asr.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MimiAsrResultEntity(
    @SerialName("type")
    val type: String,
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("status")
    val status: Status,
    @SerialName("response")
    val response: List<Response>
) {

    @Serializable
    data class Response(
        @SerialName("pronunciation")
        val pronunciation: String,
        @SerialName("result")
        val result: String,
        @SerialName("time")
        val time: List<Long>
    )

    @Serializable
    enum class Status {
        @SerialName("recog-in-progress")
        RECOG_IN_PROGRESS,

        @SerialName("recog-finished")
        RECOG_FINISHED
    }
}
