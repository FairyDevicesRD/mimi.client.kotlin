package ai.fd.mimi.client.service.token

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class MimiTokenGrantTypeTest {

    @Test
    fun testEntries() {
        val grantTypes = MimiTokenGrantType.entries

        assertEquals(3, grantTypes.size)
        assertEquals(
            "https://auth.mimi.fd.ai/grant_type/application_credentials",
            MimiTokenGrantType.APPLICATION_CREDENTIALS.value
        )
        assertEquals(
            "https://auth.mimi.fd.ai/grant_type/client_credentials",
            MimiTokenGrantType.CLIENT_CREDENTIALS.value
        )
        assertEquals(
            "https://auth.mimi.fd.ai/grant_type/application_client_credentials",
            MimiTokenGrantType.APPLICATION_CLIENT_CREDENTIALS.value
        )
    }
}
