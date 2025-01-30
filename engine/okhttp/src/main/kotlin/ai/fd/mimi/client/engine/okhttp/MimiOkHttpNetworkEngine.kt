package ai.fd.mimi.client.engine.okhttp

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okio.IOException

internal class MimiOkHttpNetworkEngine(
    private val okHttpClient: OkHttpClient,
    useSsl: Boolean,
    host: String,
    port: Int
) : MimiNetworkEngine() {

    private val httpUrl: HttpUrl = HttpUrl.Builder()
        .scheme(if (useSsl) "https" else "http")
        .host(host)
        .port(port)
        .build()

    override suspend fun requestInternal(
        accessToken: String,
        byteArray: ByteArray,
        contentType: String,
        headers: Map<String, String>
    ): Result<String> {
        val request = Request.Builder()
            .url(httpUrl)
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeaders(headers)
            .post(byteArray.toRequestBody(contentType = contentType.toMediaType()))
            .build()

        val response = okHttpClient.newCall(request).executeAsync()
        if (!response.isSuccessful) {
            return Result.failure(
                MimiIOException("Request failed with status: ${response.code}. Body: ${response.body?.string()}")
            )
        }
        val text = response.body?.string() ?: return Result.failure(MimiIOException("Response body is null"))
        return Result.success(text)
    }

    @Throws(MimiIOException::class, CancellationException::class)
    override suspend fun <R> openWebSocketSession(
        accessToken: String,
        contentType: String,
        headers: Map<String, String>,
        converter: MimiModelConverter<R>
    ): MimiWebSocketSessionInternal<R> {
        val request = Request.Builder()
            .url(httpUrl) // Will be upgraded to ws scheme after connection established.
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeader("ContentType", contentType)
            .addHeaders(headers)
            .build()

        val session = MimiOkHttpWebSocketSession(request, okHttpClient, converter)
        session.connect()
        return session
    }

    private suspend fun Call.executeAsync(): Response =
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                cancel()
            }
            enqueue(
                object : Callback {
                    override fun onFailure(
                        call: Call,
                        e: IOException,
                    ) {
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(
                        call: Call,
                        response: Response,
                    ) {
                        continuation.resume(response) { _, _, _ ->
                            response.closeQuietly()
                        }
                    }
                }
            )
        }

    private fun Request.Builder.addHeaders(headers: Map<String, String>): Request.Builder = apply {
        headers.forEach { (key, value) ->
            addHeader(key, value)
        }
    }

    internal class Factory(private val okHttpClient: OkHttpClient) : MimiNetworkEngine.Factory {
        override fun create(useSsl: Boolean, host: String, port: Int): MimiNetworkEngine =
            MimiOkHttpNetworkEngine(okHttpClient, useSsl, host, port)
    }
}
