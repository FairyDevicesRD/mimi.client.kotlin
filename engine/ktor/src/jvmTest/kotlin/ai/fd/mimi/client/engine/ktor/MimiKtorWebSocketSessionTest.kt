package ai.fd.mimi.client.engine.ktor

import ai.fd.mimi.client.engine.MimiModelConverter
import app.cash.turbine.test
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiKtorWebSocketSessionTest {

    @MockK
    private lateinit var ktorWebSocketSession: DefaultClientWebSocketSession

    @MockK
    private lateinit var converter: MimiModelConverter.EncodableJsonString<Any>

    private val incomingChannel: Channel<Frame> = Channel()

    private lateinit var target: MimiKtorWebSocketSession<Any>

    @BeforeEach
    fun setUp() {
        every { ktorWebSocketSession.incoming } returns incomingChannel
        target = MimiKtorWebSocketSession(ktorWebSocketSession, converter)
    }

    @Test
    fun testRxFlow() = runTest {
        val decodedData = mockk<Any>()
        every { converter.decode("jsonText") } returns decodedData

        target.rxFlow.test {
            expectNoEvents()
            assertTrue(target.isActive)

            incomingChannel.send(Frame.Text(true, "jsonText".encodeToByteArray()))
            assertEquals(decodedData, awaitItem())
            expectNoEvents()
            assertTrue(target.isActive)

            incomingChannel.close()
            awaitComplete()
            assertFalse(target.isActive)
        }
    }

    @Test
    fun testSendBinary() = runTest {
        val frameSlot = slot<Frame.Binary>()
        coEvery { ktorWebSocketSession.send(capture(frameSlot)) } just runs

        target.sendBinary(byteArrayOf(1, 2, 3))

        coVerify { ktorWebSocketSession.send(any()) }
        with(frameSlot.captured) {
            assertEquals(true, fin)
            assertContentEquals(byteArrayOf(1, 2, 3), data)
        }
    }

    @Test
    fun testSendText() = runTest {
        val frameSlot = slot<Frame.Text>()
        coEvery { ktorWebSocketSession.send(capture(frameSlot)) } just runs

        target.sendText("text")

        coVerify { ktorWebSocketSession.send(any()) }
        with(frameSlot.captured) {
            assertEquals(true, fin)
            assertContentEquals("text".encodeToByteArray(), data)
        }
    }

    @Test
    fun testCancel() = runTest {
        mockkStatic("kotlinx.coroutines.CoroutineScopeKt")
        coEvery { ktorWebSocketSession.cancel() } just runs

        target.cancel()

        coVerify { ktorWebSocketSession.cancel() }
    }
}
