package ai.fd.mimi.client.service.asr.entity

import ai.fd.mimi.client.service.asr.core.entity.MimiAsrResultStatusEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MimiAsrResultEntity(
    @SerialName("type")
    val type: String,
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("status")
    val status: MimiAsrResultStatusEntity,
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
}
