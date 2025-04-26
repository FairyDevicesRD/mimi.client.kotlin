package ai.fd.mimi.client.service.token

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine

/**
 * A service class to issue access tokens for Mimi services.
 */
class MimiTokenService internal constructor(
    private val engine: MimiNetworkEngine,
    private val converter: MimiModelConverter.JsonString<MimiTokenResult>
) {

    constructor(
        engineFactory: MimiNetworkEngine.Factory,
        useSsl: Boolean = true,
        host: String = "auth.mimi.fd.ai",
        path: String = "v2/token",
        port: Int = if (useSsl) 443 else 80
    ) : this(
        engine = engineFactory.create(useSsl = useSsl, host = host, port = port, path = path),
        converter = MimiTokenModelConverter()
    )

    suspend fun issueClientAccessToken(
        applicationId: String,
        clientId: String,
        clientSecret: String,
        scope: MimiTokenScope
    ): Result<MimiTokenResult> = issueClientAccessToken(
        applicationId = applicationId,
        clientId = clientId,
        clientSecret = clientSecret,
        scopes = setOf(scope)
    )

    suspend fun issueClientAccessToken(
        applicationId: String,
        clientId: String,
        clientSecret: String,
        scopes: Set<MimiTokenScope>
    ): Result<MimiTokenResult> = issueAccessToken(
        clientId = "${applicationId}:${clientId}",
        clientSecret = clientSecret,
        grantType = MimiTokenGrantType.CLIENT_CREDENTIALS,
        scopes = scopes
    )

    private suspend fun issueAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: MimiTokenGrantType,
        scopes: Set<MimiTokenScope>
    ): Result<MimiTokenResult> = engine.request(
        requestBody = MimiNetworkEngine.RequestBody.FormData(
            fields = mapOf(
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "grant_type" to grantType.value,
                "scope" to scopes.getContainingScopes().joinToString(";") { it.value }
            )
        ),
        converter = converter,
        accessToken = null
    )
}
