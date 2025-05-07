package ai.fd.mimi.sample.thinklet

import ai.fd.mimi.client.service.nict.tts.MimiNictTtsService
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A sample code to run Mimi TTS service.
 */
class TtsSample(
    private val lifecycleScope: CoroutineScope,
    private val ttsService: MimiNictTtsService
) {
    fun runTts(text: String) {
        lifecycleScope.launch {
            val result = ttsService.requestTts(text)
            val data = result.getOrNull()?.audioBinary ?: return@launch

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(16000)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(data.size)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()
            audioTrack.write(data, 0, data.size)
            audioTrack.play()
        }
    }
}
