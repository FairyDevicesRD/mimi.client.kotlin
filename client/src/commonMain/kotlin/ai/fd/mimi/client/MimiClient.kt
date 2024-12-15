package ai.fd.mimi.client

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import ai.fd.mimi.client.engine.MimiWebSocketSessionInternal
import kotlin.coroutines.cancellation.CancellationException

class MimiClient(
    engineFactory: MimiNetworkEngine.Factory,
    useSsl: Boolean = true,
    host: String = "service.mimi.fd.ai",
    port: Int = if (useSsl) 443 else 80
) {
    private val engine: MimiNetworkEngine = engineFactory.create(
        useSsl = useSsl,
        host = host,
        port = port
    )

    suspend fun <T> request(
        accessToken: String,
        byteArray: ByteArray,
        contentType: String,
        headers: Map<String, String>,
        converter: MimiModelConverter<T>
    ): Result<T> = engine.request(accessToken, byteArray, contentType, headers, converter)

    @Throws(MimiIOException::class, CancellationException::class)
    suspend fun <T> openWebSocketSession(
        accessToken: String,
        contentType: String,
        headers: Map<String, String>,
        converter: MimiModelConverter<T>
    ): MimiWebSocketSessionInternal<T> = engine.openWebSocketSession(accessToken, contentType, headers, converter)
}
