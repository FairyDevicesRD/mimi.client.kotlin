//[nict-tts](../../../index.md)/[ai.fd.mimi.client.service.nict.tts](../index.md)/[MimiNictTtsOptions](index.md)

# MimiNictTtsOptions

[common]\
data class [MimiNictTtsOptions](index.md)(val language: [MimiNictTtsOptions.Language](-language/index.md), val audioFormat: [MimiNictTtsOptions.AudioFormat](-audio-format/index.md), val audioEndian: [MimiNictTtsOptions.AudioEndian](-audio-endian/index.md), val gender: [MimiNictTtsOptions.Gender](-gender/index.md), @FloatRange(from = 0.5, to = 2.0)val rate: [Float](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-float/index.html))

Options for requesting the NICT TTS service.

For more information on each option, refer to [the API reference](https://mimi.readme.io/reference/speech-synthesis).

## Constructors

| | |
|---|---|
| [MimiNictTtsOptions](-mimi-nict-tts-options.md) | [common]<br>constructor(language: [MimiNictTtsOptions.Language](-language/index.md), audioFormat: [MimiNictTtsOptions.AudioFormat](-audio-format/index.md), audioEndian: [MimiNictTtsOptions.AudioEndian](-audio-endian/index.md), gender: [MimiNictTtsOptions.Gender](-gender/index.md), @FloatRange(from = 0.5, to = 2.0)rate: [Float](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-float/index.html)) |

## Types

| Name | Summary |
|---|---|
| [AudioEndian](-audio-endian/index.md) | [common]<br>enum [AudioEndian](-audio-endian/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[MimiNictTtsOptions.AudioEndian](-audio-endian/index.md)&gt; <br>Endian of the generated audio binary. |
| [AudioFormat](-audio-format/index.md) | [common]<br>enum [AudioFormat](-audio-format/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[MimiNictTtsOptions.AudioFormat](-audio-format/index.md)&gt; <br>Audio format of the generated audio binary. |
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |
| [Gender](-gender/index.md) | [common]<br>enum [Gender](-gender/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[MimiNictTtsOptions.Gender](-gender/index.md)&gt; <br>Gender of the generated voice. |
| [Language](-language/index.md) | [common]<br>enum [Language](-language/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[MimiNictTtsOptions.Language](-language/index.md)&gt; <br>Language of the generated voice. |

## Properties

| Name | Summary |
|---|---|
| [audioEndian](audio-endian.md) | [common]<br>val [audioEndian](audio-endian.md): [MimiNictTtsOptions.AudioEndian](-audio-endian/index.md) |
| [audioFormat](audio-format.md) | [common]<br>val [audioFormat](audio-format.md): [MimiNictTtsOptions.AudioFormat](-audio-format/index.md) |
| [gender](gender.md) | [common]<br>val [gender](gender.md): [MimiNictTtsOptions.Gender](-gender/index.md) |
| [language](language.md) | [common]<br>val [language](language.md): [MimiNictTtsOptions.Language](-language/index.md) |
| [rate](rate.md) | [common]<br>val [rate](rate.md): [Float](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-float/index.html) |
