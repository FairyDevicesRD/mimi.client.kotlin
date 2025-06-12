//[core](../../../index.md)/[ai.fd.mimi.client.engine](../index.md)/[MimiWebSocketSession](index.md)/[isActive](is-active.md)

# isActive

[common]\
abstract val [isActive](is-active.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

Whether the WebSocket connection has been successfully created and can communicate to the server. This becomes `false` when the connection is disconnected or graceful shutdown is initiated as defined by each service.
