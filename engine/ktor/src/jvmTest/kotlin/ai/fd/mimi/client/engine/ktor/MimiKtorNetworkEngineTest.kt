package ai.fd.mimi.client.engine.ktor

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.io.bytestring.ByteString
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiKtorNetworkEngineTest {

    @Test
    fun testRequestAsStringInternal_Binary() = runTest {
        val mockEngine = MockEngine { request ->
            assertEquals("https://example.com:1234/path", request.url.toString())
            assertEquals("application/octet-stream", request.body.contentType.toString())
            assertEquals("header", request.headers["additional"])
            assertContentEquals(byteArrayOf(1, 2, 3), request.body.toByteArray())
            assertEquals(HttpMethod.Post, request.method)
            respond(
                content = ByteReadChannel("response"),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        val actual = target.requestAsStringInternal(
            path = "path",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )

        assertTrue(actual.isSuccess)
        assertEquals("response", actual.getOrThrow())
    }

    @Test
    fun testRequestAsStringInternal_Form() = runTest {
        val mockEngine = MockEngine { request ->
            assertEquals("http://example.com:1234/", request.url.toString())
            assertEquals("application/x-www-form-urlencoded; charset=UTF-8", request.body.contentType.toString())
            assertEquals("header", request.headers["additional"])
            assertContentEquals("key=value".toByteArray(Charsets.UTF_8), request.body.toByteArray())
            assertEquals(HttpMethod.Post, request.method)
            respond(
                content = ByteReadChannel("response"),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = false,
            host = "example.com",
            port = 1234
        )

        val actual = target.requestAsStringInternal(
            path = "",
            requestBody = MimiNetworkEngine.RequestBody.FormData(mapOf("key" to "value")),
            headers = mapOf("additional" to "header")
        )

        assertTrue(actual.isSuccess)
        assertEquals("response", actual.getOrThrow())
    }

    @Test
    fun `testRequestAsStringInternal throw handleError`() = runTest {
        val mockEngine = MockEngine {
            throw UnknownHostException("device is offline")
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        val actual = target.requestAsStringInternal(
            path = "path",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )

        assertTrue(actual.isFailure)
        val error = actual.exceptionOrNull() ?: throw AssertionError("Exception is null")
        assertIs<UnknownHostException>(error)
        assertEquals(error.message, "device is offline")
    }

    @Test
    fun `testRequestAsStringInternal throw CancellationException`() = runTest {
        val mockEngine = MockEngine {
            throw CancellationException("suspend cancelled")
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        assertThrows<CancellationException> {
            target.requestAsStringInternal(
                path = "path",
                requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
                headers = mapOf("additional" to "header")
            )
        }
    }

    @Test
    fun testRequestAsBinaryInternal() = runTest {
        val mockEngine = MockEngine { request ->
            assertEquals("https://example.com:1234/path", request.url.toString())
            assertEquals("application/octet-stream", request.body.contentType.toString())
            assertEquals("header", request.headers["additional"])
            assertContentEquals(byteArrayOf(1, 2, 3), request.body.toByteArray())
            assertEquals(HttpMethod.Post, request.method)
            respond(
                content = ByteReadChannel(byteArrayOf(4, 5, 6)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/octet-stream")
            )
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        val actual = target.requestAsBinaryInternal(
            path = "path",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )

        assertTrue(actual.isSuccess)
        assertEquals(ByteString(4, 5, 6), actual.getOrThrow())
    }

    @Test
    fun `testRequestAsBinaryInternal throw handleError`() = runTest {
        val mockEngine = MockEngine {
            throw UnknownHostException("device is offline")
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        val actual = target.requestAsBinaryInternal(
            path = "path",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )

        assertTrue(actual.isFailure)
        val error = actual.exceptionOrNull() ?: throw AssertionError("Exception is null")
        assertIs<UnknownHostException>(error)
        assertEquals(error.message, "device is offline")
    }

    @Test
    fun `testRequestAsBinaryInternal throw CancellationException`() = runTest {
        val mockEngine = MockEngine {
            throw CancellationException("suspend cancelled")
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        assertThrows<CancellationException> {
            target.requestAsBinaryInternal(
                path = "path",
                requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
                headers = mapOf("additional" to "header")
            )
        }
    }

    @Test
    fun testRequest_MultiSegmentPath() = runTest {
        val mockEngine = MockEngine { request ->
            assertEquals("https://example.com:1234/path/with/multiple/segments", request.url.toString())
            assertEquals("application/octet-stream", request.body.contentType.toString())
            assertEquals("header", request.headers["additional"])
            assertContentEquals(byteArrayOf(1, 2, 3), request.body.toByteArray())
            assertEquals(HttpMethod.Post, request.method)
            respond(
                content = ByteReadChannel(byteArrayOf(4, 5, 6)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/octet-stream")
            )
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        val actual = target.requestAsBinaryInternal(
            path = "path/with/multiple/segments",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )

        assertTrue(actual.isSuccess)
        assertEquals(ByteString(4, 5, 6), actual.getOrThrow())
    }

    @Test
    fun testRequest_Error() = runTest {
        val mockEngine = MockEngine { request ->
            assertEquals("https://example.com:1234/path", request.url.toString())
            assertEquals("application/octet-stream", request.body.contentType.toString())
            assertEquals("header", request.headers["additional"])
            assertContentEquals(byteArrayOf(1, 2, 3), request.body.toByteArray())
            assertEquals(HttpMethod.Post, request.method)
            respond(
                content = ByteReadChannel("error"),
                status = HttpStatusCode.BadRequest,
            )
        }
        val target = MimiKtorNetworkEngine(
            httpClient = HttpClient(mockEngine),
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        val actual = target.requestAsStringInternal(
            path = "path",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )

        assertTrue(actual.isFailure)
        val exception = actual.exceptionOrNull()
        assertIs<MimiIOException>(exception)
        assertEquals("Request failed with status: 400 Bad Request. Body: error", exception.message)
    }

    @Test
    fun testOpenWebSocketSession() = runTest {
        // Ktor doesn't support WebSocket mocking, so use okhttp's MockWebServer instead.
        // https://youtrack.jetbrains.com/issue/KTOR-537
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(object : WebSocketListener() {}))

        val target = spyk(
            MimiKtorNetworkEngine(
                httpClient = HttpClient(OkHttp) {
                    install(WebSockets)
                },
                useSsl = false,
                host = mockWebServer.hostName,
                port = mockWebServer.port
            )
        )
        val converter = mockk<MimiModelConverter.EncodableJsonString<Any>>()
        val session = mockk<MimiWebSocketSessionInternal<Any>>()
        every { target.createWebSocketSession(any(), eq(converter)) } returns session

        val actual = target.openWebSocketSessionInternal(
            path = "path",
            contentType = "application/json",
            headers = mapOf("additional" to "header"),
            converter = converter
        )

        assertEquals(session, actual)

        mockWebServer.close()
    }

    @Test
    fun testOpenWebSocketSession_Error() = runTest {
        mockkStatic("io.ktor.client.plugins.websocket.BuildersKt")
        val httpClient = mockk<HttpClient>()
        coEvery { httpClient.webSocketSession(any<HttpRequestBuilder.() -> Unit>()) } throws Exception()
        val target = MimiKtorNetworkEngine(
            httpClient = httpClient,
            useSsl = true,
            host = "example.com",
            port = 1234
        )

        assertFailsWith(MimiIOException::class, "Failed to open WebSocket session") {
            target.openWebSocketSessionInternal(
                path = "path",
                contentType = "application/json",
                headers = mapOf("additional" to "header"),
                converter = mockk<MimiModelConverter.EncodableJsonString<Any>>()
            )
        }
    }
}
