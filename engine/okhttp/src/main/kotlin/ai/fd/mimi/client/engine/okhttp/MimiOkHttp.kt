package ai.fd.mimi.client.engine.okhttp

import ai.fd.mimi.client.engine.MimiNetworkEngine
import okhttp3.OkHttpClient

/**
 * A network engine which uses [OkHttp](https://square.github.io/okhttp/) as the underlying implementation.
 */
@Suppress("FunctionName")
fun MimiNetworkEngine.Companion.OkHttp(okHttpClient: OkHttpClient): MimiNetworkEngine.Factory =
    MimiOkHttpNetworkEngine.Factory(okHttpClient)
