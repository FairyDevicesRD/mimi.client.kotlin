package ai.fd.mimi.client.service.nict.asr

import ai.fd.mimi.client.service.asr.core.MimiAsrResultStatus

data class MimiNictAsrV1Result(
    val type: String,
    val sessionId: String,
    val status: MimiAsrResultStatus,
    val response: List<String>
)
