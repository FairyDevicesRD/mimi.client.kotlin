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
import java.io.OutputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * A sample code to run Mimi ASR with a realtime translation via WebSocket API.
 */
class RealtimeAsrSample(
    private val context: Context,
    private val lifecycleScope: CoroutineScope,
    private val asrService: MimiAsrService
) {
    private val rawRecorder = RawAudioRecordWrapper(
        sampleRate = MultiChannelAudioRecord.SampleRate.SAMPLING_RATE_16000,
        outputChannel = RawAudioRecordWrapper.RawAudioOutputChannel.MONO
    )

    private var currentJob: Job? = null

    private val _latestResult: MutableStateFlow<String?> = MutableStateFlow(null)
    val latestResult: StateFlow<String?>
        get() = _latestResult

    @MainThread
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startOrStop(): Boolean {
        if (currentJob?.isActive == true) {
            stopRecordingAndRequestAsr()
            return false
        } else {
            start()
            return true
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun start() {
        if (currentJob?.isActive == true) {
            return
        }
        if (!rawRecorder.prepare(context)) {
            return
        }
        currentJob = lifecycleScope.launch(Dispatchers.IO) {
            val session = asrService.openAsrSession(
                MimiAsrOptions.DEFAULT.copy(
                    audioFormat = MimiAsrAudioFormat.PCM,
                    audioBitrate = 16,
                    audioSamplingRate = 16000,
                    inputLanguage = MimiAsrInputLanguage.JA
                )
            )
            launch {
                session.rxFlow.collect {
                    _latestResult.value = it.response.joinToString(" ") { it.result }
                }
            }
            val callback: RawAudioRecordWrapper.IRawAudioRecorder = object : RawAudioRecordWrapper.IRawAudioRecorder {
                override fun onFailed(throwable: Throwable) {
                    _latestResult.value = "Recording error: ${throwable.message}"
                }

                override fun onReceivedPcmData(pcmData: ByteArray) {
                    if (pcmData.isEmpty()) {
                        return
                    }
                    session.sendBinaryBlocking(pcmData)
                }
            }
            rawRecorder.start(NullOutputStream, callback)
            try {
                awaitCancellation()
            } finally {
                rawRecorder.stop()
                session.stopRecognition()
            }
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun stopRecordingAndRequestAsr() {
        currentJob?.cancel()
    }

    private object NullOutputStream : OutputStream() {
        override fun write(b: Int) = Unit
    }
}
