//[token](../../../index.md)/[ai.fd.mimi.client.service.token](../index.md)/[MimiTokenService](index.md)/[revokeApplicationAccessToken](revoke-application-access-token.md)

# revokeApplicationAccessToken

[common]\
suspend fun [revokeApplicationAccessToken](revoke-application-access-token.md)(applicationId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), applicationSecret: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), token: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)&gt;

Revokes the access token with application authority.

See [API Documentation](https://mimi.readme.io/docs/auth-api#21-%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E6%A8%A9%E9%99%90%E3%81%A7%E3%81%AE-revoke) for more detail.
