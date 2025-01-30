package ai.fd.mimi.client.service.asr.core.internal

import ai.fd.mimi.client.service.asr.core.MimiAsrAudioFormat

object MimiAsrOptionDefaults {
    val DEFAULT_AUDIO_FORMAT = MimiAsrAudioFormat.PCM
    const val DEFAULT_AUDIO_BITRATE = 16
    const val DEFAULT_AUDIO_SAMPLING_RATE = 16000
}
