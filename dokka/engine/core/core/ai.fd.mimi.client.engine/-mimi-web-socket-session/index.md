//[core](../../../index.md)/[ai.fd.mimi.client.engine](../index.md)/[MimiWebSocketSession](index.md)

# MimiWebSocketSession

interface [MimiWebSocketSession](index.md)&lt;[T](index.md)&gt;

An interface of a WebSocket session for mimi service.

Data sent from the server side is notified through [rxFlow](rx-flow.md). It is disconnected when this [rxFlow](rx-flow.md) subscription is canceled or when [cancel](cancel.md) is called. In other words, it disconnects without graceful shutdown. Each mimi service has its own graceful shutdown feature, please refer to the documentation of each mimi service's session implementation class.

#### Inheritors

| |
|---|
| [MimiWebSocketSessionInternal](../-mimi-web-socket-session-internal/index.md) |

## Properties

| Name | Summary |
|---|---|
| [isActive](is-active.md) | [common]<br>abstract val [isActive](is-active.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the WebSocket connection has been successfully created and can communicate to the server. This becomes `false` when the connection is disconnected or graceful shutdown is initiated as defined by each service. |
| [rxFlow](rx-flow.md) | [common]<br>abstract val [rxFlow](rx-flow.md): Flow&lt;[T](index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [common]<br>abstract fun [cancel](cancel.md)() |
| [sendBinary](send-binary.md) | [common]<br>abstract suspend fun [sendBinary](send-binary.md)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html))<br>Sends voice binary data to the server side. If there is room in the buffer, this function is executed immediately. If there is no buffer space, the function suspends until one is available. |
| [sendBinaryBlocking](send-binary-blocking.md) | [common]<br>open fun [sendBinaryBlocking](send-binary-blocking.md)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html)) |
