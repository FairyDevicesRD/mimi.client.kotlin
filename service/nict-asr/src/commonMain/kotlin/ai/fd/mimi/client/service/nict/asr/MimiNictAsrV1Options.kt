package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.service.asr.core.MimiAsrAudioFormat
import ai.fd.mimi.client.service.asr.core.internal.MimiAsrContentTypeFormatter
import ai.fd.mimi.client.service.asr.core.internal.MimiAsrOptionDefaults

@ExposedCopyVisibility
data class MimiNictAsrV1Options internal constructor(
    val audioFormat: MimiAsrAudioFormat,
    val audioBitrate: Int,
    val audioSamplingRate: Int,
    val inputLanguage: MimiNictAsrInputLanguage
) {

    internal fun toContentType(): String = MimiAsrContentTypeFormatter.toContentTypeString(
        audioFormat = audioFormat,
        audioBitrate = audioBitrate,
        audioSamplingRate = audioSamplingRate
    )

    companion object {
        val DEFAULT = MimiNictAsrV1Options(
            audioFormat = MimiAsrOptionDefaults.DEFAULT_AUDIO_FORMAT,
            audioBitrate = MimiAsrOptionDefaults.DEFAULT_AUDIO_BITRATE,
            audioSamplingRate = MimiAsrOptionDefaults.DEFAULT_AUDIO_SAMPLING_RATE,
            inputLanguage = MimiNictAsrInputLanguage.JA
        )
    }
}
