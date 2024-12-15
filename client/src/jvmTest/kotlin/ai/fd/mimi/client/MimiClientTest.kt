package ai.fd.mimi.client

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@MockKExtension.CheckUnnecessaryStub
class MimiClientTest {

    @MockK
    private lateinit var engineFactory: MimiNetworkEngine.Factory

    @MockK
    private lateinit var engine: MimiNetworkEngine

    private lateinit var target: MimiClient

    @BeforeEach
    fun setup() {
        every { engineFactory.create(any(), any(), any()) } returns engine
        target = MimiClient(engineFactory, useSsl = false, host = "localhost", port = 8080)
    }

    @Test
    fun testCreateEngine() {
        verify {
            engineFactory.create(useSsl = false, host = "localhost", port = 8080)
        }
    }

    @Test
    fun testRequest() = runTest {
        val response = Result.success("response")
        coEvery {
            engine.request<String>(any(), any(), any(), any(), any())
        } returns response
        val converter = mockk<MimiModelConverter<String>>()

        val actual = target.request(
            accessToken = "accessToken",
            byteArray = byteArrayOf(0x1, 0x2, 0x3),
            contentType = "application/json",
            headers = mapOf("key" to "value"),
            converter = converter
        )

        assertEquals(response, actual)
        coVerify {
            engine.request(
                accessToken = "accessToken",
                byteArray = byteArrayOf(0x1, 0x2, 0x3),
                contentType = "application/json",
                headers = mapOf("key" to "value"),
                converter = converter
            )
        }
    }

    @Test
    fun testOpenWebSocketSession() = runTest {
        val session = mockk<MimiWebSocketSessionInternal<String>>()
        coEvery {
            engine.openWebSocketSession<String>(any(), any(), any(), any())
        } returns session
        val converter = mockk<MimiModelConverter<String>>()

        val actual = target.openWebSocketSession(
            accessToken = "accessToken",
            contentType = "application/json",
            headers = mapOf("key" to "value"),
            converter = converter
        )

        assertEquals(session, actual)
        coVerify {
            engine.openWebSocketSession(
                accessToken = "accessToken",
                contentType = "application/json",
                headers = mapOf("key" to "value"),
                converter = converter
            )
        }
    }

    @Test
    fun testOpenWebSocketSession_Error() = runTest {
        val exception = MimiIOException("error")
        coEvery {
            engine.openWebSocketSession<String>(any(), any(), any(), any())
        } throws exception
        val converter = mockk<MimiModelConverter<String>>()

        assertFailsWith<MimiIOException>("error") {
            target.openWebSocketSession(
                accessToken = "accessToken",
                contentType = "application/json",
                headers = mapOf("key" to "value"),
                converter = converter
            )
        }

        coVerify {
            engine.openWebSocketSession(
                accessToken = "accessToken",
                contentType = "application/json",
                headers = mapOf("key" to "value"),
                converter = converter
            )
        }
    }
}
