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
        val byteArray = byteArrayOf(1, 2, 3)

        val actual = MimiNictTtsModelConverter().decode(byteArray)

        assertEquals(ByteString.of(1, 2, 3), actual.audioBinary)
    }
}
