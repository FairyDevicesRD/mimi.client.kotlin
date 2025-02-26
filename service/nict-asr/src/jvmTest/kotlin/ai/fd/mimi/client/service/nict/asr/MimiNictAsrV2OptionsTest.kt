package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.service.asr.core.MimiAsrAudioFormat
import ai.fd.mimi.client.service.asr.core.internal.MimiAsrContentTypeFormatter
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiNictAsrV2OptionsTest {

    @Test
    fun testToContentType() {
        val audioFormat = mockk<MimiAsrAudioFormat>()
        mockkObject(MimiAsrContentTypeFormatter)
        every { MimiAsrContentTypeFormatter.toContentTypeString(audioFormat, 12, 34567) } returns "contentType"

        val actual = MimiNictAsrV2Options(
            audioFormat = audioFormat,
            audioBitrate = 12,
            audioSamplingRate = 34567,
            inputLanguage = mockk(),
            progressive = false,
            temporary = true,
            temporaryInterval = 890
        ).toContentType()

        assertEquals("contentType", actual)
    }

    @Test
    fun testToNictAsrOptions() {
        val actual = MimiNictAsrV2Options(
            audioFormat = mockk(),
            audioBitrate = 12,
            audioSamplingRate = 34567,
            inputLanguage = mockk(),
            progressive = false,
            temporary = true,
            temporaryInterval = 890
        ).toNictAsrOptions()

        assertEquals("response_format=v2;progressive=false;temporary=true;temporary_interval=890", actual)
    }
}
