package ai.fd.mimi.client.service.asr.core

data class MimiAsrOptions(
    val audioFormat: MimiAsrAudioFormat,
    val audioBitrate: Int,
    val audioSamplingRate: Int,
    val inputLanguage: String
) {
    enum class MimiAsrAudioFormat {
        PCM,
        FLAC
    }

    fun toContentType(): String {
        val audioFormatText = when (audioFormat) {
            MimiAsrAudioFormat.PCM -> "x-pcm"
            MimiAsrAudioFormat.FLAC -> "x-flac"
        }
        return "audio/$audioFormatText;bit=$audioBitrate;rate=$audioSamplingRate;channels=1"
    }

    companion object {
        val DEFAULT = MimiAsrOptions(
            audioFormat = MimiAsrAudioFormat.PCM,
            audioBitrate = 16,
            audioSamplingRate = 16000,
            inputLanguage = "ja"
        )
    }
}
