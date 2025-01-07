package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.service.asr.core.MimiAsrOptions
import ai.fd.mimi.client.service.asr.core.MimiAsrOptions.MimiAsrAudioFormat

data class MimiNictAsrV2Options internal constructor(
    val coreOption: MimiAsrOptions,
    val progressive: Boolean,
    val temporary: Boolean,
    val temporaryInterval: Int
) {

    constructor(
        audioFormat: MimiAsrAudioFormat = DEFAULT.coreOption.audioFormat,
        audioBitrate: Int = DEFAULT.coreOption.audioBitrate,
        audioSamplingRate: Int = DEFAULT.coreOption.audioSamplingRate,
        inputLanguage: String = DEFAULT.coreOption.inputLanguage,
        progressive: Boolean = DEFAULT.progressive,
        temporary: Boolean = DEFAULT.temporary,
        temporaryInterval: Int = DEFAULT.temporaryInterval
    ) : this(
        coreOption = MimiAsrOptions(
            audioFormat = audioFormat,
            audioBitrate = audioBitrate,
            audioSamplingRate = audioSamplingRate,
            inputLanguage = inputLanguage
        ),
        progressive = progressive,
        temporary = temporary,
        temporaryInterval = temporaryInterval
    )

    fun toNictAsrOptions(): String =
        "response_format=v2;progressive=$progressive;temporary=$temporary;temporary_interval=$temporaryInterval"

    companion object {
        val DEFAULT = MimiNictAsrV2Options(
            coreOption = MimiAsrOptions.DEFAULT,
            progressive = false,
            temporary = true,
            temporaryInterval = 1500
        )
    }
}
