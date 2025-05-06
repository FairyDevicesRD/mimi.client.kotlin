package ai.fd.mimi.client.engine

import ai.fd.mimi.client.MimiJsonException
import ai.fd.mimi.client.MimiSerializationException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.io.bytestring.ByteString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MimiNetworkEngineTest {

    private lateinit var target: MimiNetworkEngine

    @BeforeEach
    fun setUp() {
        target = spyk<MimiNetworkEngine>(recordPrivateCalls = true)
    }

    @Test
    fun testRequest_JsonString() = runTest {
        val converter = mockk<MimiModelConverter.JsonString<Any>>()
        val headers = mapOf("key" to "value", "Authorization" to "Bearer accessToken")
        val requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "contentType")
        coEvery {
            target.requestAsStringInternal("path", requestBody, headers)
        } returns Result.success("response")
        val decodedModel = mockk<Any>()
        every { converter.decode("response") } returns decodedModel

        val actual = target.request(
            path = "path",
            accessToken = "accessToken",
            requestBody = requestBody,
            headers = headers,
            converter = converter
        )

        assertTrue(actual.isSuccess)
        assertEquals(decodedModel, actual.getOrThrow())
    }

    @Test
    fun testRequest_JsonString_NetworkError() = runTest {
        val headers = mapOf("key" to "value", "Authorization" to "Bearer accessToken")
        val requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "contentType")
        val exception = mockk<Exception>()
        coEvery {
            target.requestAsStringInternal("path", requestBody, headers)
        } returns Result.failure(exception)

        val actual = target.request(
            path = "path",
            accessToken = "accessToken",
            requestBody = requestBody,
            headers = headers,
            converter = mockk<MimiModelConverter.JsonString<Any>>()
        )

        assertTrue(actual.isFailure)
        assertEquals(exception, actual.exceptionOrNull())
    }

    @Test
    fun testRequest_JsonString_DecodeError() = runTest {
        val converter = mockk<MimiModelConverter.JsonString<Any>>()
        val headers = mapOf("key" to "value", "Authorization" to "Bearer accessToken")
        val requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "contentType")
        coEvery {
            target.requestAsStringInternal("path", requestBody, headers)
        } returns Result.success("response")
        val exception = mockk<MimiJsonException>()
        every { converter.decode("response") } throws exception

        val actual = target.request(
            path = "path",
            accessToken = "accessToken",
            requestBody = requestBody,
            headers = headers,
            converter = converter
        )

        assertTrue(actual.isFailure)
        assertEquals(exception, actual.exceptionOrNull())
    }

    @Test
    fun testRequest_Binary() = runTest {
        val converter = mockk<MimiModelConverter.Binary<Any>>()
        val headers = mapOf("key" to "value", "Authorization" to "Bearer accessToken")
        val requestBody = MimiNetworkEngine.RequestBody.FormData(mapOf("formKey" to "formValue"))
        coEvery {
            target.requestAsBinaryInternal("path", requestBody, headers)
        } returns Result.success(ByteString(1, 2, 3))
        val decodedModel = mockk<Any>()
        every { converter.decode(ByteString(1, 2, 3)) } returns decodedModel

        val actual = target.request(
            path = "path",
            accessToken = "accessToken",
            requestBody = requestBody,
            headers = headers,
            converter = converter
        )

        assertTrue(actual.isSuccess)
        assertEquals(decodedModel, actual.getOrThrow())
    }

    @Test
    fun testRequest_Binary_NetworkError() = runTest {
        val converter = mockk<MimiModelConverter.Binary<Any>>()
        val headers = mapOf("key" to "value", "Authorization" to "Bearer accessToken")
        val requestBody = MimiNetworkEngine.RequestBody.FormData(mapOf("formKey" to "formValue"))
        val exception = mockk<Exception>()
        coEvery {
            target.requestAsBinaryInternal("path", requestBody, headers)
        } returns Result.failure(exception)

        val actual = target.request(
            path = "path",
            accessToken = "accessToken",
            requestBody = requestBody,
            headers = headers,
            converter = converter
        )

        assertTrue(actual.isFailure)
        assertEquals(exception, actual.exceptionOrNull())
    }

    @Test
    fun testRequest_Binary_DecodeError() = runTest {
        val converter = mockk<MimiModelConverter.Binary<Any>>()
        val headers = mapOf("key" to "value", "Authorization" to "Bearer accessToken")
        val requestBody = MimiNetworkEngine.RequestBody.FormData(mapOf("formKey" to "formValue"))
        coEvery {
            target.requestAsBinaryInternal("path", requestBody, headers)
        } returns Result.success(ByteString(1, 2, 3))
        val exception = mockk<MimiSerializationException>()
        every { converter.decode(ByteString(1, 2, 3)) } throws exception

        val actual = target.request(
            path = "path",
            accessToken = "accessToken",
            requestBody = requestBody,
            headers = headers,
            converter = converter
        )

        assertTrue(actual.isFailure)
        assertEquals(exception, actual.exceptionOrNull())
    }

    @Test
    fun testRequest_NoAccessToken() = runTest {
        val converter = mockk<MimiModelConverter.JsonString<Any>>()
        val headers = mapOf("key" to "value")
        val requestBody = MimiNetworkEngine.RequestBody.Binary(ByteString(1, 2, 3), "contentType")
        coEvery {
            target.requestAsStringInternal("path", requestBody, headers)
        } returns Result.success("response")
        val decodedModel = mockk<Any>()
        every { converter.decode("response") } returns decodedModel

        val actual = target.request(
            path = "path",
            accessToken = null,
            requestBody = requestBody,
            headers = headers,
            converter = converter
        )

        assertTrue(actual.isSuccess)
        assertEquals(decodedModel, actual.getOrThrow())
    }

    @Test
    fun testOpenWebSocketSession() = runTest {
        val converter = mockk<MimiModelConverter.EncodableJsonString<Any>>()
        val headers = mapOf("key" to "value", "Authorization" to "Bearer accessToken")
        val session = mockk<MimiWebSocketSessionInternal<Any>>()
        coEvery {
            target.openWebSocketSessionInternal("path", "contentType", headers, converter)
        } returns session

        val actual = target.openWebSocketSession(
            path = "path",
            accessToken = "accessToken",
            contentType = "contentType",
            headers = headers,
            converter = converter
        )

        assertEquals(session, actual)
    }
}
