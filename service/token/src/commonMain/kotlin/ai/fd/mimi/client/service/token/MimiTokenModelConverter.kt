package ai.fd.mimi.client.service.token

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.service.token.entity.MimiTokenResultEntity
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * An implementation of [MimiModelConverter.Binary] for issuing token of the Mimi services.
 */
class MimiTokenModelConverter(
    json: Json = Json {
        ignoreUnknownKeys = true
    }
) : MimiModelConverter.JsonString<MimiTokenResult>(json = json) {
    override fun decode(jsonText: String): MimiTokenResult = try {
        json.decodeFromString<MimiTokenResultEntity>(jsonText).convert()
    } catch (e: SerializationException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    } catch (e: IllegalArgumentException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    }
}

private fun MimiTokenResultEntity.convert(): MimiTokenResult = MimiTokenResult(
    accessToken = accessToken,
    expiresIn = expiresIn,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp
)
