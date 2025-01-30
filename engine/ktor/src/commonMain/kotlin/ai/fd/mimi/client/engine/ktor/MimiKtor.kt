package ai.fd.mimi.client.engine.ktor

import ai.fd.mimi.client.engine.MimiNetworkEngine
import io.ktor.client.HttpClient

/**
 * A network engine which uses [Ktor Client](https://ktor.io/docs/client.html) as the underlying implementation.
 */
@Suppress("FunctionName")
fun MimiNetworkEngine.Companion.Ktor(httpClient: HttpClient): MimiNetworkEngine.Factory =
    MimiKtorNetworkEngine.Factory(httpClient)
