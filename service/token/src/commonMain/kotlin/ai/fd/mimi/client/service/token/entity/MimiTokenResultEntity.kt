package ai.fd.mimi.client.service.token.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MimiTokenResultEntity(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("startTimestamp")
    val startTimestamp: Long,
    @SerialName("endTimestamp")
    val endTimestamp: Long
)
