package ai.fd.mimi.sample.thinklet

import ai.fd.mimi.client.service.asr.MimiAsrInputLanguage
import ai.fd.mimi.client.service.asr.MimiAsrOptions
import ai.fd.mimi.client.service.asr.MimiAsrService
import ai.fd.mimi.client.service.asr.core.MimiAsrAudioFormat
import ai.fd.thinklet.sdk.audio.MultiChannelAudioRecord
import ai.fd.thinklet.sdk.audio.RawAudioRecordWrapper
import android.Manifest
import android.content.Context
import androidx.annotation.MainThread
import androidx.annotation.RequiresPermission
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * A sample code to run Mimi ASR with a recorded voice via HTTP API.
 */
class RecordedAsrSample(
    private val context: Context,
    private val lifecycleScope: CoroutineScope,
    private val asrService: MimiAsrService
) {
    private val rawRecorder = RawAudioRecordWrapper(
        sampleRate = MultiChannelAudioRecord.SampleRate.SAMPLING_RATE_16000,
        outputChannel = RawAudioRecordWrapper.RawAudioOutputChannel.MONO
    )

    private var currentDestination: File? = null

    private val callback: RawAudioRecordWrapper.IRawAudioRecorder = object : RawAudioRecordWrapper.IRawAudioRecorder {
        override fun onFailed(throwable: Throwable) {
            _latestResult.value = "Recording error: ${throwable.message}"
        }

        override fun onReceivedPcmData(pcmData: ByteArray) = Unit
    }

    private val _latestResult: MutableStateFlow<String?> = MutableStateFlow(null)
    val latestResult: StateFlow<String?>
        get() = _latestResult

    @MainThread
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startOrStop(): Boolean {
        if (currentDestination != null) {
            stopRecordingAndRequestAsr()
            return false
        } else {
            startRecording()
            return true
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun startRecording() {
        if (currentDestination != null) {
            return
        }
        if (!rawRecorder.prepare(context)) {
            return
        }
        val destination = File(
            context.getExternalFilesDir(null),
            "asr_${System.currentTimeMillis()}.raw"
        )
        rawRecorder.start(destination.outputStream(), callback)
        _latestResult.value = "Recording..."
        currentDestination = destination
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun stopRecordingAndRequestAsr() {
        rawRecorder.stop()
        val recordedFile = currentDestination
        currentDestination = null

        lifecycleScope.launch(Dispatchers.IO) {
            val bytes = recordedFile?.readBytes() ?: return@launch
            _latestResult.value = "Analyzing..."
            val result = asrService.requestAsr(
                bytes,
                MimiAsrOptions.DEFAULT.copy(
                    audioFormat = MimiAsrAudioFormat.PCM,
                    audioBitrate = 16,
                    audioSamplingRate = 16000,
                    inputLanguage = MimiAsrInputLanguage.JA
                )
            )
            if (result.isSuccess) {
                val asrResult = result.getOrThrow()
                _latestResult.value = asrResult.response.joinToString(" ") { it.result }
            } else {
                _latestResult.value = "Error: ${result.exceptionOrNull()?.message}"
            }
        }
    }
}
