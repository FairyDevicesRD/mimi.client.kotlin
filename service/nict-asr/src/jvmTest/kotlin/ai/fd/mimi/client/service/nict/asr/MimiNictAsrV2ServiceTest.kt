package ai.fd.mimi.client.service.nict.asr

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
    fun testConstructor() {
        val engineFactory = mockk<MimiNetworkEngine.Factory>(relaxed = true)

        MimiNictAsrV2Service(engineFactory, "accessToken")
        verify { engineFactory.create(true, "service.mimi.fd.ai", 443) }
        confirmVerified(engineFactory)

        MimiNictAsrV2Service(engineFactory, "accessToken", false)
        verify { engineFactory.create(false, "service.mimi.fd.ai", 80) }
        confirmVerified(engineFactory)

        MimiNictAsrV2Service(engineFactory, "accessToken", true, "example.com")
        verify { engineFactory.create(true, "example.com", 443) }
        confirmVerified(engineFactory)

        MimiNictAsrV2Service(engineFactory, "accessToken", false, "example.com", 1234)
        verify { engineFactory.create(false, "example.com", 1234) }
        confirmVerified(engineFactory)
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
        val target = MimiNictAsrV2Service(engine, "accessToken", converter)

        val actual = target.requestAsr(byteArrayOf(1, 2, 3), options)

        assertEquals(result, actual.getOrThrow())
    }

    @Test
    fun testRequestAsr_error_progressive() = runTest {
        val options = mockk<MimiNictAsrV2Options>()
        every { options.progressive } returns true
        val target = MimiNictAsrV2Service(engine, "accessToken", converter)

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
        val target = spyk(MimiNictAsrV2Service(engine, "accessToken", converter))
        val session = mockk<MimiAsrWebSocketSession<MimiNictAsrV2Result>>()
        every { target.createMimiAsrWebSocketSession(internalSession) } returns session

        val actual = target.openAsrSession(options)

        assertEquals(session, actual)
    }
}
