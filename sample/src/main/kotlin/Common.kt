import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.service.asr.MimiAsrService
import ai.fd.mimi.client.service.asr.core.MimiAsrWebSocketSession
import ai.fd.mimi.client.service.nict.asr.MimiNictAsrV1Service
import ai.fd.mimi.client.service.nict.asr.MimiNictAsrV2Service
import ai.fd.mimi.client.service.nict.tts.MimiNictTtsService
import ai.fd.mimi.client.service.token.MimiTokenScope
import ai.fd.mimi.client.service.token.MimiTokenService
import io.ktor.util.toLowerCasePreservingASCIIRules
import java.io.File
import java.text.SimpleDateFormat
import java.util.Properties
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okio.use

private fun loadLocalProperties(): Properties = Properties()
    .apply {
        getLocalPropertyFile().inputStream().use {
            load(it)
        }
    }

private fun getLocalPropertyFile(): File {
    val file = File("local.properties")
    if (file.exists()) {
        return file
    }
    val fileInParent = File("../local.properties")
    if (fileInParent.exists()) {
        return fileInParent
    }
    throw IllegalArgumentException("local.properties not found")
}

suspend fun issueClientAccessToken(engineFactory: MimiNetworkEngine.Factory, scopes: Set<MimiTokenScope>) {
    val applicationId = System.getenv("MIMI_APPLICATION_ID") ?: loadLocalProperties().getProperty("MIMI_APPLICATION_ID")
    val clientId = System.getenv("MIMI_CLIENT_ID") ?: loadLocalProperties().getProperty("MIMI_CLIENT_ID")
    val clientSecret = System.getenv("MIMI_CLIENT_SECRET") ?: loadLocalProperties().getProperty("MIMI_CLIENT_SECRET")
    val tokenService = MimiTokenService(engineFactory = engineFactory)
    val result = tokenService.issueClientAccessToken(
        applicationId = applicationId,
        clientId = clientId,
        clientSecret = clientSecret,
        scopes = scopes
    )

    if (result.isFailure) {
        println("Failed to issue token.")
        result.exceptionOrNull()?.printStackTrace()
        return
    }
    val token = result.getOrThrow().accessToken
    println("Token issued: $token")

    val validateResult = tokenService.validateAccessToken(token)
    println("Validate result: $validateResult")

    if (validateResult.isFailure) {
        println("Failed to validate token.")
        validateResult.exceptionOrNull()?.printStackTrace()
        return
    }

    tokenService.revokeClientAccessToken(
        applicationId = applicationId,
        clientId = clientId,
        clientSecret = clientSecret,
        token = token
    )
}


private fun loadToken(): String {
    val token = System.getenv("MIMI_TOKEN") ?: loadLocalProperties().getProperty("MIMI_TOKEN")
    ?: throw IllegalArgumentException("MIMI_TOKEN is required in environment variables or local.properties")
    return token
}

private fun loadAsrType(): AsrType {
    val asrType = System.getenv("ASR_TYPE") ?: loadLocalProperties().getProperty("ASR_TYPE")
    return when (asrType?.toLowerCasePreservingASCIIRules()) {
        "asr" -> AsrType.ASR
        "nict-v1" -> AsrType.NICT_V1
        "nict-v2" -> AsrType.NICT_V2
        else -> AsrType.ASR
    }
}

suspend fun runAsr(engineFactory: MimiNetworkEngine.Factory) = when (loadAsrType()) {
    AsrType.ASR -> runNormalAsr(engineFactory)
    AsrType.NICT_V1 -> runNictV1Asr(engineFactory)
    AsrType.NICT_V2 -> runNictV2Asr(engineFactory)
}

suspend fun runWebSocketAsr(engineFactory: MimiNetworkEngine.Factory) = when (loadAsrType()) {
    AsrType.ASR -> runWebSocketNormalAsr(engineFactory)
    AsrType.NICT_V1 -> runWebSocketNictV1Asr(engineFactory)
    AsrType.NICT_V2 -> runWebSocketNictV2Asr(engineFactory)
}

suspend fun runNormalAsr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiAsrService(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    val data = ClassLoader.getSystemResource("audio.raw").readBytes()

    val result = asrService.requestAsr(data)
    println(result)
}

suspend fun runWebSocketNormalAsr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiAsrService(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    println("Start connecting")
    val session = asrService.openAsrSession()
    testAsrWebSocket(session)
}

suspend fun runNictV1Asr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiNictAsrV1Service(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    val data = ClassLoader.getSystemResource("audio.raw").readBytes()

    val result = asrService.requestAsr(data)
    println(result)
}

suspend fun runWebSocketNictV1Asr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiNictAsrV1Service(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    println("Start connecting")
    val session = asrService.openAsrSession()
    testAsrWebSocket(session)
}

suspend fun runNictV2Asr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiNictAsrV2Service(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    val data = ClassLoader.getSystemResource("audio.raw").readBytes()

    val result = asrService.requestAsr(data)
    println(result)
}

suspend fun runWebSocketNictV2Asr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiNictAsrV2Service(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    println("Start connecting")
    val session = asrService.openAsrSession()
    testAsrWebSocket(session)
}

suspend fun testAsrWebSocket(session: MimiAsrWebSocketSession<*>) {
    val data = ClassLoader.getSystemResource("audio.raw").readBytes()
    coroutineScope {
        val job = launch {
            session.rxFlow.collect {
                println(it)
            }
            println("collect finished")
        }
        val first = data.sliceArray(0 until data.size / 2)
        val second = data.sliceArray(data.size / 2 until data.size)
        println("Sent first $first")
        session.sendBinary(first)
        println("Sent second $second")
        session.sendBinary(second)
        println("send recog finish")
        session.stopRecognition()
        job.join()
    }
}

suspend fun runTts(engineFactory: MimiNetworkEngine.Factory) {
    val ttsService = MimiNictTtsService(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    val result = ttsService.requestTts(
        "吾輩は猫である。名前はまだ無い。どこで生れたかとんと見当がつかぬ。" +
                "何でも薄暗いじめじめした所でニャーニャー泣いていた事だけは記憶している。"
    )
    result.onSuccess {
        println("Success to run TTS. bytes: ${it.audioBinary.size}")
        val currentTimeStr = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis())
        val file = File("${currentTimeStr}.wav")
        file.writeBytes(it.audioBinary)
        println("Saved to ${file.absolutePath}")
    }.onFailure {
        println("Failed to run TTS.")
        it.printStackTrace()
    }
}

enum class AsrType {
    ASR,
    NICT_V1,
    NICT_V2
}
