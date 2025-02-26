package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.service.asr.core.MimiAsrResultStatus
import ai.fd.mimi.client.service.asr.core.entity.MimiAsrResultStatusEntity
import ai.fd.mimi.client.service.nict.asr.entity.MimiNictAsrV1ResultEntity
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
class MimiNictAsrV1ModelConverterTest {

    @MockK(relaxed = true)
    private lateinit var json: Json

    private lateinit var target: MimiNictAsrV1ModelConverter

    @BeforeEach
    fun setUp() {
        target = MimiNictAsrV1ModelConverter(json)
    }

    @Test
    fun decode() {
        val entity = MimiNictAsrV1ResultEntity(
            type = "type",
            sessionId = "sessionId",
            status = MimiAsrResultStatusEntity.RECOG_FINISHED,
            response = listOf(MimiNictAsrV1ResultEntity.Response("result"))
        )
        every {
            hint(MimiNictAsrV1ResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiNictAsrV1ResultEntity>>(), "jsonString")
        } returns entity

        val actual = target.decode("jsonString")

        assertEquals(
            MimiNictAsrV1Result(
                type = "type",
                sessionId = "sessionId",
                status = MimiAsrResultStatus.RECOG_FINISHED,
                response = listOf("result")
            ),
            actual
        )
    }

    @Test
    fun decode_error_SerializationException() {
        every {
            hint(MimiNictAsrV1ResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiNictAsrV1ResultEntity>>(), "jsonString")
        } throws SerializationException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }

    @Test
    fun decode_error_IllegalArgumentException() {
        every {
            hint(MimiNictAsrV1ResultEntity::class)
            @Suppress("JsonStandardCompliance")
            json.decodeFromString(any<KSerializer<MimiNictAsrV1ResultEntity>>(), "jsonString")
        } throws IllegalArgumentException("")

        assertFailsWith<MimiJsonException>("Failed to decode: jsonString") {
            target.decode("jsonString")
        }
    }
}
