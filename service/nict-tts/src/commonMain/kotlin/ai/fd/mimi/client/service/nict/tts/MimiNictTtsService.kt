package ai.fd.mimi.client.service.nict.tts

import ai.fd.mimi.client.annotation.ExperimentalMimiApi
import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import androidx.annotation.VisibleForTesting
import ai.fd.mimi.client.service.nict.tts.MimiNictTtsServiceConst as Const

/**
 * A service class to interact with [the NICT TTS service](https://mimi.readme.io/docs/tts-http-service).
 */
@OptIn(ExperimentalMimiApi::class)
class MimiNictTtsService internal constructor(
    @VisibleForTesting internal val path: String,
    private val engine: MimiNetworkEngine,
    private val accessToken: String,
    private val converter: MimiModelConverter<MimiNictTtsResult>
) {
    constructor(
        engineFactory: MimiNetworkEngine.Factory,
        accessToken: String,
        useSsl: Boolean = true,
        host: String = "tts.mimi.fd.ai",
        path: String = "speech_synthesis",
        port: Int = if (useSsl) 443 else 80
    ) : this(
        path = path,
        engine = engineFactory.create(useSsl = useSsl, host = host, port = port),
        accessToken = accessToken,
        converter = MimiNictTtsModelConverter()
    )

    /**
     * Generates a voice reading the specified [text] by the NICT TTS service.
     */
    suspend fun requestTts(
        text: String,
        options: MimiNictTtsOptions = MimiNictTtsOptions.DEFAULT
    ): Result<MimiNictTtsResult> {
        require(options.rate in 0.5f..2.0f) { "`rate` should be in 0.5~2.0." }
        return engine.request(
            path = path,
            accessToken = accessToken,
            requestBody = MimiNetworkEngine.RequestBody.FormData(
                fields = mapOf(
                    Const.FORM_TEXT_KEY to text,
                    Const.FORM_ENGINE_KEY to Const.FORM_ENGINE_NICT_VALUE,
                    Const.FORM_LANG_KEY to options.language.value,
                    Const.FORM_AUDIO_FORMAT_KEY to options.audioFormat.value,
                    Const.FORM_AUDIO_ENDIAN_KEY to options.audioEndian.value,
                    Const.FORM_GENDER_KEY to options.gender.value,
                    Const.FORM_RATE_KEY to options.rate.toString()
                )
            ),
            converter = converter
        )
    }
}
