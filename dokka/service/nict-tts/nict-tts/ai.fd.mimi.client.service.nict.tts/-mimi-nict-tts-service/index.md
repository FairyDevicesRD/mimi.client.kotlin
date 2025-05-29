//[nict-tts](../../../index.md)/[ai.fd.mimi.client.service.nict.tts](../index.md)/[MimiNictTtsService](index.md)

# MimiNictTtsService

[common]\
class [MimiNictTtsService](index.md)

A service class to interact with [the NICT TTS service](https://mimi.readme.io/docs/tts-http-service).

## Constructors

| | |
|---|---|
| [MimiNictTtsService](-mimi-nict-tts-service.md) | [common]<br>constructor(engineFactory: MimiNetworkEngine.Factory, accessToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), useSsl: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = true, host: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;tts.mimi.fd.ai&quot;, path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;speech_synthesis&quot;, port: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = if (useSsl) 443 else 80) |

## Functions

| Name | Summary |
|---|---|
| [requestTts](request-tts.md) | [common]<br>suspend fun [requestTts](request-tts.md)(text: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), options: [MimiNictTtsOptions](../-mimi-nict-tts-options/index.md) = MimiNictTtsOptions.DEFAULT): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiNictTtsResult](../-mimi-nict-tts-result/index.md)&gt;<br>Generates a voice reading the specified [text](request-tts.md) by the NICT TTS service. |
