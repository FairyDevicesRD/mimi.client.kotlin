//[core](../../../index.md)/[ai.fd.mimi.client.engine](../index.md)/[MimiWebSocketSessionInternal](index.md)

# MimiWebSocketSessionInternal

[common]\
abstract class [MimiWebSocketSessionInternal](index.md)&lt;[T](index.md)&gt;(converter: [MimiModelConverter.EncodableJsonString](../-mimi-model-converter/-encodable-json-string/index.md)&lt;[T](index.md)&gt;) : [MimiWebSocketSession](../-mimi-web-socket-session/index.md)&lt;[T](index.md)&gt;

## Constructors

| | |
|---|---|
| [MimiWebSocketSessionInternal](-mimi-web-socket-session-internal.md) | [common]<br>constructor(converter: [MimiModelConverter.EncodableJsonString](../-mimi-model-converter/-encodable-json-string/index.md)&lt;[T](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [isActive](../-mimi-web-socket-session/is-active.md) | [common]<br>abstract val [isActive](../-mimi-web-socket-session/is-active.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the WebSocket connection has been successfully created and can communicate to the server. This becomes `false` when the connection is disconnected or graceful shutdown is initiated as defined by each service. |
| [rxFlow](../-mimi-web-socket-session/rx-flow.md) | [common]<br>abstract val [rxFlow](../-mimi-web-socket-session/rx-flow.md): Flow&lt;[T](index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [cancel](../-mimi-web-socket-session/cancel.md) | [common]<br>abstract fun [cancel](../-mimi-web-socket-session/cancel.md)() |
| [sendBinary](../-mimi-web-socket-session/send-binary.md) | [common]<br>abstract suspend fun [sendBinary](../-mimi-web-socket-session/send-binary.md)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html))<br>Sends voice binary data to the server side. If there is room in the buffer, this function is executed immediately. If there is no buffer space, the function suspends until one is available. |
| [sendBinaryBlocking](../-mimi-web-socket-session/send-binary-blocking.md) | [common]<br>open fun [sendBinaryBlocking](../-mimi-web-socket-session/send-binary-blocking.md)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html)) |
| [sendJsonText](send-json-text.md) | [common]<br>suspend fun &lt;[R](send-json-text.md)&gt; [sendJsonText](send-json-text.md)(data: [R](send-json-text.md), serializer: KSerializer&lt;[R](send-json-text.md)&gt;) |
| [sendText](send-text.md) | [common]<br>@VisibleForTesting(otherwise = 4)<br>abstract suspend fun [sendText](send-text.md)(text: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |
