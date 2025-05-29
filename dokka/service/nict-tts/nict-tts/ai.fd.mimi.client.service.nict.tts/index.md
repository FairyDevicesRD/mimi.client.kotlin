//[nict-tts](../../index.md)/[ai.fd.mimi.client.service.nict.tts](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [MimiNictTtsOptions](-mimi-nict-tts-options/index.md) | [common]<br>data class [MimiNictTtsOptions](-mimi-nict-tts-options/index.md)(val language: [MimiNictTtsOptions.Language](-mimi-nict-tts-options/-language/index.md), val audioFormat: [MimiNictTtsOptions.AudioFormat](-mimi-nict-tts-options/-audio-format/index.md), val audioEndian: [MimiNictTtsOptions.AudioEndian](-mimi-nict-tts-options/-audio-endian/index.md), val gender: [MimiNictTtsOptions.Gender](-mimi-nict-tts-options/-gender/index.md), @FloatRange(from = 0.5, to = 2.0)val rate: [Float](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-float/index.html))<br>Options for requesting the NICT TTS service. |
| [MimiNictTtsResult](-mimi-nict-tts-result/index.md) | [common]<br>class [MimiNictTtsResult](-mimi-nict-tts-result/index.md)(val audioBinary: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html))<br>A result of the NICT TTS service. |
| [MimiNictTtsService](-mimi-nict-tts-service/index.md) | [common]<br>class [MimiNictTtsService](-mimi-nict-tts-service/index.md)<br>A service class to interact with [the NICT TTS service](https://mimi.readme.io/docs/tts-http-service). |
