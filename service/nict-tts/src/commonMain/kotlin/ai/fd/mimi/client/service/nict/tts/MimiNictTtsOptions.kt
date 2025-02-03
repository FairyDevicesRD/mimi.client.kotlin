package ai.fd.mimi.client.service.nict.tts

import ai.fd.mimi.client.annotation.ExperimentalMimiApi
import androidx.annotation.FloatRange

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
    // Note: Currently, annotations on data class properties do not raise warnings when they are used in constructors or
    // copy.
    // According to KEEP, there are no plans to apply RequiresOptIn to parameters, but we will use it for clarity.
    // https://github.com/Kotlin/KEEP/blob/master/proposals/opt-in.md
    @property:ExperimentalMimiApi
    @FloatRange(from = 0.5, to = 2.0)
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
