import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.service.asr.MimiAsrService
import ai.fd.mimi.client.service.asr.core.MimiAsrWebSocketSession
import ai.fd.mimi.client.service.nict.asr.MimiNictAsrService
import io.ktor.util.toLowerCasePreservingASCIIRules
import java.io.File
import java.util.Properties
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okio.ByteString.Companion.toByteString

private fun loadLocalProperties(): Properties = Properties()
    .apply {
        File("local.properties").inputStream().use {
            load(it)
        }
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
    val asrService = MimiNictAsrService(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    val data = ClassLoader.getSystemResource("audio.raw").readBytes()

    val result = asrService.requestNictAsrV1(data)
    println(result)
}

suspend fun runWebSocketNictV1Asr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiNictAsrService(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    println("Start connecting")
    val session = asrService.openNictAsrV1Session()
    testAsrWebSocket(session)
}

suspend fun runNictV2Asr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiNictAsrService(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    val data = ClassLoader.getSystemResource("audio.raw").readBytes()

    val result = asrService.requestNictAsrV2(data)
    println(result)
}

suspend fun runWebSocketNictV2Asr(engineFactory: MimiNetworkEngine.Factory) {
    val asrService = MimiNictAsrService(
        engineFactory = engineFactory,
        accessToken = loadToken()
    )
    println("Start connecting")
    val session = asrService.openNictAsrV2Session()
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
        val first = data.sliceArray(0 until data.size / 2).toByteString()
        val second = data.sliceArray(data.size / 2 until data.size).toByteString()
        println("Sent first $first")
        session.sendBinary(first)
        println("Sent second $second")
        session.sendBinary(second)
        println("send recog finish")
        session.stopRecognition()
        job.join()
    }
}

enum class AsrType {
    ASR,
    NICT_V1,
    NICT_V2
}
