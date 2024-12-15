import ai.fd.mimi.client.MimiClient
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.okhttp.OkHttp
import kotlin.system.exitProcess
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

suspend fun main() {
    val logging = HttpLoggingInterceptor { println(it) }
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val mimiClient = MimiClient(
        engineFactory = MimiNetworkEngine.OkHttp(client)
    )
    runWebSocketAsr(mimiClient)
    // OKHttp will not release all threads immediately after disconnection.
    // https://github.com/square/okhttp/issues/3708
    exitProcess(0)
}
