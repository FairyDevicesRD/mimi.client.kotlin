package ai.fd.mimi.client.service.token

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine

/**
 * A service class to issue access tokens for Mimi services.
 */
class MimiTokenService internal constructor(
    private val issueAccessTokenPath: String,
    private val revokeAccessTokenPath: String,
    private val validateAccessTokenPath: String,
    private val engine: MimiNetworkEngine,
    private val issueTokenResultConverter: MimiModelConverter.JsonString<MimiIssueTokenResult>,
    private val revokeTokenResultConverter: MimiModelConverter<Unit>,
    private val validateTokenResultConverter: MimiModelConverter.JsonString<MimiValidateTokenResult>,
) {

    constructor(
        engineFactory: MimiNetworkEngine.Factory,
        useSsl: Boolean = true,
        host: String = "auth.mimi.fd.ai",
        issueAccessTokenPath: String = "v2/token",
        revokeAccessTokenPath: String = "v2/revoke",
        validateAccessTokenPath: String = "v2/validate",
        port: Int = if (useSsl) 443 else 80
    ) : this(
        issueAccessTokenPath = issueAccessTokenPath,
        revokeAccessTokenPath = revokeAccessTokenPath,
        validateAccessTokenPath = validateAccessTokenPath,
        engine = engineFactory.create(useSsl = useSsl, host = host, port = port),
        issueTokenResultConverter = MimiIssueTokenModelConverter(),
        revokeTokenResultConverter = MimiModelConverter.JsonString.IgnoreResult,
        validateTokenResultConverter = MimiValidateTokenModelConverter()
    )


    /**
     * Issues an access token with application authority.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#11-%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E6%A8%A9%E9%99%90%E3%81%A7%E3%81%AE%E7%99%BA%E8%A1%8C%EF%BC%88%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E5%86%85%E3%81%AB%E9%96%89%E3%81%98%E3%81%9F-root-%E6%A8%A9%E9%99%90%EF%BC%89) for more detail.
     */
    suspend fun issueApplicationAccessToken(
        applicationId: String,
        applicationSecret: String,
        scope: MimiTokenScope
    ): Result<MimiIssueTokenResult> = issueApplicationAccessToken(
        applicationId = applicationId,
        applicationSecret = applicationSecret,
        scopes = setOf(scope)
    )

    /**
     * Issues an access token with application authority.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#11-%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E6%A8%A9%E9%99%90%E3%81%A7%E3%81%AE%E7%99%BA%E8%A1%8C%EF%BC%88%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E5%86%85%E3%81%AB%E9%96%89%E3%81%98%E3%81%9F-root-%E6%A8%A9%E9%99%90%EF%BC%89) for more detail.
     */
    suspend fun issueApplicationAccessToken(
        applicationId: String,
        applicationSecret: String,
        scopes: Set<MimiTokenScope>
    ): Result<MimiIssueTokenResult> = issueAccessToken(
        clientId = applicationId,
        clientSecret = applicationSecret,
        grantType = MimiTokenGrantType.APPLICATION_CREDENTIALS,
        scopes = scopes
    )

    /**
     * Issues an access token with client authority by transfer request from an external authentication server.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#121-%E5%A4%96%E9%83%A8%E8%AA%8D%E8%A8%BC%E3%82%B5%E3%83%BC%E3%83%90%E3%83%BC%E3%81%8B%E3%82%89%E3%81%AE%E8%BB%A2%E9%80%81%E3%83%AA%E3%82%AF%E3%82%A8%E3%82%B9%E3%83%88) for more detail.
     */
    suspend fun issueClientAccessTokenFromExternalAuthServer(
        applicationId: String,
        clientId: String,
        applicationSecret: String,
        scope: MimiTokenScope
    ): Result<MimiIssueTokenResult> = issueClientAccessTokenFromExternalAuthServer(
        applicationId = applicationId,
        clientId = clientId,
        applicationSecret = applicationSecret,
        scopes = setOf(scope)
    )

    /**
     * Issues an access token with client authority by transfer request from an external authentication server.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#121-%E5%A4%96%E9%83%A8%E8%AA%8D%E8%A8%BC%E3%82%B5%E3%83%BC%E3%83%90%E3%83%BC%E3%81%8B%E3%82%89%E3%81%AE%E8%BB%A2%E9%80%81%E3%83%AA%E3%82%AF%E3%82%A8%E3%82%B9%E3%83%88) for more detail.
     */
    suspend fun issueClientAccessTokenFromExternalAuthServer(
        applicationId: String,
        clientId: String,
        applicationSecret: String,
        scopes: Set<MimiTokenScope>
    ): Result<MimiIssueTokenResult> = issueAccessToken(
        clientId = "${applicationId}:${clientId}",
        clientSecret = applicationSecret,
        grantType = MimiTokenGrantType.APPLICATION_CLIENT_CREDENTIALS,
        scopes = scopes
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
    ): Result<MimiIssueTokenResult> = issueClientAccessToken(
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
    ): Result<MimiIssueTokenResult> = issueAccessToken(
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
    ): Result<MimiIssueTokenResult> = engine.request(
        path = issueAccessTokenPath,
        requestBody = MimiNetworkEngine.RequestBody.FormData(
            fields = mapOf(
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "grant_type" to grantType.value,
                "scope" to scopes.getContainingScopes().joinToString(";") { it.value }
            )
        ),
        converter = issueTokenResultConverter,
        accessToken = null
    )

    /**
     * Revokes the access token with application authority.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#21-%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E6%A8%A9%E9%99%90%E3%81%A7%E3%81%AE-revoke) for more detail.
     */
    suspend fun revokeApplicationAccessToken(
        applicationId: String,
        applicationSecret: String,
        token: String
    ): Result<Unit> = revokeAccessToken(
        clientId = applicationId,
        clientSecret = applicationSecret,
        token = token
    )

    /**
     * Revokes the access token with client authority.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#22-%E3%82%AF%E3%83%A9%E3%82%A4%E3%82%A2%E3%83%B3%E3%83%88%E6%A8%A9%E9%99%90%E3%81%A7%E3%81%AE-revoke) for more detail.
     */
    suspend fun revokeClientAccessToken(
        applicationId: String,
        clientId: String,
        clientSecret: String,
        token: String
    ): Result<Unit> = revokeAccessToken(
        clientId = "${applicationId}:${clientId}",
        clientSecret = clientSecret,
        token = token
    )

    private suspend fun revokeAccessToken(
        clientId: String,
        clientSecret: String,
        token: String
    ): Result<Unit> = engine.request(
        path = revokeAccessTokenPath,
        requestBody = MimiNetworkEngine.RequestBody.FormData(
            fields = mapOf(
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "token" to token
            )
        ),
        converter = revokeTokenResultConverter,
        accessToken = null
    )

    /**
     * Revokes the access token with client authority.
     *
     * See [API Documentation](https://mimi.readme.io/docs/auth-api#3-%E5%8F%96%E5%BE%97%E6%B8%88%E3%81%BF%E3%81%AEmimi-api%E3%82%A2%E3%82%AF%E3%82%BB%E3%82%B9%E3%83%88%E3%83%BC%E3%82%AF%E3%83%B3%E3%81%AE%E6%9C%89%E5%8A%B9%E6%80%A7%E3%81%AE%E7%A2%BA%E8%AA%8D%EF%BC%88validate%EF%BC%89) for more detail.
     */
    suspend fun validateAccessToken(token: String): Result<MimiValidateTokenResult> = engine.request(
        path = validateAccessTokenPath,
        requestBody = MimiNetworkEngine.RequestBody.FormData(
            fields = mapOf(
                "token" to token
            )
        ),
        converter = validateTokenResultConverter,
        accessToken = null
    )
}
