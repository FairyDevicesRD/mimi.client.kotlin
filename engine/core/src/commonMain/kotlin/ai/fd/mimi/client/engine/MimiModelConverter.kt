package ai.fd.mimi.client.engine

import ai.fd.mimi.client.MimiJsonException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

abstract class MimiModelConverter<T>(protected val json: Json) {
    @Throws(MimiJsonException::class)
    abstract fun decode(jsonText: String): T

    fun <R> encode(model: R, serializer: KSerializer<R>): String = json.encodeToString(serializer, model)
}
