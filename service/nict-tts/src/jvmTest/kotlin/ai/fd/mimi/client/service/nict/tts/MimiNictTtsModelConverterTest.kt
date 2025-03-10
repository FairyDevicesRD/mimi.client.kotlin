package ai.fd.mimi.client.service.nict.tts

import kotlin.test.assertContentEquals
import kotlinx.io.bytestring.ByteString
import org.junit.jupiter.api.Test

/**
 * Tests for [MimiNictTtsModelConverter].
 */
class MimiNictTtsModelConverterTest {

    @Test
    fun testDecode() {
        val actual = MimiNictTtsModelConverter().decode(ByteString(1, 2, 3))

        assertContentEquals(byteArrayOf(1, 2, 3), actual.audioBinary)
    }
}
