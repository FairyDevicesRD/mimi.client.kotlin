//[asr](../../../index.md)/[ai.fd.mimi.client.service.asr](../index.md)/[MimiAsrService](index.md)

# MimiAsrService

[common]\
class [MimiAsrService](index.md)

## Constructors

| | |
|---|---|
| [MimiAsrService](-mimi-asr-service.md) | [common]<br>constructor(engineFactory: MimiNetworkEngine.Factory, accessToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), useSsl: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = true, host: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;service.mimi.fd.ai&quot;, port: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = if (useSsl) 443 else 80, path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;&quot;) |

## Functions

| Name | Summary |
|---|---|
| [openAsrSession](open-asr-session.md) | [common]<br>suspend fun [openAsrSession](open-asr-session.md)(options: [MimiAsrOptions](../-mimi-asr-options/index.md) = MimiAsrOptions.DEFAULT): [MimiAsrWebSocketSession](../../../../asr-core/asr-core/ai.fd.mimi.client.service.asr.core/-mimi-asr-web-socket-session/index.md)&lt;[MimiAsrResult](../-mimi-asr-result/index.md)&gt; |
| [requestAsr](request-asr.md) | [common]<br>suspend fun [requestAsr](request-asr.md)(audioData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html), options: [MimiAsrOptions](../-mimi-asr-options/index.md) = MimiAsrOptions.DEFAULT): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiAsrResult](../-mimi-asr-result/index.md)&gt; |
