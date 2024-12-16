package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.service.nict.asr.entity.MimiNictAsrV1ResultEntity
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal class MimiNictAsrV1ModelConverter(json: Json = Json.Default) : MimiModelConverter<MimiNictAsrV1Result>(json) {

    @Throws(MimiJsonException::class)
    override fun decode(jsonText: String): MimiNictAsrV1Result = try {
        json.decodeFromString<MimiNictAsrV1ResultEntity>(jsonText).convert()
    } catch (e: SerializationException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    } catch (e: IllegalArgumentException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    }

    @Throws(MimiJsonException::class)
    private fun MimiNictAsrV1ResultEntity.convert(): MimiNictAsrV1Result = MimiNictAsrV1Result(
        type = type,
        sessionId = sessionId,
        status = status.toStatus(),
        response = response.map { it.result }
    )
}
