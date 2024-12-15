import ai.fd.mimi.client.MimiClient
import ai.fd.mimi.client.service.asr.MimiAsrService
import java.io.File
import java.util.Properties
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okio.ByteString.Companion.toByteString

private fun loadToken(): String {
    val token = System.getenv("MIMI_TOKEN")
        ?: Properties()
            .apply {
                File("local.properties").inputStream().use {
                    load(it)
                }
            }
            .getProperty("MIMI_TOKEN")
    return token
}

suspend fun runAsr(mimiClient: MimiClient) {
    val asrService = MimiAsrService(
        mimiClient = mimiClient,
        accessToken = loadToken()
    )
    val data = ClassLoader.getSystemResource("audio.raw").readBytes()

    val result = asrService.requestAsr(data)
    println(result)
}

suspend fun runWebSocketAsr(mimiClient: MimiClient) {
    val asrService = MimiAsrService(
        mimiClient = mimiClient,
        accessToken = loadToken()
    )
    val data = ClassLoader.getSystemResource("audio.raw").readBytes()

    println("Start connecting")
    val session = asrService.openAsrSession()
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
