package ai.fd.mimi.client.engine

import io.mockk.junit5.MockKExtension
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiModelConverterTest {

    @Test
    fun testDecode_JsonString_IgnoreResult() {
        assertEquals(Unit, MimiModelConverter.JsonString.IgnoreResult.decode(""))
    }
}
