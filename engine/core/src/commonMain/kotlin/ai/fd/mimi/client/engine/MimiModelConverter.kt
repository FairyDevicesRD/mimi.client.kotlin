package ai.fd.mimi.client.engine

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.MimiSerializationException
import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

sealed interface MimiModelConverter<T> {

    abstract class JsonString<T> : MimiModelConverter<T> {
        @Throws(MimiJsonException::class)
        abstract fun decode(jsonText: String): T

        companion object {

            /**
             * A [JsonString] which ignores the result of the decoding.
             */
            val IgnoreResult = object : JsonString<Unit>() {
                override fun decode(jsonText: String): Unit = Unit
            }
        }
    }

    abstract class EncodableJsonString<T>(protected val json: Json) : JsonString<T>() {
        fun <R> encode(model: R, serializer: KSerializer<R>): String = json.encodeToString(serializer, model)
    }

    abstract class Binary<T> : MimiModelConverter<T> {
        @Throws(MimiSerializationException::class)
        abstract fun decode(data: ByteString): T
    }
}
