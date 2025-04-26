package ai.fd.mimi.client.service.nict.asr

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
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import kotlinx.io.bytestring.ByteString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiNictAsrV2ServiceTest {

    @MockK
    private lateinit var engine: MimiNetworkEngine

    @MockK
    private lateinit var converter: MimiModelConverter.JsonString<MimiNictAsrV2Result>

    @Test
    fun testPublicConstructor_ssl() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(true, "service.mimi.fd.ai", 443) } returns engine
        }

        val service = MimiNictAsrV2Service(engineFactory, "accessToken")
        assertEquals("/", service.path)
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testPublicConstructor_noSsl() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(false, "service.mimi.fd.ai", 80) } returns engine
        }

        val service = MimiNictAsrV2Service(engineFactory, "accessToken", false)
        assertEquals("/", service.path)
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testPublicConstructor_ssl_customHost() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(true, "example.com", 443) } returns engine
        }

        val service = MimiNictAsrV2Service(engineFactory, "accessToken", true, "example.com")
        assertEquals("/", service.path)
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testPublicConstructor_noSsl_customHost() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(false, "example.com", 1234) } returns engine
        }

        val service = MimiNictAsrV2Service(engineFactory, "accessToken", false, "example.com", 1234)
        assertEquals("/", service.path)
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testPublicConstructor_customPath() {
        val engine = mockk<MimiNetworkEngine>()
        val engineFactory = mockk<MimiNetworkEngine.Factory> {
            every { create(true, "service.mimi.fd.ai", 443) } returns engine
        }

        val service = MimiNictAsrV2Service(engineFactory, "accessToken", path = "path")
        assertEquals(service.path, "path")
        assertEquals(engine, service.engine)
        assertEquals("accessToken", service.accessToken)
    }

    @Test
    fun testRequestAsr() = runTest {
        val options = mockk<MimiNictAsrV2Options>()
        every { options.toContentType() } returns "contentType"
        every { options.inputLanguage.value } returns "lang"
        every { options.toNictAsrOptions() } returns "nictAsrOptions"
        every { options.progressive } returns false
        val result = mockk<MimiNictAsrV2Result>()
        coEvery {
            engine.request(
                path = "path",
                accessToken = "accessToken",
                requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "contentType"),
                headers = mapOf(
                    "x-mimi-process" to "nict-asr",
                    "x-mimi-input-language" to "lang",
                    "x-mimi-nict-asr-options" to "nictAsrOptions"
                ),
                converter = converter
            )
        } returns Result.success(result)
        val target = MimiNictAsrV2Service("path", engine, "accessToken", converter)

        val actual = target.requestAsr(byteArrayOf(1, 2, 3), options)

        assertEquals(result, actual.getOrThrow())
    }

    @Test
    fun testRequestAsr_error_progressive() = runTest {
        val options = mockk<MimiNictAsrV2Options>()
        every { options.progressive } returns true
        val target = MimiNictAsrV2Service("path", engine, "accessToken", converter)

        assertFailsWith<IllegalStateException>("Progressive mode is not supported for HTTP request") {
            target.requestAsr(byteArrayOf(1, 2, 3), options)
        }
    }

    @Test
    fun testOpenAsrSession() = runTest {
        val options = mockk<MimiNictAsrV2Options>()
        every { options.toContentType() } returns "contentType"
        every { options.inputLanguage.value } returns "lang"
        every { options.toNictAsrOptions() } returns "nictAsrOptions"
        val internalSession = mockk<MimiWebSocketSessionInternal<MimiNictAsrV2Result>>()
        coEvery {
            engine.openWebSocketSession(
                path = "path",
                accessToken = "accessToken",
                headers = mapOf(
                    "x-mimi-process" to "nict-asr",
                    "x-mimi-input-language" to "lang",
                    "x-mimi-nict-asr-options" to "nictAsrOptions"
                ),
                contentType = "contentType",
                converter = converter
            )
        } returns internalSession
        val target = spyk(MimiNictAsrV2Service("path", engine, "accessToken", converter))
        val session = mockk<MimiAsrWebSocketSession<MimiNictAsrV2Result>>()
        every { target.createMimiAsrWebSocketSession(internalSession) } returns session

        val actual = target.openAsrSession(options)

        assertEquals(session, actual)
    }
}
