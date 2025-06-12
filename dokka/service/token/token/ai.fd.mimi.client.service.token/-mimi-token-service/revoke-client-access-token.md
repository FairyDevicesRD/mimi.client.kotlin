//[token](../../../index.md)/[ai.fd.mimi.client.service.token](../index.md)/[MimiTokenService](index.md)/[revokeClientAccessToken](revoke-client-access-token.md)

# revokeClientAccessToken

[common]\
suspend fun [revokeClientAccessToken](revoke-client-access-token.md)(applicationId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), clientId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), clientSecret: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), token: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)&gt;

Revokes the access token with client authority.

See [API Documentation](https://mimi.readme.io/docs/auth-api#22-%E3%82%AF%E3%83%A9%E3%82%A4%E3%82%A2%E3%83%B3%E3%83%88%E6%A8%A9%E9%99%90%E3%81%A7%E3%81%AE-revoke) for more detail.
