package ai.fd.mimi.client.engine.okhttp

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.io.bytestring.ByteString
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import okio.ByteString as OkioByteString

class MimiOkHttpNetworkEngineTest {

    private lateinit var mockWebServer: MockWebServer

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testRequestAsStringInternal_Binary() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("response")
                .addHeader("Content-Type", "application/json")
        )
        val target = MimiOkHttpNetworkEngine(
            okHttpClient = OkHttpClient(),
            useSsl = false,
            host = mockWebServer.hostName,
            port = mockWebServer.port,
            path = "path"
        )

        val actual = target.requestAsStringInternal(
            accessToken = "accessToken",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )
        val actualRequest = mockWebServer.takeRequest()

        assertTrue(actual.isSuccess)
        assertEquals("response", actual.getOrThrow())
        assertEquals("POST", actualRequest.method)
        assertEquals("/path", actualRequest.path)
        assertEquals("Bearer accessToken", actualRequest.getHeader("Authorization"))
        assertEquals("application/octet-stream", actualRequest.getHeader("Content-Type"))
        assertEquals("header", actualRequest.getHeader("additional"))
        assertContentEquals(byteArrayOf(1, 2, 3), actualRequest.body.readByteArray())
    }

    @Test
    fun testRequestAsStringInternal_Form() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("response")
                .addHeader("Content-Type", "application/json")
        )
        val target = MimiOkHttpNetworkEngine(
            okHttpClient = OkHttpClient(),
            useSsl = false,
            host = mockWebServer.hostName,
            port = mockWebServer.port,
            path = ""
        )

        val actual = target.requestAsStringInternal(
            accessToken = "accessToken",
            requestBody = MimiNetworkEngine.RequestBody.FormData(mapOf("key" to "value")),
            headers = mapOf("additional" to "header")
        )
        val actualRequest = mockWebServer.takeRequest()

        assertTrue(actual.isSuccess)
        assertEquals("response", actual.getOrThrow())
        assertEquals("POST", actualRequest.method)
        assertEquals("/", actualRequest.path)
        assertEquals("Bearer accessToken", actualRequest.getHeader("Authorization"))
        assertEquals("application/x-www-form-urlencoded", actualRequest.getHeader("Content-Type"))
        assertEquals("header", actualRequest.getHeader("additional"))
        assertContentEquals("key=value".toByteArray(Charsets.UTF_8), actualRequest.body.readByteArray())
    }

    @Test
    fun testRequestAsBinaryInternal() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Buffer().write(byteArrayOf(4, 5, 6)))
                .addHeader("Content-Type", "application/octet-stream")
        )
        val target = MimiOkHttpNetworkEngine(
            okHttpClient = OkHttpClient(),
            useSsl = false,
            host = mockWebServer.hostName,
            port = mockWebServer.port,
            path = ""
        )

        val actual = target.requestAsBinaryInternal(
            accessToken = "accessToken",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )
        val actualRequest = mockWebServer.takeRequest()

        assertTrue(actual.isSuccess)
        assertEquals(ByteString(4, 5, 6), actual.getOrThrow())
        assertEquals("POST", actualRequest.method)
        assertEquals("/", actualRequest.path)
        assertEquals("Bearer accessToken", actualRequest.getHeader("Authorization"))
        assertEquals("application/octet-stream", actualRequest.getHeader("Content-Type"))
        assertEquals("header", actualRequest.getHeader("additional"))
        assertEquals(OkioByteString.of(1, 2, 3), actualRequest.body.readByteString())
    }

    @Test
    fun testRequest_Error() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(Buffer().write("error".toByteArray()))
        )
        val target = MimiOkHttpNetworkEngine(
            okHttpClient = OkHttpClient(),
            useSsl = false,
            host = mockWebServer.hostName,
            port = mockWebServer.port,
            path = ""
        )

        val actual = target.requestAsBinaryInternal(
            accessToken = "accessToken",
            requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "application/octet-stream"),
            headers = mapOf("additional" to "header")
        )
        val actualRequest = mockWebServer.takeRequest()

        assertTrue(actual.isFailure)
        val exception = actual.exceptionOrNull()
        assertIs<MimiIOException>(exception)
        assertEquals("Request failed with status: 400. Body: error", exception.message)
        assertEquals("POST", actualRequest.method)
        assertEquals("/", actualRequest.path)
        assertEquals("Bearer accessToken", actualRequest.getHeader("Authorization"))
        assertEquals("application/octet-stream", actualRequest.getHeader("Content-Type"))
        assertEquals("header", actualRequest.getHeader("additional"))
        assertEquals(OkioByteString.of(1, 2, 3), actualRequest.body.readByteString())
    }

    @Test
    fun testOpenWebSocketSession() = runTest {
        val okHttpClient = mockk<OkHttpClient>()
        val target = spyk(
            MimiOkHttpNetworkEngine(
                okHttpClient = okHttpClient,
                useSsl = true,
                host = "example.com",
                port = 1234,
                path = "path"
            )
        )
        val converter = mockk<MimiModelConverter.JsonString<Any>>()
        val session = mockk<MimiOkHttpWebSocketSession<Any>>(relaxUnitFun = true)
        val requestSlot = slot<Request>()
        every { target.createWebSocketSession(capture(requestSlot), eq(okHttpClient), eq(converter)) } returns session

        val actual = target.openWebSocketSession(
            accessToken = "accessToken",
            contentType = "application/json",
            headers = mapOf("additional" to "header"),
            converter = converter
        )

        assertEquals(session, actual)
        with(requestSlot.captured) {
            assertEquals("https://example.com:1234/path", url.toString())
            assertEquals("Bearer accessToken", header("Authorization"))
            assertEquals("application/json", header("Content-Type"))
            assertEquals("header", header("additional"))
        }
        coVerify { session.connect() }
    }
}
