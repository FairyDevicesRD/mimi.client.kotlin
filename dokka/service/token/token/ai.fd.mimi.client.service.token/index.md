//[token](../../index.md)/[ai.fd.mimi.client.service.token](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [MimiIssueTokenResult](-mimi-issue-token-result/index.md) | [common]<br>data class [MimiIssueTokenResult](-mimi-issue-token-result/index.md)(val accessToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val expiresIn: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), val startTimestamp: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), val endTimestamp: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html))<br>A result of the issued access token. |
| [MimiTokenApiGroupScope](-mimi-token-api-group-scope/index.md) | [common]<br>class [MimiTokenApiGroupScope](-mimi-token-api-group-scope/index.md)&lt;[T](-mimi-token-api-group-scope/index.md) : [MimiTokenApiScope](-mimi-token-api-scope/index.md)&gt;(val Api: [T](-mimi-token-api-group-scope/index.md)) : [MimiTokenGroupScope](-mimi-token-group-scope/index.md)<br>A group of scopes for using Mimi API services. |
| [MimiTokenApiScope](-mimi-token-api-scope/index.md) | [common]<br>sealed class [MimiTokenApiScope](-mimi-token-api-scope/index.md) : [MimiTokenGroupScope](-mimi-token-group-scope/index.md)<br>An implementation of [MimiTokenScope](-mimi-token-scope/index.md) which represents a group of scopes for using the Mimi API service. |
| [MimiTokenGroupScope](-mimi-token-group-scope/index.md) | [common]<br>abstract class [MimiTokenGroupScope](-mimi-token-group-scope/index.md) : [MimiTokenScope](-mimi-token-scope/index.md)<br>An implementation of [MimiTokenScope](-mimi-token-scope/index.md) which represents multiple scopes. |
| [MimiTokenHttpApiScope](-mimi-token-http-api-scope/index.md) | [common]<br>class [MimiTokenHttpApiScope](-mimi-token-http-api-scope/index.md) : [MimiTokenApiScope](-mimi-token-api-scope/index.md)<br>An implementation of [MimiTokenApiScope](-mimi-token-api-scope/index.md) which represents a scope for using the HTTP service API. |
| [MimiTokenHttpWebSocketApiScope](-mimi-token-http-web-socket-api-scope/index.md) | [common]<br>class [MimiTokenHttpWebSocketApiScope](-mimi-token-http-web-socket-api-scope/index.md) : [MimiTokenApiScope](-mimi-token-api-scope/index.md)<br>An implementation of [MimiTokenApiScope](-mimi-token-api-scope/index.md) which represents a scope for using the HTTP and WebSocket service APIs. |
| [MimiTokenReadWriteScope](-mimi-token-read-write-scope/index.md) | [common]<br>class [MimiTokenReadWriteScope](-mimi-token-read-write-scope/index.md) : [MimiTokenGroupScope](-mimi-token-group-scope/index.md)<br>A group of scopes for reading and writing to the target service. |
| [MimiTokenScope](-mimi-token-scope/index.md) | [common]<br>sealed interface [MimiTokenScope](-mimi-token-scope/index.md)<br>An interface representing a scope for the Mimi API. |
| [MimiTokenScopes](-mimi-token-scopes/index.md) | [common]<br>object [MimiTokenScopes](-mimi-token-scopes/index.md)<br>A collection of scopes for the Mimi API. |
| [MimiTokenService](-mimi-token-service/index.md) | [common]<br>class [MimiTokenService](-mimi-token-service/index.md)<br>A service class to issue access tokens for Mimi services. |
| [MimiValidateTokenResult](-mimi-validate-token-result/index.md) | [common]<br>data class [MimiValidateTokenResult](-mimi-validate-token-result/index.md)(val tokenStatus: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))<br>A result of the validating access token. |

## Functions

| Name | Summary |
|---|---|
| [plus](plus.md) | [common]<br>operator fun [MimiTokenScope](-mimi-token-scope/index.md).[plus](plus.md)(other: [MimiTokenScope](-mimi-token-scope/index.md)): [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[MimiTokenScope](-mimi-token-scope/index.md)&gt; |
