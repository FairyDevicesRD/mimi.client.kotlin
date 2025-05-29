//[token](../../../index.md)/[ai.fd.mimi.client.service.token](../index.md)/[MimiTokenService](index.md)/[issueApplicationAccessToken](issue-application-access-token.md)

# issueApplicationAccessToken

[common]\
suspend fun [issueApplicationAccessToken](issue-application-access-token.md)(applicationId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), applicationSecret: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), scope: [MimiTokenScope](../-mimi-token-scope/index.md)): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiIssueTokenResult](../-mimi-issue-token-result/index.md)&gt;

suspend fun [issueApplicationAccessToken](issue-application-access-token.md)(applicationId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), applicationSecret: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), scopes: [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[MimiTokenScope](../-mimi-token-scope/index.md)&gt;): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiIssueTokenResult](../-mimi-issue-token-result/index.md)&gt;

Issues an access token with application authority.

See [API Documentation](https://mimi.readme.io/docs/auth-api#11-%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E6%A8%A9%E9%99%90%E3%81%A7%E3%81%AE%E7%99%BA%E8%A1%8C%EF%BC%88%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E5%86%85%E3%81%AB%E9%96%89%E3%81%98%E3%81%9F-root-%E6%A8%A9%E9%99%90%EF%BC%89) for more detail.
