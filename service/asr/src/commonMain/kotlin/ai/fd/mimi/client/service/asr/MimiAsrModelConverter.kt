package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.service.asr.entity.MimiAsrResultEntity
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal class MimiAsrModelConverter(json: Json = Json.Default) : MimiModelConverter<MimiAsrResult>(json) {

    @Throws(MimiJsonException::class)
    override fun decode(jsonText: String): MimiAsrResult = try {
        json.decodeFromString<MimiAsrResultEntity>(jsonText).convert()
    } catch (e: SerializationException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    } catch (e: IllegalArgumentException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    }

    @Throws(MimiJsonException::class)
    private fun MimiAsrResultEntity.convert(): MimiAsrResult = MimiAsrResult(
        type = type,
        sessionId = sessionId,
        status = status.toStatus(),
        response = response.map { it.convert() }
    )

    @Throws(MimiJsonException::class)
    private fun MimiAsrResultEntity.Response.convert(): MimiAsrResult.Response {
        val startTime = time.getOrNull(0) ?: throw MimiJsonException("time[0] is missing")
        val endTime = time.getOrNull(1) ?: throw MimiJsonException("time[1] is missing")
        return MimiAsrResult.Response(
            pronunciation = pronunciation,
            result = result,
            startTime = startTime,
            endTime = endTime
        )
    }
}
