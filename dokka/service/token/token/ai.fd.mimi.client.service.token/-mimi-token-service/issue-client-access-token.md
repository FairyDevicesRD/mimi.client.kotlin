//[token](../../../index.md)/[ai.fd.mimi.client.service.token](../index.md)/[MimiTokenService](index.md)/[issueClientAccessToken](issue-client-access-token.md)

# issueClientAccessToken

[common]\
suspend fun [issueClientAccessToken](issue-client-access-token.md)(applicationId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), clientId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), clientSecret: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), scope: [MimiTokenScope](../-mimi-token-scope/index.md)): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiIssueTokenResult](../-mimi-issue-token-result/index.md)&gt;

suspend fun [issueClientAccessToken](issue-client-access-token.md)(applicationId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), clientId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), clientSecret: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), scopes: [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[MimiTokenScope](../-mimi-token-scope/index.md)&gt;): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiIssueTokenResult](../-mimi-issue-token-result/index.md)&gt;

Issues an access token with client authority by direct request from the client application.

See [API Documentation](https://mimi.readme.io/docs/auth-api#122-%E3%82%AF%E3%83%A9%E3%82%A4%E3%82%A2%E3%83%B3%E3%83%88%E3%81%8B%E3%82%89%E3%81%AE%E7%9B%B4%E6%8E%A5%E3%83%AA%E3%82%AF%E3%82%A8%E3%82%B9%E3%83%88) for more detail.
