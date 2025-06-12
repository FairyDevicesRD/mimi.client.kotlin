//[core](../../../index.md)/[ai.fd.mimi.client.engine](../index.md)/[MimiNetworkEngine](index.md)/[openWebSocketSessionInternal](open-web-socket-session-internal.md)

# openWebSocketSessionInternal

[common]\

@VisibleForTesting(otherwise = 4)

abstract suspend fun &lt;[T](open-web-socket-session-internal.md)&gt; [openWebSocketSessionInternal](open-web-socket-session-internal.md)(path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), contentType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyMap(), converter: [MimiModelConverter.EncodableJsonString](../-mimi-model-converter/-encodable-json-string/index.md)&lt;[T](open-web-socket-session-internal.md)&gt;): [MimiWebSocketSessionInternal](../-mimi-web-socket-session-internal/index.md)&lt;[T](open-web-socket-session-internal.md)&gt;
