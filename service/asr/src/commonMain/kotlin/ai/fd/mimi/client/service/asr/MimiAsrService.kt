package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.MimiClient
import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.service.asr.core.MimiAsrOptions
import ai.fd.mimi.client.service.asr.core.MimiAsrWebSocketSession
import kotlin.coroutines.cancellation.CancellationException

class MimiAsrService internal constructor(
    private val mimiClient: MimiClient,
    private val accessToken: String,
    private val converter: MimiModelConverter<MimiAsrResult>
) {
    constructor(
        mimiClient: MimiClient,
        accessToken: String
    ) : this(
        mimiClient = mimiClient,
        accessToken = accessToken,
        converter = MimiAsrModelConverter()
    )

    suspend fun requestAsr(
        audioData: ByteArray,
        options: MimiAsrOptions = MimiAsrOptions.DEFAULT
    ): Result<MimiAsrResult> = mimiClient.request(
        accessToken = accessToken,
        byteArray = audioData,
        headers = mapOf(
            HEADER_X_MIMI_PROCESS_KEY to HEADER_X_MIMI_PROCESS_VALUE,
            HEADER_X_MIMI_INPUT_LANGUAGE to options.inputLanguage
        ),
        contentType = options.toContentType(),
        converter = converter
    )

    @Throws(MimiIOException::class, CancellationException::class)
    suspend fun openAsrSession(
        options: MimiAsrOptions = MimiAsrOptions.DEFAULT
    ): MimiAsrWebSocketSession<MimiAsrResult> {
        val session = mimiClient.openWebSocketSession(
            accessToken = accessToken,
            headers = mapOf(
                HEADER_X_MIMI_PROCESS_KEY to HEADER_X_MIMI_PROCESS_VALUE,
                HEADER_X_MIMI_INPUT_LANGUAGE to options.inputLanguage
            ),
            contentType = options.toContentType(),
            converter = converter
        )
        return MimiAsrWebSocketSession(session)
    }

    private companion object {
        const val HEADER_X_MIMI_PROCESS_KEY = "x-mimi-process"
        const val HEADER_X_MIMI_PROCESS_VALUE = "asr"
        const val HEADER_X_MIMI_INPUT_LANGUAGE = "x-mimi-input-language"
    }
}
