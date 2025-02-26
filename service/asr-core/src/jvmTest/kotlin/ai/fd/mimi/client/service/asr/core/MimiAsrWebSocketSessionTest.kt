package ai.fd.mimi.client.service.asr.core

import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiAsrWebSocketSessionTest {

    @MockK
    private lateinit var internalSession: MimiWebSocketSessionInternal<Any>

    private lateinit var target: MimiAsrWebSocketSession<Any>

    @BeforeEach
    fun setUp() {
        target = MimiAsrWebSocketSession(internalSession)
    }

    @Test
    fun testStopRecognition() = runTest {
        every { internalSession.isActive } returns true
        coEvery { internalSession.sendJsonText(any<Any>(), any()) } returns Unit

        assertTrue(target.isActive)

        target.stopRecognition()

        assertFalse(target.isActive)
        coVerify { internalSession.sendJsonText(any<Any>(), any()) }
    }

    @Test
    fun testIsActive_upstreamClosed() = runTest {
        every { internalSession.isActive } returns false

        assertFalse(target.isActive)
    }
}
