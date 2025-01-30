package ai.fd.mimi.client.service.nict.tts

/**
 * Options for requesting the NICT TTS service.
 *
 * For more information on each option, refer to [the API reference](https://mimi.readme.io/reference/speech-synthesis).
 */
data class MimiNictTtsOptions(
    val language: Language,
    val audioFormat: AudioFormat,
    val audioEndian: AudioEndian,
    val gender: Gender,
    val rate: Float
) {
    /**
     * Language of the generated voice.
     */
    enum class Language(internal val value: String) {
        JA("ja"),
        EN("en"),
        ZH("zh"),
        ZH_TW("zh-TW"),
        KO("ko"),
        ES("es"),
        FIL("fil"),
        FR("fr"),
        ID("id"),
        MY("my"),
        PT_BR("pt-BR"),
        TH("th"),
        VI("vi"),
    }

    /**
     * Audio format of the generated audio binary.
     */
    enum class AudioFormat(internal val value: String) {
        WAV("WAV"),
        RAW("RAW"),
        ADPCM("ADPCM"),
        SPEEX("Speex"),
    }

    /**
     * Endian of the generated audio binary.
     */
    enum class AudioEndian(internal val value: String) {
        LITTLE("Little"),
        BIG("Big"),
    }

    /**
     * Gender of the generated voice.
     */
    enum class Gender(internal val value: String) {
        FEMALE("female"),
        MALE("male"),
        UNKNOWN("unknown"),
    }

    companion object {
        val DEFAULT = MimiNictTtsOptions(
            language = Language.JA,
            audioFormat = AudioFormat.WAV,
            audioEndian = AudioEndian.LITTLE,
            gender = Gender.UNKNOWN,
            rate = 1.0f
        )
    }
}
