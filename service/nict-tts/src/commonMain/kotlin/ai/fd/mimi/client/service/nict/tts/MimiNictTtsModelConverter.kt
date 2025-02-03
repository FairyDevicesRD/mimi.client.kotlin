package ai.fd.mimi.client.service.nict.tts

import ai.fd.mimi.client.engine.MimiModelConverter
import okio.ByteString.Companion.toByteString

/**
 * An implementation of [MimiModelConverter.Binary] for the NICT TTS service.
 */
internal class MimiNictTtsModelConverter : MimiModelConverter.Binary<MimiNictTtsResult>() {
    override fun decode(byteArray: ByteArray): MimiNictTtsResult =
        MimiNictTtsResult(audioBinary = byteArray.toByteString())
}
