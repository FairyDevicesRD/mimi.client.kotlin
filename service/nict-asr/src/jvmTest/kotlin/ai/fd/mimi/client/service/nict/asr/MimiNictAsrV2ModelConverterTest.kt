package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.service.asr.core.MimiAsrResultStatus
import ai.fd.mimi.client.service.asr.core.entity.MimiAsrResultStatusEntity
import ai.fd.mimi.client.service.nict.asr.entity.MimiNictAsrV2ResultEntity
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
class MimiNictAsrV2ModelConverterTest {

    @MockK(relaxed = true)
    private lateinit var json: Json

    private lateinit var target: MimiNictAsrV2ModelConverter

    @BeforeEach
    fun setUp() {
        target = MimiNictAsrV2ModelConverter(json)
    }

    @Test
    fun decode() {
        val entity = MimiNictAsrV2ResultEntity(
            type = "type",
            sessionId = "sessionId",
            status = MimiAsrResultStatusEntity.RECOG_FINISHED,
            response = listOf(
                MimiNictAsrV2ResultEntity.Response(
                    result = "result",
                    words = listOf("words1", "words2"),
                    determined = true,
                    time = 123L
                )
            )
        )
        every {
            hint(MimiNictAsrV2ResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiNictAsrV2ResultEntity>>(), "jsonString")
        } returns entity

        val actual = target.decode("jsonString")

        assertEquals(
            MimiNictAsrV2Result(
                type = "type",
                sessionId = "sessionId",
                status = MimiAsrResultStatus.RECOG_FINISHED,
                response = listOf(
                    MimiNictAsrV2Result.Response(
                        result = "result",
                        words = listOf("words1", "words2"),
                        determined = true,
                        time = 123L
                    )
                )
            ),
            actual
        )
    }

    @Test
    fun decode_error_SerializationException() {
        every {
            hint(MimiNictAsrV2ResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiNictAsrV2ResultEntity>>(), "jsonString")
        } throws SerializationException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }

    @Test
    fun decode_error_IllegalArgumentException() {
        every {
            hint(MimiNictAsrV2ResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiNictAsrV2ResultEntity>>(), "jsonString")
        } throws IllegalArgumentException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }
}
