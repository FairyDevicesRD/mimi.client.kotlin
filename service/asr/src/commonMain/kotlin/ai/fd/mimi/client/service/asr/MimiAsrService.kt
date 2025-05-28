package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import ai.fd.mimi.client.service.asr.core.MimiAsrWebSocketSession
import androidx.annotation.VisibleForTesting
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.io.bytestring.ByteString

class MimiAsrService internal constructor(
    @VisibleForTesting internal val path: String,
    @VisibleForTesting internal val engine: MimiNetworkEngine,
    @VisibleForTesting internal val accessToken: String,
    private val converter: MimiModelConverter.EncodableJsonString<MimiAsrResult>
) {
    constructor(
        engineFactory: MimiNetworkEngine.Factory,
        accessToken: String,
        useSsl: Boolean = true,
        host: String = "service.mimi.fd.ai",
        port: Int = if (useSsl) 443 else 80,
        path: String = "",
    ) : this(
        path = path,
        engine = engineFactory.create(useSsl = useSsl, host = host, port = port),
        accessToken = accessToken,
        converter = MimiAsrModelConverter()
    )

    suspend fun requestAsr(
        audioData: ByteArray,
        options: MimiAsrOptions = MimiAsrOptions.DEFAULT
    ): Result<MimiAsrResult> = engine.request(
        path = path,
        accessToken = accessToken,
        requestBody = MimiNetworkEngine.RequestBody.Binary(
            data = ByteString(audioData),
            contentType = options.toContentType()
        ),
        headers = mapOf(
            HEADER_X_MIMI_PROCESS_KEY to HEADER_X_MIMI_PROCESS_VALUE,
            HEADER_X_MIMI_INPUT_LANGUAGE to options.inputLanguage.value
        ),
        converter = converter
    )

    @Throws(MimiIOException::class, CancellationException::class)
    suspend fun openAsrSession(
        options: MimiAsrOptions = MimiAsrOptions.DEFAULT
    ): MimiAsrWebSocketSession<MimiAsrResult> {
        val session = engine.openWebSocketSession(
            path = path,
            accessToken = accessToken,
            headers = mapOf(
                HEADER_X_MIMI_PROCESS_KEY to HEADER_X_MIMI_PROCESS_VALUE,
                HEADER_X_MIMI_INPUT_LANGUAGE to options.inputLanguage.value
            ),
            contentType = options.toContentType(),
            converter = converter
        )
        return createMimiAsrWebSocketSession(session)
    }

    @VisibleForTesting
    internal fun createMimiAsrWebSocketSession(
        session: MimiWebSocketSessionInternal<MimiAsrResult>
    ): MimiAsrWebSocketSession<MimiAsrResult> = MimiAsrWebSocketSession(session)

    private companion object {
        const val HEADER_X_MIMI_PROCESS_KEY = "x-mimi-process"
        const val HEADER_X_MIMI_PROCESS_VALUE = "asr"
        const val HEADER_X_MIMI_INPUT_LANGUAGE = "x-mimi-input-language"
    }
}
