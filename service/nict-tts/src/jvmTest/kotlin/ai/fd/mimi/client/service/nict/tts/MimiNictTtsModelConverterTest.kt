package ai.fd.mimi.client.service.nict.tts

import kotlin.test.assertEquals
import okio.ByteString
import org.junit.jupiter.api.Test

/**
 * Tests for [MimiNictTtsModelConverter].
 */
class MimiNictTtsModelConverterTest {

    @Test
    fun testDecode() {
        val actual = MimiNictTtsModelConverter().decode(ByteString.of(1, 2, 3))

        assertEquals(ByteString.of(1, 2, 3), actual.audioBinary)
    }
}
