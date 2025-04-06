package ai.fd.mimi.client.service.token

/**
 * A result of issued access token.
 */
data class MimiTokenResult(
    val accessToken: String,
    val expiresIn: Int,
    val startTimestamp: Long,
    val endTimestamp: Long
)
