package ai.fd.mimi.client.engine.okhttp

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.asReadOnlyByteBuffer
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.IOException
import org.jetbrains.annotations.VisibleForTesting
import okhttp3.RequestBody as OkHttpRequestBody

internal class MimiOkHttpNetworkEngine(
    private val okHttpClient: OkHttpClient,
    useSsl: Boolean,
    host: String,
    port: Int
) : MimiNetworkEngine() {

    private val baseUrl: HttpUrl = HttpUrl.Builder()
        .scheme(if (useSsl) "https" else "http")
        .host(host)
        .port(port)
        .build()

    override suspend fun requestAsStringInternal(
        path: String,
        requestBody: RequestBody,
        headers: Map<String, String>
    ): Result<String> = requestInternal(path, requestBody, headers) { it.body?.string() }

    override suspend fun requestAsBinaryInternal(
        path: String,
        requestBody: RequestBody,
        headers: Map<String, String>
    ): Result<ByteString> =
        requestInternal(path, requestBody, headers) { ByteString(it.body?.bytes() ?: byteArrayOf()) }

    private suspend fun <T> requestInternal(
        path: String,
        requestBody: RequestBody,
        headers: Map<String, String>,
        extractResponseBodyAction: suspend (Response) -> T?
    ): Result<T> {
        try {
            val url = baseUrl.newBuilder().addPathSegments(path).build()
            val request = Request.Builder()
                .url(url)
                .addHeaders(headers)
                .post(requestBody.toOkHttpRequestBody())
                .build()

            val response = okHttpClient.newCall(request).executeAsync()
            if (!response.isSuccessful) {
                return Result.failure(
                    MimiIOException("Request failed with status: ${response.code}. Body: ${response.body?.string()}")
                )
            }
            val data =
                extractResponseBodyAction(response) ?: return Result.failure(MimiIOException("Response body is null"))
            return Result.success(data)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    @Throws(MimiIOException::class, CancellationException::class)
    override suspend fun <R> openWebSocketSessionInternal(
        path: String,
        contentType: String,
        headers: Map<String, String>,
        converter: MimiModelConverter.EncodableJsonString<R>
    ): MimiWebSocketSessionInternal<R> {
        val url = baseUrl.newBuilder().addPathSegment(path).build()
        val request = Request.Builder()
            .url(url) // Will be upgraded to ws scheme after connection established.
            .addHeader("Content-Type", contentType)
            .addHeaders(headers)
            .build()

        val session = createWebSocketSession(request, okHttpClient, converter)
        session.connect()
        return session
    }

    @VisibleForTesting
    internal fun <T> createWebSocketSession(
        request: Request,
        okHttpClient: OkHttpClient,
        converter: MimiModelConverter.EncodableJsonString<T>
    ): MimiOkHttpWebSocketSession<T> = MimiOkHttpWebSocketSession(request, okHttpClient, converter)

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

    private fun RequestBody.toOkHttpRequestBody(): OkHttpRequestBody = when (this) {
        is RequestBody.Binary -> data.toRequestBody(contentType.toMediaType())
        is RequestBody.FormData ->
            fields.entries.fold(FormBody.Builder()) { builder, (key, value) -> builder.add(key, value) }.build()
    }

    private fun ByteString.toRequestBody(contentType: MediaType?): OkHttpRequestBody =
        object : OkHttpRequestBody() {
            override fun contentType(): MediaType? = contentType
            override fun contentLength(): Long = size.toLong()
            override fun writeTo(sink: BufferedSink) {
                sink.write(this@toRequestBody.asReadOnlyByteBuffer())
            }
        }

    internal class Factory(private val okHttpClient: OkHttpClient) : MimiNetworkEngine.Factory {
        override fun create(useSsl: Boolean, host: String, port: Int): MimiNetworkEngine =
            MimiOkHttpNetworkEngine(okHttpClient, useSsl, host, port)
    }
}
