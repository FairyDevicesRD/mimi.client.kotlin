package ai.fd.mimi.client.service.token

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.service.token.entity.MimiValidateTokenResultEntity
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiValidateTokenModelConverterTest {

    @MockK(relaxed = true)
    private lateinit var json: Json

    private lateinit var target: MimiValidateTokenModelConverter


    @BeforeEach
    fun setUp() {
        target = MimiValidateTokenModelConverter(json)
    }

    @Test
    fun decode() {
        val entity = MimiValidateTokenResultEntity(tokenStatus = "tokenStatus")
        every {
            hint(MimiValidateTokenResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiValidateTokenResultEntity>>(), "jsonString")
        } returns entity

        val actual = target.decode("jsonString")

        assertEquals(
            MimiValidateTokenResult(
                tokenStatus = "tokenStatus"
            ),
            actual
        )
    }

    @Test
    fun decode_error_SerializationException() {
        every {
            hint(MimiValidateTokenResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiValidateTokenResultEntity>>(), "jsonString")
        } throws SerializationException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }

    @Test
    fun decode_error_IllegalArgumentException() {
        every {
            hint(MimiValidateTokenResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiValidateTokenResultEntity>>(), "jsonString")
        } throws IllegalArgumentException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }
}
