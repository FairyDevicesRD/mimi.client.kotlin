import ai.fd.mimi.client.MimiClient
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.ktor.Ktor
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets

suspend fun main() {
    val httpClient = HttpClient(CIO) {
        install(WebSockets)
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
    }
    val mimiClient = MimiClient(
        engineFactory = MimiNetworkEngine.Ktor(httpClient)
    )
    runAsr(mimiClient)
}
