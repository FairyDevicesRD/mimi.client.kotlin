//[asr-core](../../../index.md)/[ai.fd.mimi.client.service.asr.core.entity](../index.md)/[MimiAsrResultStatusEntity](index.md)

# MimiAsrResultStatusEntity

[common]\
@Serializable

enum [MimiAsrResultStatusEntity](index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[MimiAsrResultStatusEntity](index.md)&gt;

## Entries

| | |
|---|---|
| [RECOG_IN_PROGRESS](-r-e-c-o-g_-i-n_-p-r-o-g-r-e-s-s/index.md) | [common]<br>@SerialName(value = &quot;recog-in-progress&quot;)<br>[RECOG_IN_PROGRESS](-r-e-c-o-g_-i-n_-p-r-o-g-r-e-s-s/index.md) |
| [RECOG_FINISHED](-r-e-c-o-g_-f-i-n-i-s-h-e-d/index.md) | [common]<br>@SerialName(value = &quot;recog-finished&quot;)<br>[RECOG_FINISHED](-r-e-c-o-g_-f-i-n-i-s-h-e-d/index.md) |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [common]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.enums/-enum-entries/index.html)&lt;[MimiAsrResultStatusEntity](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](-r-e-c-o-g_-f-i-n-i-s-h-e-d/index.md#-372974862%2FProperties%2F115902251) | [common]<br>val [name](-r-e-c-o-g_-f-i-n-i-s-h-e-d/index.md#-372974862%2FProperties%2F115902251): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](-r-e-c-o-g_-f-i-n-i-s-h-e-d/index.md#-739389684%2FProperties%2F115902251) | [common]<br>val [ordinal](-r-e-c-o-g_-f-i-n-i-s-h-e-d/index.md#-739389684%2FProperties%2F115902251): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [toStatus](to-status.md) | [common]<br>fun [toStatus](to-status.md)(): [MimiAsrResultStatus](../../ai.fd.mimi.client.service.asr.core/-mimi-asr-result-status/index.md) |
| [valueOf](value-of.md) | [common]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [MimiAsrResultStatusEntity](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[MimiAsrResultStatusEntity](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
