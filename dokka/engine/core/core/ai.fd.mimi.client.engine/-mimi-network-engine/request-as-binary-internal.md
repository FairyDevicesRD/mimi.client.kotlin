//[core](../../../index.md)/[ai.fd.mimi.client.engine](../index.md)/[MimiNetworkEngine](index.md)/[requestAsBinaryInternal](request-as-binary-internal.md)

# requestAsBinaryInternal

[common]\

@VisibleForTesting(otherwise = 4)

abstract suspend fun [requestAsBinaryInternal](request-as-binary-internal.md)(path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), requestBody: [MimiNetworkEngine.RequestBody](-request-body/index.md), headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyMap()): [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/index.html)&lt;ByteString&gt;
