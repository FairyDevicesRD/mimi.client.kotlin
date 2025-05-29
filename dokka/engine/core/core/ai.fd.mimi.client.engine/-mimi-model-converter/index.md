//[core](../../../index.md)/[ai.fd.mimi.client.engine](../index.md)/[MimiModelConverter](index.md)

# MimiModelConverter

sealed interface [MimiModelConverter](index.md)&lt;[T](index.md)&gt;

#### Inheritors

| |
|---|
| [JsonString](-json-string/index.md) |
| [Binary](-binary/index.md) |

## Types

| Name | Summary |
|---|---|
| [Binary](-binary/index.md) | [common]<br>abstract class [Binary](-binary/index.md)&lt;[T](-binary/index.md)&gt; : [MimiModelConverter](index.md)&lt;[T](-binary/index.md)&gt; |
| [EncodableJsonString](-encodable-json-string/index.md) | [common]<br>abstract class [EncodableJsonString](-encodable-json-string/index.md)&lt;[T](-encodable-json-string/index.md)&gt;(json: Json) : [MimiModelConverter.JsonString](-json-string/index.md)&lt;[T](-encodable-json-string/index.md)&gt; |
| [JsonString](-json-string/index.md) | [common]<br>abstract class [JsonString](-json-string/index.md)&lt;[T](-json-string/index.md)&gt; : [MimiModelConverter](index.md)&lt;[T](-json-string/index.md)&gt; |
