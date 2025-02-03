package ai.fd.mimi.client.service.nict.tts

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import okio.ByteString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Tests for [MimiNictTtsService].
 */
@ExtendWith(MockKExtension::class)
class MimiNictTtsServiceTest {

    @MockK
    private lateinit var engine: MimiNetworkEngine

    @MockK
    private lateinit var engineFactory: MimiNetworkEngine.Factory

    @MockK
    private lateinit var modelConverter: MimiModelConverter<MimiNictTtsResult>

    @Test
    fun testPublicConstructor_ssl() {
        every { engineFactory.create(any(), any(), any(), any()) } returns engine

        MimiNictTtsService(
            engineFactory = engineFactory,
            accessToken = "accessToken",
            useSsl = true,
            host = "host",
            path = "path",
        )

        verify { engineFactory.create(useSsl = true, host = "host", port = 443, path = "path") }
    }

    @Test
    fun testPublicConstructor_noSsl() {
        every { engineFactory.create(any(), any(), any(), any()) } returns engine

        MimiNictTtsService(
            engineFactory = engineFactory,
            accessToken = "accessToken",
            useSsl = false,
            host = "host",
            path = "path",
        )

        verify { engineFactory.create(useSsl = false, host = "host", port = 80, path = "path") }
    }

    @Test
    fun testPublicConstructor_customPort() {
        every { engineFactory.create(any(), any(), any(), any()) } returns engine

        MimiNictTtsService(
            engineFactory = engineFactory,
            accessToken = "accessToken",
            useSsl = true,
            host = "host",
            path = "path",
            port = 1000
        )

        verify { engineFactory.create(useSsl = true, host = "host", port = 1000, path = "path") }
    }

    @Test
    fun testRequestTts() = runTest {
        coEvery {
            engine.request<MimiNictTtsResult>(any(), any(), any(), any())
        } returns Result.success(MimiNictTtsResult(ByteString.of(1, 2, 3)))

        val service = MimiNictTtsService(
            engine = engine,
            accessToken = "accessToken",
            converter = modelConverter
        )
        val options = mockk<MimiNictTtsOptions> {
            every { language.value } returns "language"
            every { audioFormat.value } returns "audioFormat"
            every { audioEndian.value } returns "audioEndian"
            every { gender.value } returns "gender"
            every { rate } returns 1.5f
        }

        val actual = service.requestTts(text = "input", options = options)

        assertTrue(actual.isSuccess)
        assertEquals(ByteString.of(1, 2, 3), actual.getOrThrow().audioBinary)
        val expectedFormData = mapOf(
            "text" to "input",
            "engine" to "nict",
            "lang" to "language",
            "audio_format" to "audioFormat",
            "audio_endian" to "audioEndian",
            "gender" to "gender",
            "rate" to "1.5"
        )
        coVerify {
            engine.request(
                "accessToken",
                match { it is MimiNetworkEngine.RequestBody.FormData && it.fields == expectedFormData },
                mapOf(),
                modelConverter
            )
        }
    }

    @Test
    fun testRequestTts_invalidRate() = runTest {
        val service = MimiNictTtsService(
            engine = engine,
            accessToken = "accessToken",
            converter = modelConverter
        )

        assertThrows<IllegalArgumentException>("`rate` should be in 0.5~2.0.") {
            service.requestTts(text = "input", options = MimiNictTtsOptions.DEFAULT.copy(rate = 0.4f))
        }
        assertThrows<IllegalArgumentException>("`rate` should be in 0.5~2.0.") {
            service.requestTts(text = "input", options = MimiNictTtsOptions.DEFAULT.copy(rate = 2.1f))
        }
    }

    @Test
    fun testRequestTts_engineError() = runTest {
        val exception = mockk<Exception>()
        coEvery {
            engine.request<MimiNictTtsResult>(any(), any(), any(), any())
        } returns Result.failure(exception)

        val service = MimiNictTtsService(
            engine = engine,
            accessToken = "accessToken",
            converter = modelConverter
        )

        val actual = service.requestTts(text = "input", options = MimiNictTtsOptions.DEFAULT)

        assertFalse(actual.isSuccess)
        assertEquals(exception, actual.exceptionOrNull())
    }
}
