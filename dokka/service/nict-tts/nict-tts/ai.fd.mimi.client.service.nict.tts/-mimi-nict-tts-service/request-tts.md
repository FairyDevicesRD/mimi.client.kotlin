//[nict-tts](../../../index.md)/[ai.fd.mimi.client.service.nict.tts](../index.md)/[MimiNictTtsService](index.md)/[requestTts](request-tts.md)

# requestTts

[common]\
suspend fun [requestTts](request-tts.md)(text: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), options: [MimiNictTtsOptions](../-mimi-nict-tts-options/index.md) = MimiNictTtsOptions.DEFAULT): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[MimiNictTtsResult](../-mimi-nict-tts-result/index.md)&gt;

Generates a voice reading the specified [text](request-tts.md) by the NICT TTS service.
