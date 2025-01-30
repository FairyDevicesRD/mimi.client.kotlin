package ai.fd.mimi.client.engine

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.MimiJsonException
import kotlin.coroutines.cancellation.CancellationException

abstract class MimiNetworkEngine {

    suspend fun <T> request(
        accessToken: String,
        byteArray: ByteArray,
        contentType: String,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter<T>
    ): Result<T> {
        val networkResult = requestInternal(accessToken, byteArray, contentType, headers)
        val networkException = networkResult.exceptionOrNull()
        if (networkException != null) {
            return Result.failure(networkException)
        }
        val text = networkResult.getOrThrow()
        return try {
            Result.success(converter.decode(text))
        } catch (e: MimiJsonException) {
            Result.failure(e)
        }
    }

    protected abstract suspend fun requestInternal(
        accessToken: String,
        byteArray: ByteArray,
        contentType: String,
        headers: Map<String, String> = emptyMap()
    ): Result<String>

    @Throws(MimiIOException::class, CancellationException::class)
    abstract suspend fun <T> openWebSocketSession(
        accessToken: String,
        contentType: String,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter<T>
    ): MimiWebSocketSessionInternal<T>

    interface Factory {
        fun create(useSsl: Boolean, host: String, port: Int): MimiNetworkEngine
    }

    companion object
}
