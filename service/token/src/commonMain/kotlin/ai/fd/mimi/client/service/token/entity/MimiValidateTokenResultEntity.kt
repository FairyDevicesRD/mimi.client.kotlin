package ai.fd.mimi.client.service.token.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MimiValidateTokenResultEntity(
    @SerialName("tokenStatus")
    val tokenStatus: String,
)
