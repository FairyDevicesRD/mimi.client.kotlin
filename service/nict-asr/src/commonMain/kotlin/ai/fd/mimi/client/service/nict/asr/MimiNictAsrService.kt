package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.MimiClient
import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.service.asr.core.MimiAsrOptions
import ai.fd.mimi.client.service.asr.core.MimiAsrWebSocketSession
import kotlin.coroutines.cancellation.CancellationException

class MimiNictAsrService internal constructor(
    private val mimiClient: MimiClient,
    private val accessToken: String,
    private val converterV1: MimiModelConverter<MimiNictAsrV1Result>,
    private val converterV2: MimiModelConverter<MimiNictAsrV2Result>
) {
    constructor(
        mimiClient: MimiClient,
        accessToken: String
    ) : this(
        mimiClient = mimiClient,
        accessToken = accessToken,
        converterV1 = MimiNictAsrV1ModelConverter(),
        converterV2 = MimiNictAsrV2ModelConverter()
    )

    suspend fun requestNictAsrV1(
        audioData: ByteArray,
        options: MimiAsrOptions = MimiAsrOptions.DEFAULT
    ): Result<MimiNictAsrV1Result> = mimiClient.request(
        accessToken = accessToken,
        byteArray = audioData,
        headers = mapOf(
            HEADER_X_MIMI_PROCESS_KEY to HEADER_X_MIMI_PROCESS_VALUE,
            HEADER_X_MIMI_INPUT_LANGUAGE to options.inputLanguage
        ),
        contentType = options.toContentType(),
        converter = converterV1
    )

    @Throws(MimiIOException::class, CancellationException::class)
    suspend fun openNictAsrV1Session(
        options: MimiAsrOptions = MimiAsrOptions.DEFAULT
    ): MimiAsrWebSocketSession<MimiNictAsrV1Result> {
        val session = mimiClient.openWebSocketSession(
            accessToken = accessToken,
            headers = mapOf(
                HEADER_X_MIMI_PROCESS_KEY to HEADER_X_MIMI_PROCESS_VALUE,
                HEADER_X_MIMI_INPUT_LANGUAGE to options.inputLanguage
            ),
            contentType = options.toContentType(),
            converter = converterV1
        )
        return MimiAsrWebSocketSession(session)
    }

    suspend fun requestNictAsrV2(
        audioData: ByteArray,
        options: MimiNictAsrV2Options = MimiNictAsrV2Options.DEFAULT
    ): Result<MimiNictAsrV2Result> {
        check(!options.progressive) { "Progressive mode is not supported for HTTP request" }
        return mimiClient.request(
            accessToken = accessToken,
            byteArray = audioData,
            headers = mapOf(
                HEADER_X_MIMI_PROCESS_KEY to HEADER_X_MIMI_PROCESS_VALUE,
                HEADER_X_MIMI_INPUT_LANGUAGE to options.coreOption.inputLanguage,
                HEADER_X_MIMI_NICT_ASR_OPTIONS to options.toNictAsrOptions()
            ),
            contentType = options.coreOption.toContentType(),
            converter = converterV2
        )
    }

    @Throws(MimiIOException::class, CancellationException::class)
    suspend fun openNictAsrV2Session(
        options: MimiNictAsrV2Options = MimiNictAsrV2Options.DEFAULT
    ): MimiAsrWebSocketSession<MimiNictAsrV2Result> {
        val session = mimiClient.openWebSocketSession(
            accessToken = accessToken,
            headers = mapOf(
                HEADER_X_MIMI_PROCESS_KEY to HEADER_X_MIMI_PROCESS_VALUE,
                HEADER_X_MIMI_INPUT_LANGUAGE to options.coreOption.inputLanguage,
                HEADER_X_MIMI_NICT_ASR_OPTIONS to options.toNictAsrOptions()
            ),
            contentType = options.coreOption.toContentType(),
            converter = converterV2
        )
        return MimiAsrWebSocketSession(session)
    }

    private companion object {
        const val HEADER_X_MIMI_PROCESS_KEY = "x-mimi-process"
        const val HEADER_X_MIMI_PROCESS_VALUE = "nict-asr"
        const val HEADER_X_MIMI_INPUT_LANGUAGE = "x-mimi-input-language"
        const val HEADER_X_MIMI_NICT_ASR_OPTIONS = "x-mimi-nict-asr-options"
    }
}
