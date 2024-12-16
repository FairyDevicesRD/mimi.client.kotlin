package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.service.asr.core.MimiAsrResultStatus

data class MimiNictAsrV2Result(
    val type: String,
    val sessionId: String,
    val status: MimiAsrResultStatus,
    val response: List<Response>
) {
    data class Response(
        val result: String,
        val words: List<String>,
        val determined: Boolean,
        val time: Long
    )
}
