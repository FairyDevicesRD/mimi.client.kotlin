package ai.fd.mimi.client.engine.ktor

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import androidx.annotation.VisibleForTesting
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.buildUrl
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.utils.io.ByteReadChannel
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.io.bytestring.ByteString

class MimiKtorNetworkEngine(
    private val httpClient: HttpClient,
    useSsl: Boolean,
    host: String,
    port: Int,
    path: String
) : MimiNetworkEngine() {

    private val httpTargetUrl: Url = buildUrl {
        this.protocol = if (useSsl) URLProtocol.HTTPS else URLProtocol.HTTP
        this.host = host
        this.port = port
        this.path(path)
    }

    private val webSocketTargetUrl: Url = buildUrl {
        this.protocol = if (useSsl) URLProtocol.WSS else URLProtocol.WS
        this.host = host
        this.port = port
        this.path(path)
    }

    override suspend fun requestAsStringInternal(
        requestBody: RequestBody,
        headers: Map<String, String>
    ): Result<String> = requestInternal(requestBody, headers) { it.bodyAsText() }

    override suspend fun requestAsBinaryInternal(
        requestBody: RequestBody,
        headers: Map<String, String>
    ): Result<ByteString> = requestInternal(requestBody, headers) { ByteString(it.bodyAsBytes()) }

    private suspend fun <T> requestInternal(
        requestBody: RequestBody,
        headers: Map<String, String>,
        extractResponseBodyAction: suspend (HttpResponse) -> T
    ): Result<T> {
        val response = httpClient.post(httpTargetUrl) {
            headers {
                headers.forEach { (key, value) -> append(key, value) }
            }
            setBodyAndContentType(requestBody)
        }
        if (!response.status.isSuccess()) {
            return Result.failure(
                MimiIOException("Request failed with status: ${response.status}. Body: ${response.bodyAsText()}")
            )
        }
        return Result.success(extractResponseBodyAction(response))
    }

    @Throws(MimiIOException::class, CancellationException::class)
    override suspend fun <T> openWebSocketSessionInternal(
        contentType: String,
        headers: Map<String, String>,
        converter: MimiModelConverter.JsonString<T>
    ): MimiWebSocketSessionInternal<T> {
        val session = try {
            httpClient.webSocketSession {
                url.takeFrom(webSocketTargetUrl)
                headers {
                    headers.forEach { (key, value) -> append(key, value) }
                }
                contentType(ContentType.parse(contentType))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            throw MimiIOException("Failed to open WebSocket session", e)
        }
        return createWebSocketSession(session, converter)
    }

    @VisibleForTesting
    internal fun <T> createWebSocketSession(
        session: DefaultClientWebSocketSession,
        converter: MimiModelConverter.JsonString<T>
    ): MimiWebSocketSessionInternal<T> = MimiKtorWebSocketSession(session, converter)

    private fun HttpRequestBuilder.setBodyAndContentType(requestBody: RequestBody): Unit = when (requestBody) {
        is RequestBody.Binary -> {
            contentType(ContentType.parse(requestBody.contentType))
            setBody(ByteReadChannel(requestBody.data.toByteArray()))
        }

        is RequestBody.FormData -> {
            contentType(ContentType.Application.FormUrlEncoded)
            val parameters = parameters { requestBody.fields.forEach { (key, value) -> append(key, value) } }
            setBody(FormDataContent(parameters))
        }
    }

    internal class Factory(private val httpClient: HttpClient) : MimiNetworkEngine.Factory {
        override fun create(useSsl: Boolean, host: String, port: Int, path: String): MimiNetworkEngine =
            MimiKtorNetworkEngine(httpClient, useSsl, host, port, path)
    }
}
