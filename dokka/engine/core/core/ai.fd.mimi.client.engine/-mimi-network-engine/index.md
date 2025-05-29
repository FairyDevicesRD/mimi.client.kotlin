//[core](../../../index.md)/[ai.fd.mimi.client.engine](../index.md)/[MimiNetworkEngine](index.md)

# MimiNetworkEngine

[common]\
abstract class [MimiNetworkEngine](index.md)

## Constructors

| | |
|---|---|
| [MimiNetworkEngine](-mimi-network-engine.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |
| [Factory](-factory/index.md) | [common]<br>interface [Factory](-factory/index.md) |
| [RequestBody](-request-body/index.md) | [common]<br>sealed interface [RequestBody](-request-body/index.md) |

## Functions

| Name | Summary |
|---|---|
| [openWebSocketSession](open-web-socket-session.md) | [common]<br>suspend fun &lt;[T](open-web-socket-session.md)&gt; [openWebSocketSession](open-web-socket-session.md)(path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), accessToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, contentType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyMap(), converter: [MimiModelConverter.EncodableJsonString](../-mimi-model-converter/-encodable-json-string/index.md)&lt;[T](open-web-socket-session.md)&gt;): [MimiWebSocketSessionInternal](../-mimi-web-socket-session-internal/index.md)&lt;[T](open-web-socket-session.md)&gt; |
| [openWebSocketSessionInternal](open-web-socket-session-internal.md) | [common]<br>@VisibleForTesting(otherwise = 4)<br>abstract suspend fun &lt;[T](open-web-socket-session-internal.md)&gt; [openWebSocketSessionInternal](open-web-socket-session-internal.md)(path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), contentType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyMap(), converter: [MimiModelConverter.EncodableJsonString](../-mimi-model-converter/-encodable-json-string/index.md)&lt;[T](open-web-socket-session-internal.md)&gt;): [MimiWebSocketSessionInternal](../-mimi-web-socket-session-internal/index.md)&lt;[T](open-web-socket-session-internal.md)&gt; |
| [request](request.md) | [common]<br>suspend fun &lt;[T](request.md)&gt; [request](request.md)(path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), accessToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, requestBody: [MimiNetworkEngine.RequestBody](-request-body/index.md), headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyMap(), converter: [MimiModelConverter](../-mimi-model-converter/index.md)&lt;[T](request.md)&gt;): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[T](request.md)&gt; |
| [requestAsBinaryInternal](request-as-binary-internal.md) | [common]<br>@VisibleForTesting(otherwise = 4)<br>abstract suspend fun [requestAsBinaryInternal](request-as-binary-internal.md)(path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), requestBody: [MimiNetworkEngine.RequestBody](-request-body/index.md), headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyMap()): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;ByteString&gt; |
| [requestAsStringInternal](request-as-string-internal.md) | [common]<br>@VisibleForTesting(otherwise = 4)<br>abstract suspend fun [requestAsStringInternal](request-as-string-internal.md)(path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), requestBody: [MimiNetworkEngine.RequestBody](-request-body/index.md), headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyMap()): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; |
