//[nict-asr](../../../index.md)/[ai.fd.mimi.client.service.nict.asr](../index.md)/[MimiNictAsrV1Service](index.md)

# MimiNictAsrV1Service

[common]\
class [MimiNictAsrV1Service](index.md)

## Constructors

| | |
|---|---|
| [MimiNictAsrV1Service](-mimi-nict-asr-v1-service.md) | [common]<br>constructor(engineFactory: MimiNetworkEngine.Factory, accessToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), useSsl: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = true, host: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;service.mimi.fd.ai&quot;, port: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = if (useSsl) 443 else 80, path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;&quot;) |

## Functions

| Name | Summary |
|---|---|
| [openAsrSession](open-asr-session.md) | [common]<br>suspend fun [openAsrSession](open-asr-session.md)(options: [MimiNictAsrV1Options](../-mimi-nict-asr-v1-options/index.md) = MimiNictAsrV1Options.DEFAULT): [MimiAsrWebSocketSession](../../../../asr-core/asr-core/ai.fd.mimi.client.service.asr.core/-mimi-asr-web-socket-session/index.md)&lt;[MimiNictAsrV1Result](../-mimi-nict-asr-v1-result/index.md)&gt; |
| [requestAsr](request-asr.md) | [common]<br>suspend fun [requestAsr](request-asr.md)(audioData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html), options: [MimiNictAsrV1Options](../-mimi-nict-asr-v1-options/index.md) = MimiNictAsrV1Options.DEFAULT): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiNictAsrV1Result](../-mimi-nict-asr-v1-result/index.md)&gt; |
