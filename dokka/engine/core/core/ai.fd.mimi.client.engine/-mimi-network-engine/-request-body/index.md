//[core](../../../../index.md)/[ai.fd.mimi.client.engine](../../index.md)/[MimiNetworkEngine](../index.md)/[RequestBody](index.md)

# RequestBody

sealed interface [RequestBody](index.md)

#### Inheritors

| |
|---|
| [Binary](-binary/index.md) |
| [FormData](-form-data/index.md) |

## Types

| Name | Summary |
|---|---|
| [Binary](-binary/index.md) | [common]<br>data class [Binary](-binary/index.md)(val data: ByteString, val contentType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) : [MimiNetworkEngine.RequestBody](index.md) |
| [FormData](-form-data/index.md) | [common]<br>data class [FormData](-form-data/index.md)(val fields: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;) : [MimiNetworkEngine.RequestBody](index.md) |
