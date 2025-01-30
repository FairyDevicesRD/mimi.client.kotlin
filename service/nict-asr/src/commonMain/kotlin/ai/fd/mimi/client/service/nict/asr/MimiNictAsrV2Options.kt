package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.service.asr.core.MimiAsrAudioFormat
import ai.fd.mimi.client.service.asr.core.internal.MimiAsrContentTypeFormatter
import ai.fd.mimi.client.service.asr.core.internal.MimiAsrOptionDefaults

data class MimiNictAsrV2Options(
    val audioFormat: MimiAsrAudioFormat,
    val audioBitrate: Int,
    val audioSamplingRate: Int,
    val inputLanguage: MimiNictAsrInputLanguage,
    val progressive: Boolean,
    val temporary: Boolean,
    val temporaryInterval: Int
) {

    internal fun toContentType(): String = MimiAsrContentTypeFormatter.toContentTypeString(
        audioFormat = audioFormat,
        audioBitrate = audioBitrate,
        audioSamplingRate = audioSamplingRate
    )

    internal fun toNictAsrOptions(): String =
        "response_format=v2;progressive=$progressive;temporary=$temporary;temporary_interval=$temporaryInterval"

    companion object {
        val DEFAULT = MimiNictAsrV2Options(
            audioFormat = MimiAsrOptionDefaults.DEFAULT_AUDIO_FORMAT,
            audioBitrate = MimiAsrOptionDefaults.DEFAULT_AUDIO_BITRATE,
            audioSamplingRate = MimiAsrOptionDefaults.DEFAULT_AUDIO_SAMPLING_RATE,
            inputLanguage = MimiNictAsrInputLanguage.JA,
            progressive = false,
            temporary = true,
            temporaryInterval = 1500
        )
    }
}
