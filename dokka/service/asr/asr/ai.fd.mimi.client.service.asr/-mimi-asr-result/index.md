//[asr](../../../index.md)/[ai.fd.mimi.client.service.asr](../index.md)/[MimiAsrResult](index.md)

# MimiAsrResult

[common]\
data class [MimiAsrResult](index.md)(val type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val sessionId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val status: [MimiAsrResultStatus](../../../../asr-core/asr-core/ai.fd.mimi.client.service.asr.core/-mimi-asr-result-status/index.md), val response: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MimiAsrResult.Response](-response/index.md)&gt;)

## Constructors

| | |
|---|---|
| [MimiAsrResult](-mimi-asr-result.md) | [common]<br>constructor(type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), sessionId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), status: [MimiAsrResultStatus](../../../../asr-core/asr-core/ai.fd.mimi.client.service.asr.core/-mimi-asr-result-status/index.md), response: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MimiAsrResult.Response](-response/index.md)&gt;) |

## Types

| Name | Summary |
|---|---|
| [Response](-response/index.md) | [common]<br>data class [Response](-response/index.md)(val pronunciation: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val result: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val startTime: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), val endTime: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [response](response.md) | [common]<br>val [response](response.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MimiAsrResult.Response](-response/index.md)&gt; |
| [sessionId](session-id.md) | [common]<br>val [sessionId](session-id.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [status](status.md) | [common]<br>val [status](status.md): [MimiAsrResultStatus](../../../../asr-core/asr-core/ai.fd.mimi.client.service.asr.core/-mimi-asr-result-status/index.md) |
| [type](type.md) | [common]<br>val [type](type.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
