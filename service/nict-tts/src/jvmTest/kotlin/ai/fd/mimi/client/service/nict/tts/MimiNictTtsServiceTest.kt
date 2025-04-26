package ai.fd.mimi.client.service.nict.tts

import ai.fd.mimi.client.annotation.ExperimentalMimiApi
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Tests for [MimiNictTtsService].
 */
@OptIn(ExperimentalMimiApi::class)
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
        every { engineFactory.create(any(), any(), any()) } returns engine

        val service = MimiNictTtsService(
            engineFactory = engineFactory,
            accessToken = "accessToken",
            useSsl = true,
            host = "host",
        )

        assertEquals("speech_synthesis", service.path)
        verify { engineFactory.create(useSsl = true, host = "host", port = 443) }
    }

    @Test
    fun testPublicConstructor_noSsl() {
        every { engineFactory.create(any(), any(), any()) } returns engine

        val service = MimiNictTtsService(
            engineFactory = engineFactory,
            accessToken = "accessToken",
            useSsl = false,
            host = "host",
        )

        assertEquals("speech_synthesis", service.path)
        verify { engineFactory.create(useSsl = false, host = "host", port = 80) }
    }

    @Test
    fun testPublicConstructor_customPort() {
        every { engineFactory.create(any(), any(), any()) } returns engine

        val service = MimiNictTtsService(
            engineFactory = engineFactory,
            accessToken = "accessToken",
            useSsl = true,
            host = "host",
            port = 1000
        )

        assertEquals("speech_synthesis", service.path)
        verify { engineFactory.create(useSsl = true, host = "host", port = 1000) }
    }

    @Test
    fun testPublicConstructor_customPath() {
        every { engineFactory.create(any(), any(), any()) } returns engine

        val service = MimiNictTtsService(
            engineFactory = engineFactory,
            accessToken = "accessToken",
            useSsl = true,
            host = "host",
            path = "path",
        )

        assertEquals("path", service.path)
        verify { engineFactory.create(useSsl = true, host = "host", port = 443) }
    }

    @Test
    fun testRequestTts() = runTest {
        coEvery {
            engine.request<MimiNictTtsResult>(eq("path"), any(), any(), any(), any())
        } returns Result.success(MimiNictTtsResult(byteArrayOf(1, 2, 3)))

        val service = MimiNictTtsService(
            path = "path",
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
        assertContentEquals(byteArrayOf(1, 2, 3), actual.getOrThrow().audioBinary)
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
                path = "path",
                accessToken = "accessToken",
                requestBody = match { it is MimiNetworkEngine.RequestBody.FormData && it.fields == expectedFormData },
                headers = mapOf(),
                converter = modelConverter
            )
        }
    }

    @Test
    fun testRequestTts_invalidRate() = runTest {
        val service = MimiNictTtsService(
            path = "path",
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
            engine.request<MimiNictTtsResult>(eq("path"), any(), any(), any(), any())
        } returns Result.failure(exception)

        val service = MimiNictTtsService(
            path = "path",
            engine = engine,
            accessToken = "accessToken",
            converter = modelConverter
        )

        val actual = service.requestTts(text = "input", options = MimiNictTtsOptions.DEFAULT)

        assertFalse(actual.isSuccess)
        assertEquals(exception, actual.exceptionOrNull())
    }
}
