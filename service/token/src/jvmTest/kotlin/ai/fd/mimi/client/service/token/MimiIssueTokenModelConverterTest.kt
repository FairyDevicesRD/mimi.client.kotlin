package ai.fd.mimi.client.service.token

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.service.token.entity.MimiIssueTokenResultEntity
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
class MimiIssueTokenModelConverterTest {

    @MockK(relaxed = true)
    private lateinit var json: Json

    private lateinit var target: MimiIssueTokenModelConverter


    @BeforeEach
    fun setUp() {
        target = MimiIssueTokenModelConverter(json)
    }

    @Test
    fun decode() {
        val entity = MimiIssueTokenResultEntity(
            accessToken = "accessToken",
            expiresIn = 1234,
            startTimestamp = 5678,
            endTimestamp = 9012
        )
        every {
            hint(MimiIssueTokenResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiIssueTokenResultEntity>>(), "jsonString")
        } returns entity

        val actual = target.decode("jsonString")

        assertEquals(
            MimiIssueTokenResult(
                accessToken = "accessToken",
                expiresIn = 1234,
                startTimestamp = 5678,
                endTimestamp = 9012
            ),
            actual
        )
    }

    @Test
    fun decode_error_SerializationException() {
        every {
            hint(MimiIssueTokenResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiIssueTokenResultEntity>>(), "jsonString")
        } throws SerializationException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }

    @Test
    fun decode_error_IllegalArgumentException() {
        every {
            hint(MimiIssueTokenResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiIssueTokenResultEntity>>(), "jsonString")
        } throws IllegalArgumentException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }
}
