import ai.fd.mimi.client.MimiClient
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

suspend fun main() {
    val logging = HttpLoggingInterceptor { println(it.take(1000)) }
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val mimiClient = MimiClient(
        engineFactory = MimiNetworkEngine.OkHttp(client)
    )
    runAsr(mimiClient)
}
