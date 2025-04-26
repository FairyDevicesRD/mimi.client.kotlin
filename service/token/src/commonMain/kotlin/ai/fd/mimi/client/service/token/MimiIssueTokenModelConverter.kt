package ai.fd.mimi.client.service.token

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.service.token.entity.MimiIssueTokenResultEntity
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * An implementation of [MimiModelConverter.Binary] for issuing token of the Mimi services.
 */
internal class MimiIssueTokenModelConverter(
    private val json: Json = Json {
        ignoreUnknownKeys = true
    }
) : MimiModelConverter.JsonString<MimiIssueTokenResult>() {
    override fun decode(jsonText: String): MimiIssueTokenResult = try {
        json.decodeFromString<MimiIssueTokenResultEntity>(jsonText).convert()
    } catch (e: SerializationException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    } catch (e: IllegalArgumentException) {
        throw MimiJsonException("Failed to decode: $jsonText", e)
    }
}

private fun MimiIssueTokenResultEntity.convert(): MimiIssueTokenResult = MimiIssueTokenResult(
    accessToken = accessToken,
    expiresIn = expiresIn,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp
)
