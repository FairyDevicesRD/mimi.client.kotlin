package ai.fd.mimi.client.service.asr.core.internal

import ai.fd.mimi.client.service.asr.core.MimiAsrAudioFormat
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class MimiAsrContentTypeFormatterTest {

    @Test
    fun testToContentTypeString() {
        val actual = MimiAsrContentTypeFormatter.toContentTypeString(
            audioFormat = MimiAsrAudioFormat.PCM,
            audioBitrate = 12,
            audioSamplingRate = 34567
        )

        assertEquals("audio/x-pcm;bit=12;rate=34567;channels=1", actual)
    }
}
