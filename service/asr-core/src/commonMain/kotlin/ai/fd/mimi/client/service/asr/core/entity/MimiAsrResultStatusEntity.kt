package ai.fd.mimi.client.service.asr.core.entity

import ai.fd.mimi.client.service.asr.core.MimiAsrResultStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MimiAsrResultStatusEntity {
    @SerialName("recog-in-progress")
    RECOG_IN_PROGRESS,

    @SerialName("recog-finished")
    RECOG_FINISHED;

    fun toStatus(): MimiAsrResultStatus = when (this) {
        RECOG_IN_PROGRESS -> MimiAsrResultStatus.RECOG_IN_PROGRESS
        RECOG_FINISHED -> MimiAsrResultStatus.RECOG_FINISHED
    }
}
