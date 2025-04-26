package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import ai.fd.mimi.client.service.asr.core.MimiAsrWebSocketSession
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.io.bytestring.ByteString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiAsrServiceTest {

    @MockK
    private lateinit var engine: MimiNetworkEngine

    @MockK
    private lateinit var converter: MimiModelConverter.JsonString<MimiAsrResult>

    @Test
    fun testConstructor() {
        val engineFactory = mockk<MimiNetworkEngine.Factory>(relaxed = true)

        val sslService = MimiAsrService(engineFactory, "accessToken")
        assertEquals("/", sslService.path)
        verify { engineFactory.create(true, "service.mimi.fd.ai", 443) }
        confirmVerified(engineFactory)

        val noSslService = MimiAsrService(engineFactory, "accessToken", false)
        assertEquals("/", noSslService.path)
        verify { engineFactory.create(false, "service.mimi.fd.ai", 80) }
        confirmVerified(engineFactory)

        val sslCustomHostService = MimiAsrService(engineFactory, "accessToken", true, "example.com")
        assertEquals("/", sslCustomHostService.path)
        verify { engineFactory.create(true, "example.com", 443) }
        confirmVerified(engineFactory)

        val noSslCustomHostService = MimiAsrService(engineFactory, "accessToken", false, "example.com", 1234)
        assertEquals("/", noSslCustomHostService.path)
        verify { engineFactory.create(false, "example.com", 1234) }
        confirmVerified(engineFactory)

        val customPathService = MimiAsrService(engineFactory, "accessToken", path = "path")
        assertEquals(customPathService.path, "path")
        verify { engineFactory.create(true, "service.mimi.fd.ai", 443) }
        confirmVerified(engineFactory)
    }

    @Test
    fun testRequestAsr() = runTest {
        val options = mockk<MimiAsrOptions>()
        every { options.toContentType() } returns "contentType"
        every { options.inputLanguage.value } returns "lang"
        val result = mockk<MimiAsrResult>()
        coEvery {
            engine.request(
                path = "path",
                accessToken = "accessToken",
                requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "contentType"),
                headers = mapOf(
                    "x-mimi-process" to "asr",
                    "x-mimi-input-language" to "lang"
                ),
                converter = converter
            )
        } returns Result.success(result)
        val target = MimiAsrService("path", engine, "accessToken", converter)

        val actual = target.requestAsr(byteArrayOf(1, 2, 3), options)

        assertEquals(result, actual.getOrThrow())
    }

    @Test
    fun testOpenAsrSession() = runTest {
        val options = mockk<MimiAsrOptions>()
        every { options.toContentType() } returns "contentType"
        every { options.inputLanguage.value } returns "lang"
        val internalSession = mockk<MimiWebSocketSessionInternal<MimiAsrResult>>()
        coEvery {
            engine.openWebSocketSession(
                path = "path",
                accessToken = "accessToken",
                headers = mapOf(
                    "x-mimi-process" to "asr",
                    "x-mimi-input-language" to "lang"
                ),
                contentType = "contentType",
                converter = converter
            )
        } returns internalSession
        val target = spyk(MimiAsrService("path", engine, "accessToken", converter))
        val session = mockk<MimiAsrWebSocketSession<MimiAsrResult>>()
        every { target.createMimiAsrWebSocketSession(internalSession) } returns session

        val actual = target.openAsrSession(options)

        assertEquals(session, actual)
    }
}
