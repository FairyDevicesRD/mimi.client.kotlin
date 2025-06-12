//[core](../../../index.md)/[ai.fd.mimi.client.engine](../index.md)/[MimiWebSocketSession](index.md)/[sendBinary](send-binary.md)

# sendBinary

[common]\
abstract suspend fun [sendBinary](send-binary.md)(binaryData: [ByteArray](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-byte-array/index.html))

Sends voice binary data to the server side. If there is room in the buffer, this function is executed immediately. If there is no buffer space, the function suspends until one is available.
