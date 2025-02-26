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
class MimiNictAsrV1OptionsTest {

    @Test
    fun testToContentType() {
        val audioFormat = mockk<MimiAsrAudioFormat>()
        mockkObject(MimiAsrContentTypeFormatter)
        every { MimiAsrContentTypeFormatter.toContentTypeString(audioFormat, 12, 34567) } returns "contentType"

        val actual = MimiNictAsrV1Options(
            audioFormat = audioFormat,
            audioBitrate = 12,
            audioSamplingRate = 34567,
            inputLanguage = mockk()
        ).toContentType()

        assertEquals("contentType", actual)
    }
}
