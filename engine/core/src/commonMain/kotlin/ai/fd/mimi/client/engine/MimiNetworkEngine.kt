package ai.fd.mimi.client.engine

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.MimiSerializationException
import androidx.annotation.VisibleForTesting
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.io.bytestring.ByteString

abstract class MimiNetworkEngine {

    suspend fun <T> request(
        path: String,
        accessToken: String?,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter<T>
    ): Result<T> = when (converter) {
        is MimiModelConverter.JsonString<T> -> requestJsonString(path, accessToken, requestBody, headers, converter)
        is MimiModelConverter.Binary<T> -> requestBinary(path, accessToken, requestBody, headers, converter)
    }

    private suspend fun <T> requestJsonString(
        path: String,
        accessToken: String?,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter.JsonString<T>
    ): Result<T> {
        val networkResult = requestAsStringInternal(path, requestBody, headers + createBearerHeader(accessToken))
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
        path: String,
        accessToken: String?,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter.Binary<T>
    ): Result<T> {
        val networkResult = requestAsBinaryInternal(path, requestBody, headers + createBearerHeader(accessToken))
        val networkException = networkResult.exceptionOrNull()
        if (networkException != null) {
            return Result.failure(networkException)
        }
        val binary = networkResult.getOrThrow()
        return try {
            Result.success(converter.decode(binary))
        } catch (e: MimiSerializationException) {
            Result.failure(e)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract suspend fun requestAsStringInternal(
        path: String,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap()
    ): Result<String>

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract suspend fun requestAsBinaryInternal(
        path: String,
        requestBody: RequestBody,
        headers: Map<String, String> = emptyMap()
    ): Result<ByteString>

    @Throws(MimiIOException::class, CancellationException::class)
    suspend fun <T> openWebSocketSession(
        path: String,
        accessToken: String?,
        contentType: String,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter.JsonString<T>
    ): MimiWebSocketSessionInternal<T> =
        openWebSocketSessionInternal(path, contentType, headers + createBearerHeader(accessToken), converter)

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Throws(MimiIOException::class, CancellationException::class)
    abstract suspend fun <T> openWebSocketSessionInternal(
        path: String,
        contentType: String,
        headers: Map<String, String> = emptyMap(),
        converter: MimiModelConverter.JsonString<T>
    ): MimiWebSocketSessionInternal<T>

    sealed interface RequestBody {
        data class Binary(val data: ByteString, val contentType: String) : RequestBody
        data class FormData(val fields: Map<String, String>) : RequestBody
    }

    interface Factory {
        fun create(useSsl: Boolean, host: String, port: Int): MimiNetworkEngine
    }

    companion object {
        private fun createBearerHeader(accessToken: String?): Map<String, String> {
            return if (accessToken != null) {
                mapOf("Authorization" to "Bearer $accessToken")
            } else {
                emptyMap()
            }
        }
    }
}
