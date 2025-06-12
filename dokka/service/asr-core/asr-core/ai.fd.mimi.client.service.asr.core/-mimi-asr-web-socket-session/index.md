//[asr-core](../../../index.md)/[ai.fd.mimi.client.service.asr.core](../index.md)/[MimiAsrWebSocketSession](index.md)

# MimiAsrWebSocketSession

[common]\
class [MimiAsrWebSocketSession](index.md)&lt;[T](index.md)&gt;(webSocketSession: MimiWebSocketSessionInternal&lt;[T](index.md)&gt;) : MimiWebSocketSession&lt;[T](index.md)&gt;

## Constructors

| | |
|---|---|
| [MimiAsrWebSocketSession](-mimi-asr-web-socket-session.md) | [common]<br>constructor(webSocketSession: MimiWebSocketSessionInternal&lt;[T](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [isActive](is-active.md) | [common]<br>open override val [isActive](is-active.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [rxFlow](index.md#827443351%2FProperties%2F115902251) | [common]<br>open override val [rxFlow](index.md#827443351%2FProperties%2F115902251): Flow&lt;[T](index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [cancel](index.md#-1815357071%2FFunctions%2F115902251) | [common]<br>open override fun [cancel](index.md#-1815357071%2FFunctions%2F115902251)() |
| [sendBinary](index.md#-71701954%2FFunctions%2F115902251) | [common]<br>open suspend override fun [sendBinary](index.md#-71701954%2FFunctions%2F115902251)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html)) |
| [sendBinaryBlocking](index.md#-1691580279%2FFunctions%2F115902251) | [common]<br>open override fun [sendBinaryBlocking](index.md#-1691580279%2FFunctions%2F115902251)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html)) |
| [stopRecognition](stop-recognition.md) | [common]<br>suspend fun [stopRecognition](stop-recognition.md)() |
