//[token](../../../index.md)/[ai.fd.mimi.client.service.token](../index.md)/[MimiTokenReadWriteScope](index.md)

# MimiTokenReadWriteScope

[common]\
class [MimiTokenReadWriteScope](index.md) : [MimiTokenGroupScope](../-mimi-token-group-scope/index.md)

A group of scopes for reading and writing to the target service.

[Read](-read.md) or [Write](-write.md) is used to represent the scope of read and write permissions, respectively. Specifying this scope itself will represent both read and write scopes together.

## Properties

| Name | Summary |
|---|---|
| [Read](-read.md) | [common]<br>val [Read](-read.md): [MimiTokenScope](../-mimi-token-scope/index.md) |
| [Write](-write.md) | [common]<br>val [Write](-write.md): [MimiTokenScope](../-mimi-token-scope/index.md) |

## Functions

| Name | Summary |
|---|---|
| [plus](../plus.md) | [common]<br>operator fun [MimiTokenScope](../-mimi-token-scope/index.md).[plus](../plus.md)(other: [MimiTokenScope](../-mimi-token-scope/index.md)): [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[MimiTokenScope](../-mimi-token-scope/index.md)&gt; |
