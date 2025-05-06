package ai.fd.mimi.client.engine.okhttp

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import okio.ByteString as OkioByteString

@ExtendWith(MockKExtension::class)
class MimiOkHttpWebSocketSessionTest {

    @MockK
    private lateinit var request: Request

    @MockK
    private lateinit var okHttpClient: OkHttpClient

    @MockK
    private lateinit var converter: MimiModelConverter.EncodableJsonString<Any>

    private lateinit var target: MimiOkHttpWebSocketSession<Any>

    @BeforeEach
    fun setUp() {
        target = MimiOkHttpWebSocketSession(request, okHttpClient, converter)
    }

    @Test
    fun testRxFlow() = runTest {
        val decodedData = mockk<Any>()
        every { converter.decode("jsonText") } returns decodedData
        val webSocketListenerSlot = slot<WebSocketListener>()
        val webSocket = mockk<WebSocket>(relaxUnitFun = true)
        every { okHttpClient.newWebSocket(eq(request), capture(webSocketListenerSlot)) } answers {
            webSocketListenerSlot.captured.onOpen(webSocket, mockk())
            webSocket
        }
        every { webSocket.close(-1, "reason") } answers {
            webSocketListenerSlot.captured.onClosed(webSocket, -1, "reason")
            true
        }
        target.connect()

        target.rxFlow.test {
            expectNoEvents()
            assertTrue(target.isActive)

            webSocketListenerSlot.captured.onMessage(webSocket, "jsonText")
            assertEquals(decodedData, awaitItem())
            expectNoEvents()
            assertTrue(target.isActive)

            webSocketListenerSlot.captured.onClosing(webSocket, -1, "reason")
            awaitComplete()
            assertFalse(target.isActive)
        }
    }

    @Test
    fun testConnectionFailure_onOpening() = runTest {
        val webSocketListenerSlot = slot<WebSocketListener>()
        val webSocket = mockk<WebSocket>(relaxUnitFun = true)
        every { okHttpClient.newWebSocket(eq(request), capture(webSocketListenerSlot)) } answers {
            webSocketListenerSlot.captured.onFailure(webSocket, RuntimeException(), mockk())
            webSocket
        }

        assertFailsWith<MimiIOException>("Failed to open WebSocket session") {
            target.connect()
        }

        coVerify { webSocket.cancel() }
    }

    @Test
    fun testConnectionFailure_afterOpen() = runTest {
        val decodedData = mockk<Any>()
        every { converter.decode("jsonText") } returns decodedData
        val webSocketListenerSlot = slot<WebSocketListener>()
        val webSocket = mockk<WebSocket>(relaxUnitFun = true)
        every { okHttpClient.newWebSocket(eq(request), capture(webSocketListenerSlot)) } answers {
            webSocketListenerSlot.captured.onOpen(webSocket, mockk())
            webSocket
        }
        target.connect()

        target.rxFlow.test {
            expectNoEvents()
            assertTrue(target.isActive)

            webSocketListenerSlot.captured.onFailure(webSocket, Exception(), mockk())
            val exception = awaitError()
            assertIs<MimiIOException>(exception)
            assertEquals("WebSocket session is closed unexceptionally", exception.message)
            assertFalse(target.isActive)
        }
    }

    @Test
    fun testSendBinary() = runTest {
        val webSocketListenerSlot = slot<WebSocketListener>()
        val webSocket = mockk<WebSocket>(relaxed = true)
        every { okHttpClient.newWebSocket(eq(request), capture(webSocketListenerSlot)) } answers {
            webSocketListenerSlot.captured.onOpen(webSocket, mockk())
            webSocket
        }
        target.connect()

        target.sendBinary(byteArrayOf(1, 2, 3))

        coVerify { webSocket.send(OkioByteString.of(1, 2, 3)) }
    }

    @Test
    fun testSendText() = runTest {
        val webSocketListenerSlot = slot<WebSocketListener>()
        val webSocket = mockk<WebSocket>(relaxed = true)
        every { okHttpClient.newWebSocket(eq(request), capture(webSocketListenerSlot)) } answers {
            webSocketListenerSlot.captured.onOpen(webSocket, mockk())
            webSocket
        }
        target.connect()

        target.sendText("text")

        coVerify { webSocket.send("text") }
    }

    @Test
    fun testCancel() = runTest {
        val webSocketListenerSlot = slot<WebSocketListener>()
        val webSocket = mockk<WebSocket>(relaxUnitFun = true)
        every { okHttpClient.newWebSocket(eq(request), capture(webSocketListenerSlot)) } answers {
            webSocketListenerSlot.captured.onOpen(webSocket, mockk())
            webSocket
        }
        target.connect()

        target.cancel()

        coVerify { webSocket.cancel() }
    }
}
