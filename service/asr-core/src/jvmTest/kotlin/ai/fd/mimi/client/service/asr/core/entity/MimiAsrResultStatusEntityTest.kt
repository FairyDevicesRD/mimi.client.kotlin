package ai.fd.mimi.client.service.asr.core.entity

import ai.fd.mimi.client.service.asr.core.MimiAsrResultStatus
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class MimiAsrResultStatusEntityTest {

    @Test
    fun testToStatus() {
        assertEquals(MimiAsrResultStatusEntity.RECOG_IN_PROGRESS.toStatus(), MimiAsrResultStatus.RECOG_IN_PROGRESS)
        assertEquals(MimiAsrResultStatusEntity.RECOG_FINISHED.toStatus(), MimiAsrResultStatus.RECOG_FINISHED)
    }
}
