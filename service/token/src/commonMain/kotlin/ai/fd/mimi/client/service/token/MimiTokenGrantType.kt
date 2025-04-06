package ai.fd.mimi.client.service.token

/**
 * A grant type for Mimi access tokens.
 * See [API Documentation](https://mimi.readme.io/reference/grant_type) for more information.
 */
enum class MimiTokenGrantType(internal val value: String) {
    DEVELOPER_CREDENTIALS("https://auth.mimi.fd.ai/grant_type/developer_credentials"),
    REFRESH_TOKEN("https://auth.mimi.fd.ai/grant_type/refresh_token"),
    APPLICATION_CREDENTIALS("https://auth.mimi.fd.ai/grant_type/application_credentials"),
    CLIENT_CREDENTIALS("https://auth.mimi.fd.ai/grant_type/client_credentials"),
    APPLICATION_CLIENT_CREDENTIALS("https://auth.mimi.fd.ai/grant_type/application_client_credentials")
}
