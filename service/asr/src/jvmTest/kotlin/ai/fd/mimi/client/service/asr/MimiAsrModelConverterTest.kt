package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.service.asr.core.MimiAsrResultStatus
import ai.fd.mimi.client.service.asr.core.entity.MimiAsrResultStatusEntity
import ai.fd.mimi.client.service.asr.entity.MimiAsrResultEntity
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
class MimiAsrModelConverterTest {

    @MockK(relaxed = true)
    private lateinit var json: Json

    private lateinit var target: MimiAsrModelConverter

    @BeforeEach
    fun setUp() {
        target = MimiAsrModelConverter(json)
    }

    @Test
    fun decode() {
        val entity = MimiAsrResultEntity(
            type = "type",
            sessionId = "sessionId",
            status = MimiAsrResultStatusEntity.RECOG_FINISHED,
            response = listOf(
                MimiAsrResultEntity.Response(
                    pronunciation = "pronunciation",
                    result = "result",
                    time = listOf(0L, 1L)
                )
            )
        )
        every {
            hint(MimiAsrResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiAsrResultEntity>>(), "jsonString")
        } returns entity

        val actual = target.decode("jsonString")

        assertEquals(
            MimiAsrResult(
                type = "type",
                sessionId = "sessionId",
                status = MimiAsrResultStatus.RECOG_FINISHED,
                response = listOf(
                    MimiAsrResult.Response(
                        pronunciation = "pronunciation",
                        result = "result",
                        startTime = 0L,
                        endTime = 1L
                    )
                )
            ),
            actual
        )
    }

    @Test
    fun decode_error_SerializationException() {
        every {
            hint(MimiAsrResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiAsrResultEntity>>(), "jsonString")
        } throws SerializationException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }

    @Test
    fun decode_error_IllegalArgumentException() {
        every {
            hint(MimiAsrResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiAsrResultEntity>>(), "jsonString")
        } throws IllegalArgumentException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }

    @Test
    fun decode_error_missingStartTime() {
        val entity = MimiAsrResultEntity(
            type = "",
            sessionId = "",
            status = MimiAsrResultStatusEntity.RECOG_FINISHED,
            response = listOf(
                MimiAsrResultEntity.Response(
                    pronunciation = "",
                    result = "",
                    time = listOf()
                )
            )
        )
        every {
            hint(MimiAsrResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiAsrResultEntity>>(), "jsonString")
        } returns entity

        assertFailsWith<MimiJsonException>("time[0] is missing") {
            target.decode("jsonString")
        }
    }

    @Test
    fun decode_error_missingEndTime() {
        val entity = MimiAsrResultEntity(
            type = "",
            sessionId = "",
            status = MimiAsrResultStatusEntity.RECOG_FINISHED,
            response = listOf(
                MimiAsrResultEntity.Response(
                    pronunciation = "",
                    result = "",
                    time = listOf(0)
                )
            )
        )
        every {
            hint(MimiAsrResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiAsrResultEntity>>(), "jsonString")
        } returns entity

        assertFailsWith<MimiJsonException>("time[1] is missing") {
            target.decode("jsonString")
        }
    }
}
