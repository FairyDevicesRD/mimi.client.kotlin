package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.service.nict.asr.entity.MimiNictAsrV2ResultEntity
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal class MimiNictAsrV2ModelConverter(json: Json = Json.Default) :
    MimiModelConverter.JsonString<MimiNictAsrV2Result>(json) {

    @Throws(MimiJsonException::class)
    override fun decode(jsonText: String): MimiNictAsrV2Result = try {
        json.decodeFromString<MimiNictAsrV2ResultEntity>(jsonText).convert()
    } catch (e: SerializationException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    } catch (e: IllegalArgumentException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    }

    private fun MimiNictAsrV2ResultEntity.convert(): MimiNictAsrV2Result = MimiNictAsrV2Result(
        type = type,
        sessionId = sessionId,
        status = status.toStatus(),
        response = response.map { it.convert() }
    )

    private fun MimiNictAsrV2ResultEntity.Response.convert(): MimiNictAsrV2Result.Response =
        MimiNictAsrV2Result.Response(
            result = result,
            words = words,
            determined = determined,
            time = time
        )
}
