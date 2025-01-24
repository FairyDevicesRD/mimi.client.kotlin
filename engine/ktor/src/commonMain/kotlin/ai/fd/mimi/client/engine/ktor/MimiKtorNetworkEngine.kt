package ai.fd.mimi.client.engine.ktor

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.buildUrl
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom
import io.ktor.utils.io.ByteReadChannel
import kotlin.coroutines.cancellation.CancellationException

class MimiKtorNetworkEngine(
    private val httpClient: HttpClient,
    useSsl: Boolean,
    host: String,
    port: Int
) : MimiNetworkEngine() {

    private val httpTargetUrl: Url = buildUrl {
        this.protocol = if (useSsl) URLProtocol.HTTPS else URLProtocol.HTTP
        this.host = host
        this.port = port
    }

    private val webSocketTargetUrl: Url = buildUrl {
        this.protocol = if (useSsl) URLProtocol.WSS else URLProtocol.WS
        this.host = host
        this.port = port
    }

    override suspend fun requestInternal(
        accessToken: String,
        requestBody: RequestBody,
        headers: Map<String, String>
    ): Result<String> {
        val response = httpClient.post(httpTargetUrl) {
            headers {
                append("Authorization", "Bearer $accessToken")
                headers.forEach { (key, value) -> append(key, value) }
            }
            setBodyAndContentType(requestBody)
        }
        if (!response.status.isSuccess()) {
            return Result.failure(
                MimiIOException("Request failed with status: ${response.status}. Body: ${response.bodyAsText()}")
            )
        }
        return Result.success(response.bodyAsText())
    }

    @Throws(MimiIOException::class, CancellationException::class)
    override suspend fun <T> openWebSocketSession(
        accessToken: String,
        contentType: String,
        headers: Map<String, String>,
        converter: MimiModelConverter<T>
    ): MimiWebSocketSessionInternal<T> {
        val session = try {
            httpClient.webSocketSession {
                url.takeFrom(webSocketTargetUrl)
                headers {
                    append("Authorization", "Bearer $accessToken")
                    headers.forEach { (key, value) -> append(key, value) }
                }
                contentType(ContentType.parse(contentType))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            throw MimiIOException("Failed to open WebSocket session", e)
        }
        return MimiKtorWebSocketSession(session, converter)
    }

    private fun HttpRequestBuilder.setBodyAndContentType(requestBody: MimiNetworkEngine.RequestBody) {
        when (requestBody) {
            is MimiNetworkEngine.RequestBody.Binary -> {
                contentType(ContentType.parse(requestBody.contentType))
                setBody(ByteReadChannel(requestBody.byteArray))
            }
        }
    }

    internal class Factory(private val httpClient: HttpClient) : MimiNetworkEngine.Factory {
        override fun create(useSsl: Boolean, host: String, port: Int): MimiNetworkEngine =
            MimiKtorNetworkEngine(httpClient, useSsl, host, port)
    }
}
