package ai.fd.mimi.client.engine

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.MimiSerializationException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okio.ByteString

sealed interface MimiModelConverter<T> {
    abstract class JsonString<T>(protected val json: Json) : MimiModelConverter<T> {
        @Throws(MimiJsonException::class)
        abstract fun decode(jsonText: String): T

        fun <R> encode(model: R, serializer: KSerializer<R>): String = json.encodeToString(serializer, model)
    }

    abstract class Binary<T> : MimiModelConverter<T> {
        @Throws(MimiSerializationException::class)
        abstract fun decode(data: ByteString): T
    }
}
