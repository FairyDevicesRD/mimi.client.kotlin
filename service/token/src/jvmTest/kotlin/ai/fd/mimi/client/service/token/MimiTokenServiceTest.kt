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
    private lateinit var issueTokenConverter: MimiModelConverter.JsonString<MimiIssueTokenResult>

    @MockK
    private lateinit var revokeTokenConverter: MimiModelConverter<Unit>

    @MockK
    private lateinit var validateTokenConverter: MimiModelConverter.JsonString<MimiValidateTokenResult>

    @Test
    fun testConstructor() {
        val engineFactory = mockk<MimiNetworkEngine.Factory>(relaxed = true)

        MimiTokenService(engineFactory)
        verify { engineFactory.create(true, "auth.mimi.fd.ai", 443) }
        confirmVerified(engineFactory)

        MimiTokenService(engineFactory, false)
        verify { engineFactory.create(false, "auth.mimi.fd.ai", 80) }
        confirmVerified(engineFactory)

        MimiTokenService(engineFactory, true, "example.com")
        verify { engineFactory.create(true, "example.com", 443) }
        confirmVerified(engineFactory)

        MimiTokenService(engineFactory, false, "example.com", port = 1234)
        verify { engineFactory.create(false, "example.com", 1234) }
        confirmVerified(engineFactory)
    }

    @Test
    fun testIssueApplicationAccessToken_Single() = runTest {
        val result = mockk<MimiIssueTokenResult>()
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId",
                            "client_secret" to "applicationSecret",
                            "grant_type" to "https://auth.mimi.fd.ai/grant_type/application_credentials",
                            "scope" to "scope1"
                        )
                    )
                ),
                converter = issueTokenConverter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(
            issueAccessTokenPath = "path",
            revokeAccessTokenPath = "",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.issueApplicationAccessToken(
            applicationId = "applicationId",
            applicationSecret = "applicationSecret",
            scope = scopeOf("scope1")
        )

        assertTrue(actual.isSuccess)
        assertEquals(result, actual.getOrNull())
    }

    @Test
    fun testIssueApplicationAccessToken_Multiple() = runTest {
        val result = mockk<MimiIssueTokenResult>()
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId",
                            "client_secret" to "applicationSecret",
                            "grant_type" to "https://auth.mimi.fd.ai/grant_type/application_credentials",
                            "scope" to "scope1;scope2"
                        )
                    )
                ),
                converter = issueTokenConverter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(
            issueAccessTokenPath = "path",
            revokeAccessTokenPath = "",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter,
        )
        val actual = service.issueApplicationAccessToken(
            applicationId = "applicationId",
            applicationSecret = "applicationSecret",
            scopes = setOf(scopeOf("scope1"), scopeOf("scope2"))
        )

        assertTrue(actual.isSuccess)
        assertEquals(result, actual.getOrNull())
    }

    @Test
    fun testIssueClientAccessTokenFromExternalAuthServer_Single() = runTest {
        val result = mockk<MimiIssueTokenResult>()
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId:clientId",
                            "client_secret" to "applicationSecret",
                            "grant_type" to "https://auth.mimi.fd.ai/grant_type/application_client_credentials",
                            "scope" to "scope1"
                        )
                    )
                ),
                converter = issueTokenConverter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(
            issueAccessTokenPath = "path",
            revokeAccessTokenPath = "",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.issueClientAccessTokenFromExternalAuthServer(
            applicationId = "applicationId",
            clientId = "clientId",
            applicationSecret = "applicationSecret",
            scope = scopeOf("scope1")
        )

        assertTrue(actual.isSuccess)
        assertEquals(result, actual.getOrNull())
    }

    @Test
    fun testIssueClientAccessTokenFromExternalAuthServer_Multiple() = runTest {
        val result = mockk<MimiIssueTokenResult>()
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId:clientId",
                            "client_secret" to "applicationSecret",
                            "grant_type" to "https://auth.mimi.fd.ai/grant_type/application_client_credentials",
                            "scope" to "scope1;scope2"
                        )
                    )
                ),
                converter = issueTokenConverter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(
            issueAccessTokenPath = "path",
            revokeAccessTokenPath = "",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.issueClientAccessTokenFromExternalAuthServer(
            applicationId = "applicationId",
            clientId = "clientId",
            applicationSecret = "applicationSecret",
            scopes = setOf(scopeOf("scope1"), scopeOf("scope2"))
        )

        assertTrue(actual.isSuccess)
        assertEquals(result, actual.getOrNull())
    }

    @Test
    fun testIssueClientAccessToken_Single() = runTest {
        val result = mockk<MimiIssueTokenResult>()
        coEvery {
            engine.request(
                path = "path",
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
                converter = issueTokenConverter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(
            issueAccessTokenPath = "path",
            revokeAccessTokenPath = "",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
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
        val result = mockk<MimiIssueTokenResult>()
        coEvery {
            engine.request(
                path = "path",
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
                converter = issueTokenConverter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(
            issueAccessTokenPath = "path",
            revokeAccessTokenPath = "",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.issueClientAccessToken(
            applicationId = "applicationId",
            clientId = "clientId",
            clientSecret = "clientSecret",
            scopes = setOf(scopeOf("scope1"), scopeOf("scope2"))
        )

        assertTrue(actual.isSuccess)
        assertEquals(result, actual.getOrNull())
    }

    @Test
    fun testRevokeApplicationAccessToken() = runTest {
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId",
                            "client_secret" to "applicationSecret",
                            "token" to "token"
                        )
                    )
                ),
                converter = revokeTokenConverter,
                accessToken = null
            )
        } returns Result.success(Unit)

        val service = MimiTokenService(
            issueAccessTokenPath = "",
            revokeAccessTokenPath = "path",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.revokeApplicationAccessToken(
            applicationId = "applicationId",
            applicationSecret = "applicationSecret",
            token = "token"
        )

        assertTrue(actual.isSuccess)
        assertEquals(Unit, actual.getOrNull())
    }

    @Test
    fun testRevokeApplicationAccessToken_error() = runTest {
        val exception = mockk<Exception>()
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId",
                            "client_secret" to "applicationSecret",
                            "token" to "token"
                        )
                    )
                ),
                converter = revokeTokenConverter,
                accessToken = null
            )
        } returns Result.failure(exception)

        val service = MimiTokenService(
            issueAccessTokenPath = "",
            revokeAccessTokenPath = "path",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.revokeApplicationAccessToken(
            applicationId = "applicationId",
            applicationSecret = "applicationSecret",
            token = "token"
        )

        assertTrue(actual.isFailure)
        assertEquals(exception, actual.exceptionOrNull())
    }

    @Test
    fun testRevokeClientAccessToken() = runTest {
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId:clientId",
                            "client_secret" to "clientSecret",
                            "token" to "token"
                        )
                    )
                ),
                converter = revokeTokenConverter,
                accessToken = null
            )
        } returns Result.success(Unit)

        val service = MimiTokenService(
            issueAccessTokenPath = "",
            revokeAccessTokenPath = "path",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.revokeClientAccessToken(
            applicationId = "applicationId",
            clientId = "clientId",
            clientSecret = "clientSecret",
            token = "token"
        )

        assertTrue(actual.isSuccess)
        assertEquals(Unit, actual.getOrNull())
    }

    @Test
    fun testRevokeClientAccessToken_error() = runTest {
        val exception = mockk<Exception>()
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "client_id" to "applicationId:clientId",
                            "client_secret" to "clientSecret",
                            "token" to "token"
                        )
                    )
                ),
                converter = revokeTokenConverter,
                accessToken = null
            )
        } returns Result.failure(exception)

        val service = MimiTokenService(
            issueAccessTokenPath = "",
            revokeAccessTokenPath = "path",
            validateAccessTokenPath = "",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.revokeClientAccessToken(
            applicationId = "applicationId",
            clientId = "clientId",
            clientSecret = "clientSecret",
            token = "token"
        )

        assertTrue(actual.isFailure)
        assertEquals(exception, actual.exceptionOrNull())
    }

    @Test
    fun testValidateClientAccessToken() = runTest {
        val result = mockk<MimiValidateTokenResult>()
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "token" to "token"
                        )
                    )
                ),
                converter = validateTokenConverter,
                accessToken = null
            )
        } returns Result.success(result)

        val service = MimiTokenService(
            issueAccessTokenPath = "",
            revokeAccessTokenPath = "",
            validateAccessTokenPath = "path",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.validateAccessToken(token = "token")

        assertTrue(actual.isSuccess)
        assertEquals(result, actual.getOrNull())
    }

    @Test
    fun testValidateClientAccessToken_error() = runTest {
        val exception = mockk<Exception>()
        coEvery {
            engine.request(
                path = "path",
                requestBody = eq(
                    MimiNetworkEngine.RequestBody.FormData(
                        fields = mapOf(
                            "token" to "token"
                        )
                    )
                ),
                converter = validateTokenConverter,
                accessToken = null
            )
        } returns Result.failure(exception)

        val service = MimiTokenService(
            issueAccessTokenPath = "",
            revokeAccessTokenPath = "",
            validateAccessTokenPath = "path",
            engine = engine,
            issueTokenResultConverter = issueTokenConverter,
            revokeTokenResultConverter = revokeTokenConverter,
            validateTokenResultConverter = validateTokenConverter
        )
        val actual = service.validateAccessToken(token = "token")

        assertTrue(actual.isFailure)
        assertEquals(exception, actual.exceptionOrNull())
    }
}
