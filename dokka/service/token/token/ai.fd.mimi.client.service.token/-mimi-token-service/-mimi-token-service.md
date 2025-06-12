//[token](../../../index.md)/[ai.fd.mimi.client.service.token](../index.md)/[MimiTokenService](index.md)/[MimiTokenService](-mimi-token-service.md)

# MimiTokenService

[common]\
constructor(engineFactory: MimiNetworkEngine.Factory, useSsl: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = true, host: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;auth.mimi.fd.ai&quot;, issueAccessTokenPath: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;v2/token&quot;, revokeAccessTokenPath: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;v2/revoke&quot;, validateAccessTokenPath: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;v2/validate&quot;, port: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = if (useSsl) 443 else 80)
