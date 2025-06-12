//[ktor](../../../index.md)/[ai.fd.mimi.client.engine.ktor](../index.md)/[MimiKtorWebSocketSession](index.md)

# MimiKtorWebSocketSession

[common]\
class [MimiKtorWebSocketSession](index.md)&lt;[T](index.md)&gt;(ktorWebSocketSession: DefaultClientWebSocketSession, converter: [MimiModelConverter.EncodableJsonString](../../../../core/core/ai.fd.mimi.client.engine/-mimi-model-converter/-encodable-json-string/index.md)&lt;[T](index.md)&gt;) : [MimiWebSocketSessionInternal](../../../../core/core/ai.fd.mimi.client.engine/-mimi-web-socket-session-internal/index.md)&lt;[T](index.md)&gt;

## Constructors

| | |
|---|---|
| [MimiKtorWebSocketSession](-mimi-ktor-web-socket-session.md) | [common]<br>constructor(ktorWebSocketSession: DefaultClientWebSocketSession, converter: [MimiModelConverter.EncodableJsonString](../../../../core/core/ai.fd.mimi.client.engine/-mimi-model-converter/-encodable-json-string/index.md)&lt;[T](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [isActive](is-active.md) | [common]<br>open override val [isActive](is-active.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [rxFlow](rx-flow.md) | [common]<br>open override val [rxFlow](rx-flow.md): Flow&lt;[T](index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [common]<br>open override fun [cancel](cancel.md)() |
| [sendBinary](send-binary.md) | [common]<br>open suspend override fun [sendBinary](send-binary.md)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html)) |
| [sendBinaryBlocking](index.md#-1691580279%2FFunctions%2F-1582084820) | [common]<br>open fun [sendBinaryBlocking](index.md#-1691580279%2FFunctions%2F-1582084820)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html)) |
| [sendJsonText](index.md#-294725056%2FFunctions%2F-1582084820) | [common]<br>suspend fun &lt;[R](index.md#-294725056%2FFunctions%2F-1582084820)&gt; [sendJsonText](index.md#-294725056%2FFunctions%2F-1582084820)(data: [R](index.md#-294725056%2FFunctions%2F-1582084820), serializer: KSerializer&lt;[R](index.md#-294725056%2FFunctions%2F-1582084820)&gt;) |
| [sendText](send-text.md) | [common]<br>open suspend override fun [sendText](send-text.md)(text: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |
