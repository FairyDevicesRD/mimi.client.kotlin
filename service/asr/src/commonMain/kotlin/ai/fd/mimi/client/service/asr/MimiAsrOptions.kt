package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.service.asr.core.MimiAsrAudioFormat
import ai.fd.mimi.client.service.asr.core.internal.MimiAsrContentTypeFormatter
import ai.fd.mimi.client.service.asr.core.internal.MimiAsrOptionDefaults

data class MimiAsrOptions internal constructor(
    val audioFormat: MimiAsrAudioFormat,
    val audioBitrate: Int,
    val audioSamplingRate: Int,
    val inputLanguage: MimiAsrInputLanguage
) {

    internal fun toContentType(): String = MimiAsrContentTypeFormatter.toContentTypeString(
        audioFormat = audioFormat,
        audioBitrate = audioBitrate,
        audioSamplingRate = audioSamplingRate
    )

    companion object {
        val DEFAULT = MimiAsrOptions(
            audioFormat = MimiAsrOptionDefaults.DEFAULT_AUDIO_FORMAT,
            audioBitrate = MimiAsrOptionDefaults.DEFAULT_AUDIO_BITRATE,
            audioSamplingRate = MimiAsrOptionDefaults.DEFAULT_AUDIO_SAMPLING_RATE,
            inputLanguage = MimiAsrInputLanguage.JA
        )
    }
}
