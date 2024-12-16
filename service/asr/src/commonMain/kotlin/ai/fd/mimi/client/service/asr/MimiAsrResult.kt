package ai.fd.mimi.client.service.asr

import ai.fd.mimi.client.service.asr.core.MimiAsrResultStatus

data class MimiAsrResult(
    val type: String,
    val sessionId: String,
    val status: MimiAsrResultStatus,
    val response: List<Response>
) {

    data class Response(
        val pronunciation: String,
        val result: String,
        val startTime: Long,
        val endTime: Long
    )
}
