package ai.fd.mimi.client.service.nict.tts

import okio.ByteString

/**
 * A result of the NICT TTS service.
 */
data class MimiNictTtsResult(
    val audioBinary: ByteString
)
