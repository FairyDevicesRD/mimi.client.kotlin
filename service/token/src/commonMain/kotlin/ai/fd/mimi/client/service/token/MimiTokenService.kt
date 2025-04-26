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

    /**
     * Issues an access token with client authority by direct request from the client application.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#122-%E3%82%AF%E3%83%A9%E3%82%A4%E3%82%A2%E3%83%B3%E3%83%88%E3%81%8B%E3%82%89%E3%81%AE%E7%9B%B4%E6%8E%A5%E3%83%AA%E3%82%AF%E3%82%A8%E3%82%B9%E3%83%88) for more detail.
     */
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

    /**
     * Issues an access token with client authority by direct request from the client application.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#122-%E3%82%AF%E3%83%A9%E3%82%A4%E3%82%A2%E3%83%B3%E3%83%88%E3%81%8B%E3%82%89%E3%81%AE%E7%9B%B4%E6%8E%A5%E3%83%AA%E3%82%AF%E3%82%A8%E3%82%B9%E3%83%88) for more detail.
     */
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
