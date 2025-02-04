package ai.fd.mimi.client.engine

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.MimiJsonException
import kotlin.coroutines.cancellation.CancellationException
import okio.ByteString

abstract class MimiNetworkEngine {

    suspend fun <T> request(
        accessToken: String,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter<T>
    ): Result<T> = when (converter) {
        is MimiModelConverter.JsonString<T> -> requestJsonString(accessToken, requestBody, headers, converter)
        is MimiModelConverter.Binary<T> -> requestBinary(accessToken, requestBody, headers, converter)
    }

    private suspend fun <T> requestJsonString(
        accessToken: String,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter.JsonString<T>
    ): Result<T> {
        val networkResult = requestAsStringInternal(accessToken, requestBody, headers)
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

    private suspend fun <T> requestBinary(
        accessToken: String,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter.Binary<T>
    ): Result<T> {
        val networkResult = requestAsBinaryInternal(accessToken, requestBody, headers)
        val networkException = networkResult.exceptionOrNull()
        if (networkException != null) {
            return Result.failure(networkException)
        }
        val binary = networkResult.getOrThrow()
        return try {
            Result.success(converter.decode(binary))
        } catch (e: MimiIOException) {
            Result.failure(e)
        }
    }

    protected abstract suspend fun requestAsStringInternal(
        accessToken: String,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap()
    ): Result<String>

    protected abstract suspend fun requestAsBinaryInternal(
        accessToken: String,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap()
    ): Result<ByteString>

    @Throws(MimiIOException::class, CancellationException::class)
    abstract suspend fun <T> openWebSocketSession(
        accessToken: String,
        contentType: String,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter.JsonString<T>
    ): MimiWebSocketSessionInternal<T>

    sealed interface RequestBody {
        data class Binary(val data: ByteString, val contentType: String) : RequestBody
        data class FormData(val fields: Map<String, String>) : RequestBody
    }

    interface Factory {
        fun create(useSsl: Boolean, host: String, port: Int, path: String = "/"): MimiNetworkEngine
    }

    companion object
}
