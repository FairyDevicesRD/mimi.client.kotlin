import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.okhttp.OkHttp
import kotlin.system.exitProcess
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

suspend fun main() {
    val logging = HttpLoggingInterceptor { println(it.take(1000)) }
        .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    runTts(MimiNetworkEngine.OkHttp(client))
    exitProcess(0)
}
