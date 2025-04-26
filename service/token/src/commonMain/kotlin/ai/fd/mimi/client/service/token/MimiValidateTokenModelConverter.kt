package ai.fd.mimi.client.service.token

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.service.token.entity.MimiValidateTokenResultEntity
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * An implementation of [MimiModelConverter.Binary] for validating token of the Mimi services.
 */
internal class MimiValidateTokenModelConverter(
    private val json: Json = Json {
        ignoreUnknownKeys = true
    }
) : MimiModelConverter.JsonString<MimiValidateTokenResult>() {
    override fun decode(jsonText: String): MimiValidateTokenResult = try {
        json.decodeFromString<MimiValidateTokenResultEntity>(jsonText).convert()
    } catch (e: SerializationException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    } catch (e: IllegalArgumentException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    }
}

private fun MimiValidateTokenResultEntity.convert(): MimiValidateTokenResult = MimiValidateTokenResult(
    tokenStatus = tokenStatus
)
