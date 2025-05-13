package ai.fd.mimi.sample.thinklet

import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.okhttp.OkHttp
import ai.fd.mimi.client.service.asr.MimiAsrService
import ai.fd.mimi.client.service.nict.tts.MimiNictTtsService
import ai.fd.mimi.client.service.token.MimiTokenScopes
import ai.fd.mimi.client.service.token.MimiTokenService
import ai.fd.mimi.client.service.token.plus
import ai.fd.mimi.sample.thinklet.databinding.ActivityMainBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.KeyEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val vibrator: Vibrator by lazy(LazyThreadSafetyMode.NONE) {
        checkNotNull(getSystemService())
    }

    private val engineFactory: MimiNetworkEngine.Factory by lazy(LazyThreadSafetyMode.NONE) {
        val logging = HttpLoggingInterceptor { println(it.take(1000)) }
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .writeTimeout(10.minutes.toJavaDuration())
            .callTimeout(10.minutes.toJavaDuration())
            .readTimeout(10.minutes.toJavaDuration())
            .build()
        MimiNetworkEngine.OkHttp(client)
    }

    private val tokenService: MimiTokenService by lazy(LazyThreadSafetyMode.NONE) {
        MimiTokenService(engineFactory)
    }

    private var recordedAsrSample: RecordedAsrSample? = null
    private var realtimeAsrSample: RealtimeAsrSample? = null
    private var ttsSample: TtsSample? = null

    private var latestAsrResultText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                0
            )
        }

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            val token = tokenService.issueClientAccessToken(
                applicationId = BuildConfig.MIMI_APPLICATION_ID,
                clientId = BuildConfig.MIMI_CLIENT_ID,
                clientSecret = BuildConfig.MIMI_CLIENT_SECRET,
                scopes = MimiTokenScopes.Asr + MimiTokenScopes.Tts
            ).getOrNull() ?: return@launch
            initializeSamples(token.accessToken)
            binding.token.text = token.accessToken
        }
    }

    private fun initializeSamples(token: String) {
        val asrService = MimiAsrService(engineFactory, token)
        val ttsService = MimiNictTtsService(engineFactory, token)
        val recordedAsrSample = RecordedAsrSample(this, lifecycleScope, asrService)
        val realtimeAsrSample = RealtimeAsrSample(this, lifecycleScope, asrService)
        this.recordedAsrSample = recordedAsrSample
        this.realtimeAsrSample = realtimeAsrSample
        ttsSample = TtsSample(lifecycleScope, ttsService)
        lifecycleScope.launch {
            recordedAsrSample.latestResult.collect { result ->
                binding.recordAsr.text = result
            }
        }
        lifecycleScope.launch {
            realtimeAsrSample.latestResult.collect { result ->
                binding.realtimeAsr.text = result
            }
        }
        lifecycleScope.launch {
            merge(recordedAsrSample.latestResult, realtimeAsrSample.latestResult).collect {
                latestAsrResultText = it
            }
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                runRecordedAsr()
                return true
            }

            KeyEvent.KEYCODE_CAMERA -> {
                runRealtimeAsr()
                return true
            }

            KeyEvent.KEYCODE_VOLUME_UP -> {
                launchTts()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("MissingPermission")
    private fun runRecordedAsr() {
        val isStarted = recordedAsrSample?.startOrStop()
        if (isStarted == true) {
            vibrator.vibrate(createStaccatoVibrationEffect(1))
        } else {
            vibrator.vibrate(createStaccatoVibrationEffect(2))
        }
    }

    @SuppressLint("MissingPermission")
    private fun runRealtimeAsr() {
        val isStarted = realtimeAsrSample?.startOrStop()
        if (isStarted == true) {
            vibrator.vibrate(createStaccatoVibrationEffect(1))
        } else {
            vibrator.vibrate(createStaccatoVibrationEffect(2))
        }
    }

    private fun launchTts() {
        val ttsText = latestAsrResultText ?: DEFAULT_TTS_TEXT
        ttsSample?.runTts(ttsText)
    }

    private fun createStaccatoVibrationEffect(staccatoCount: Int): VibrationEffect {
        val timing = LongArray(staccatoCount * 2) { 200L }
        val amplitudes = IntArray(staccatoCount * 2) { i ->
            if (i % 2 == 0) 0 else DEFAULT_AMPLITUDE
        }
        return VibrationEffect.createWaveform(timing, amplitudes, -1)
    }

    companion object {
        private const val DEFAULT_TTS_TEXT = "吾輩は猫である。名前はまだ無い。"
    }
}
