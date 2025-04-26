package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.MimiIOException
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import ai.fd.mimi.client.service.asr.core.MimiAsrWebSocketSession
import androidx.annotation.VisibleForTesting
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.io.bytestring.ByteString
import ai.fd.mimi.client.service.nict.asr.MimiNictAsrServiceConst as Const

class MimiNictAsrV1Service internal constructor(
    @VisibleForTesting internal val path: String,
    @VisibleForTesting internal val engine: MimiNetworkEngine,
    @VisibleForTesting internal val accessToken: String,
    private val converter: MimiModelConverter.JsonString<MimiNictAsrV1Result>
) {
    constructor(
        engineFactory: MimiNetworkEngine.Factory,
        accessToken: String,
        useSsl: Boolean = true,
        host: String = "service.mimi.fd.ai",
        port: Int = if (useSsl) 443 else 80,
        path: String = "/"
    ) : this(
        path = path,
        engine = engineFactory.create(useSsl = useSsl, host = host, port = port),
        accessToken = accessToken,
        converter = MimiNictAsrV1ModelConverter()
    )

    suspend fun requestAsr(
        audioData: ByteArray,
        options: MimiNictAsrV1Options = MimiNictAsrV1Options.DEFAULT
    ): Result<MimiNictAsrV1Result> = engine.request(
        path = path,
        accessToken = accessToken,
        requestBody = MimiNetworkEngine.RequestBody.Binary(
            data = ByteString(audioData),
            contentType = options.toContentType()
        ),
        headers = mapOf(
            Const.HEADER_X_MIMI_PROCESS_KEY to Const.HEADER_X_MIMI_PROCESS_VALUE,
            Const.HEADER_X_MIMI_INPUT_LANGUAGE to options.inputLanguage.value
        ),
        converter = converter
    )

    @Throws(MimiIOException::class, CancellationException::class)
    suspend fun openAsrSession(
        options: MimiNictAsrV1Options = MimiNictAsrV1Options.DEFAULT
    ): MimiAsrWebSocketSession<MimiNictAsrV1Result> {
        val session = engine.openWebSocketSession(
            path = path,
            accessToken = accessToken,
            headers = mapOf(
                Const.HEADER_X_MIMI_PROCESS_KEY to Const.HEADER_X_MIMI_PROCESS_VALUE,
                Const.HEADER_X_MIMI_INPUT_LANGUAGE to options.inputLanguage.value
            ),
            contentType = options.toContentType(),
            converter = converter
        )
        return createMimiAsrWebSocketSession(session)
    }

    @VisibleForTesting
    internal fun createMimiAsrWebSocketSession(
        session: MimiWebSocketSessionInternal<MimiNictAsrV1Result>
    ): MimiAsrWebSocketSession<MimiNictAsrV1Result> = MimiAsrWebSocketSession(session)
}
