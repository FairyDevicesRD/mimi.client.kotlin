package ai.fd.mimi.client.service.asr

data class MimiAsrOptions(
    val audioFormat: MimiAsrAudioFormat,
    val audioBitrate: Int,
    val audioSamplingRate: Int,
    val audioChannelCount: Int,
    val inputLanguage: String
) {
    enum class MimiAsrAudioFormat {
        PCM,
        FLAC
    }

    internal fun toContentType(): String {
        val audioFormatText = when (audioFormat) {
            MimiAsrAudioFormat.PCM -> "x-pcm"
            MimiAsrAudioFormat.FLAC -> "x-flac"
        }
        return "audio/$audioFormatText;bit=$audioBitrate;rate=$audioSamplingRate;channels=$audioChannelCount"
    }

    companion object {
        val DEFAULT = MimiAsrOptions(
            audioFormat = MimiAsrAudioFormat.PCM,
            audioBitrate = 16,
            audioSamplingRate = 16000,
            audioChannelCount = 1,
            inputLanguage = "ja"
        )
    }
}
