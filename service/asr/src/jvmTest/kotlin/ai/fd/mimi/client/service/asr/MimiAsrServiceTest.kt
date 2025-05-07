package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import ai.fd.mimi.client.service.asr.core.MimiAsrWebSocketSession
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
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
    private lateinit var converter: MimiModelConverter.EncodableJsonString<MimiAsrResult>

    @Test
    fun testPublicConstructor_ssl() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(true, "service.mimi.fd.ai", 443) } returns engine
        }

        val service = MimiAsrService(engineFactory, "accessToken")
        assertEquals("", service.path)
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testPublicConstructor_noSsl() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(false, "service.mimi.fd.ai", 80) } returns engine
        }

        val service = MimiAsrService(engineFactory, "accessToken", false)
        assertEquals("", service.path)
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testPublicConstructor_ssl_customHost() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(true, "example.com", 443) } returns engine
        }

        val service = MimiAsrService(engineFactory, "accessToken", true, "example.com")
        assertEquals("", service.path)
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testPublicConstructor_noSsl_customHost() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(false, "example.com", 1234) } returns engine
        }

        val service = MimiAsrService(engineFactory, "accessToken", false, "example.com", 1234)
        assertEquals("", service.path)
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testPublicConstructor_customPath() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(true, "service.mimi.fd.ai", 443) } returns engine
        }

        val service = MimiAsrService(engineFactory, "accessToken", path = "path")
        assertEquals(service.path, "path")
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
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
