//[ktor](../../index.md)/[ai.fd.mimi.client.engine.ktor](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [MimiKtorNetworkEngine](-mimi-ktor-network-engine/index.md) | [common]<br>class [MimiKtorNetworkEngine](-mimi-ktor-network-engine/index.md)(httpClient: HttpClient, useSsl: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), host: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), port: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)) : [MimiNetworkEngine](../../../core/core/ai.fd.mimi.client.engine/-mimi-network-engine/index.md) |
| [MimiKtorWebSocketSession](-mimi-ktor-web-socket-session/index.md) | [common]<br>class [MimiKtorWebSocketSession](-mimi-ktor-web-socket-session/index.md)&lt;[T](-mimi-ktor-web-socket-session/index.md)&gt;(ktorWebSocketSession: DefaultClientWebSocketSession, converter: [MimiModelConverter.EncodableJsonString](../../../core/core/ai.fd.mimi.client.engine/-mimi-model-converter/-encodable-json-string/index.md)&lt;[T](-mimi-ktor-web-socket-session/index.md)&gt;) : [MimiWebSocketSessionInternal](../../../core/core/ai.fd.mimi.client.engine/-mimi-web-socket-session-internal/index.md)&lt;[T](-mimi-ktor-web-socket-session/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [Ktor](-ktor.md) | [common]<br>fun [MimiNetworkEngine.Companion](../../../core/core/ai.fd.mimi.client.engine/-mimi-network-engine/-companion/index.md).[Ktor](-ktor.md)(httpClient: HttpClient): [MimiNetworkEngine.Factory](../../../core/core/ai.fd.mimi.client.engine/-mimi-network-engine/-factory/index.md)<br>A network engine which uses [Ktor Client](https://ktor.io/docs/client.html) as the underlying implementation. |
