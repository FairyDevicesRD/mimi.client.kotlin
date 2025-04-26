package ai.fd.mimi.client.service.token

import ai.fd.mimi.client.engine.MimiModelConverter
import ai.fd.mimi.client.engine.MimiNetworkEngine
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiTokenServiceTest {

    @MockK
    private lateinit var engine: MimiNetworkEngine

    @MockK
    private lateinit var converter: MimiModelConverter.JsonString<MimiTokenResult>

    @Test
    fun testConstructor() {
        val engineFactory = mockk<MimiNetworkEngine.Factory>(relaxed = true)

        MimiTokenService(engineFactory)
        verify { engineFactory.create(true, "auth.mimi.fd.ai", 443, "v2/token") }
        confirmVerified(engineFactory)

        MimiTokenService(engineFactory, false)
        verify { engineFactory.create(false, "auth.mimi.fd.ai", 80, "v2/token") }
        confirmVerified(engineFactory)

        MimiTokenService(engineFactory, true, "example.com")
        verify { engineFactory.create(true, "example.com", 443, "v2/token") }
        confirmVerified(engineFactory)

        MimiTokenService(engineFactory, false, "example.com", "path")
        verify { engineFactory.create(false, "example.com", 80, "path") }
        confirmVerified(engineFactory)

        MimiTokenService(engineFactory, false, "example.com", port = 1234)
        verify { engineFactory.create(false, "example.com", 1234, "v2/token") }
        confirmVerified(engineFactory)
    }

    @Test
    fun testIssueClientAccessToken_Single() = runTest {
        val result = mockk<MimiTokenResult>()
        coEvery {
            engine.request(
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId:clientId",
                            "client_secret" to "clientSecret",
                            "grant_type" to "https://auth.mimi.fd.ai/grant_type/client_credentials",
                            "scope" to "scope1"
                        )
                    )
                ),
                converter = converter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(engine, converter)
        val actual = service.issueClientAccessToken(
            applicationId = "applicationId",
            clientId = "clientId",
            clientSecret = "clientSecret",
            scope = scopeOf("scope1")
        )

        assertTrue(actual.isSuccess)
        assertEquals(result, actual.getOrNull())
    }

    @Test
    fun testIssueClientAccessToken_Multiple() = runTest {
        val result = mockk<MimiTokenResult>()
        coEvery {
            engine.request(
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId:clientId",
                            "client_secret" to "clientSecret",
                            "grant_type" to "https://auth.mimi.fd.ai/grant_type/client_credentials",
                            "scope" to "scope1;scope2"
                        )
                    )
                ),
                converter = converter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(engine, converter)
        val actual = service.issueClientAccessToken(
            applicationId = "applicationId",
            clientId = "clientId",
            clientSecret = "clientSecret",
            scopes = setOf(scopeOf("scope1"), scopeOf("scope2"))
        )

        assertTrue(actual.isSuccess)
        assertEquals(result, actual.getOrNull())
    }
}
