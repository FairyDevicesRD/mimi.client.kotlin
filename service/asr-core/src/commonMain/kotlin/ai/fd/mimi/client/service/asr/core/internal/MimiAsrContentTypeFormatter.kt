package ai.fd.mimi.client.service.asr.core.internal

import ai.fd.mimi.client.service.asr.core.MimiAsrAudioFormat

object MimiAsrContentTypeFormatter {
    fun toContentTypeString(
        audioFormat: MimiAsrAudioFormat,
        audioBitrate: Int,
        audioSamplingRate: Int,
    ): String {
        val audioFormatText = when (audioFormat) {
            MimiAsrAudioFormat.PCM -> "x-pcm"
            MimiAsrAudioFormat.FLAC -> "x-flac"
        }
        return "audio/$audioFormatText;bit=$audioBitrate;rate=$audioSamplingRate;channels=1"
    }
}
