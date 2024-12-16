package ai.fd.mimi.client.service.nict.asr.entity

import ai.fd.mimi.client.service.asr.core.entity.MimiAsrResultStatusEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MimiNictAsrV1ResultEntity(
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
        @SerialName("result")
        val result: String
    )
}
