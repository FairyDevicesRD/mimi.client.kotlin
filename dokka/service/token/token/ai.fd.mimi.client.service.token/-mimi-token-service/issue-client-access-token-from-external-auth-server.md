//[token](../../../index.md)/[ai.fd.mimi.client.service.token](../index.md)/[MimiTokenService](index.md)/[issueClientAccessTokenFromExternalAuthServer](issue-client-access-token-from-external-auth-server.md)

# issueClientAccessTokenFromExternalAuthServer

[common]\
suspend fun [issueClientAccessTokenFromExternalAuthServer](issue-client-access-token-from-external-auth-server.md)(applicationId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), clientId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), applicationSecret: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), scope: [MimiTokenScope](../-mimi-token-scope/index.md)): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiIssueTokenResult](../-mimi-issue-token-result/index.md)&gt;

suspend fun [issueClientAccessTokenFromExternalAuthServer](issue-client-access-token-from-external-auth-server.md)(applicationId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), clientId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), applicationSecret: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), scopes: [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[MimiTokenScope](../-mimi-token-scope/index.md)&gt;): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiIssueTokenResult](../-mimi-issue-token-result/index.md)&gt;

Issues an access token with client authority by transfer request from an external authentication server.

See [API Documentation](https://mimi.readme.io/docs/auth-api#121-%E5%A4%96%E9%83%A8%E8%AA%8D%E8%A8%BC%E3%82%B5%E3%83%BC%E3%83%90%E3%83%BC%E3%81%8B%E3%82%89%E3%81%AE%E8%BB%A2%E9%80%81%E3%83%AA%E3%82%AF%E3%82%A8%E3%82%B9%E3%83%88) for more detail.
