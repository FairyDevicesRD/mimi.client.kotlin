package ai.fd.mimi.client.service.asr

data class MimiAsrResult(
    val type: String,
    val sessionId: String,
    val status: Status,
    val response: List<Response>
) {

    data class Response(
        val pronunciation: String,
        val result: String,
        val startTime: Long,
        val endTime: Long
    )

    enum class Status {
        RECOG_IN_PROGRESS,
        RECOG_FINISHED
    }
}
