package ai.fd.mimi.client.service.nict.tts

import ai.fd.mimi.client.engine.MimiModelConverter
import kotlinx.io.bytestring.ByteString

/**
 * An implementation of [MimiModelConverter.Binary] for the NICT TTS service.
 */
internal class MimiNictTtsModelConverter : MimiModelConverter.Binary<MimiNictTtsResult>() {
    override fun decode(data: ByteString): MimiNictTtsResult =
        MimiNictTtsResult(audioBinary = data.toByteArray())
}
