package ai.fd.mimi.client.service.token

/**
 * A result of the issued access token.
 */
data class MimiIssueTokenResult(
    val accessToken: String,
    val expiresIn: Int,
    val startTimestamp: Long,
    val endTimestamp: Long
)
