//[core](../../../../index.md)/[ai.fd.mimi.client.engine](../../index.md)/[MimiModelConverter](../index.md)/[EncodableJsonString](index.md)

# EncodableJsonString

[common]\
abstract class [EncodableJsonString](index.md)&lt;[T](index.md)&gt;(json: Json) : [MimiModelConverter.JsonString](../-json-string/index.md)&lt;[T](index.md)&gt;

## Constructors

| | |
|---|---|
| [EncodableJsonString](-encodable-json-string.md) | [common]<br>constructor(json: Json) |

## Functions

| Name | Summary |
|---|---|
| [decode](../-json-string/decode.md) | [common]<br>abstract fun [decode](../-json-string/decode.md)(jsonText: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [T](index.md) |
| [encode](encode.md) | [common]<br>fun &lt;[R](encode.md)&gt; [encode](encode.md)(model: [R](encode.md), serializer: KSerializer&lt;[R](encode.md)&gt;): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
