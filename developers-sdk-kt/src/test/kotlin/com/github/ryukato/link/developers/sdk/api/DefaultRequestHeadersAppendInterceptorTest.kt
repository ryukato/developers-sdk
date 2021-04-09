package com.github.ryukato.link.developers.sdk.api

import com.github.ryukato.link.developers.sdk.security.NonceGenerator
import com.github.ryukato.link.developers.sdk.security.SignatureGenerator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Clock

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultRequestHeadersAppendInterceptorTest {

    private val mockServerPort = 9090
    private lateinit var mockWebServer: MockWebServer

    private lateinit var interceptor: Interceptor

    @BeforeAll
    fun setUpAll() {
        mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setBody("hello, world!"))
        mockWebServer.start(mockServerPort)
    }

    @AfterAll
    fun tearDownAll() {
        mockWebServer.shutdown()
    }

    @BeforeEach
    fun setUp() {
        interceptor = DefaultRequestHeadersAppendInterceptor(
            clock,
            signatureGenerator,
            nonceGenerator,
            SERVICE_API_KEY,
            SERVICE_API_SECRET
        )
    }

    @Test
    fun test() {
        val loggedHeaders = mutableSetOf<String>()
        val headerCaptureInterceptor = Interceptor { chain ->
            val headers = chain.request().headers
            val capturedHeaders = headers.names().map {
                it
            }.toSet()
            loggedHeaders.addAll(capturedHeaders)
            chain.proceed(chain.request())
        }

        // the order of interceptors is important
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(headerCaptureInterceptor)
            .build()

        val request: Request = Request.Builder()
            .url("http://localhost:$mockServerPort/")
            .build()

        client.newCall(request).execute()

        val expectedHeaders = setOf(
            "service-api-key",
            "Nonce",
            "Signature",
            "Timestamp"
        )
        assertEquals(expectedHeaders.toSortedSet(), loggedHeaders.toSortedSet())
    }

    companion object {
        const val SERVICE_API_KEY = "api-key"
        const val SERVICE_API_SECRET = "secret"

        val clock: Clock = Clock.systemUTC()
        val signatureGenerator = object : SignatureGenerator {
            override fun generate(
                serviceApiSecret: String,
                httpMethod: String,
                path: String,
                timestamp: Long,
                nonce: String,
                pathWithQueryParam: String,
                body: Map<String, Any?>): String {
                return "test"
            }

            override fun generate(
                serviceApiSecret: String,
                httpMethod: String,
                path: String,
                timestamp: Long,
                nonce: String,
                queryParam: Map<String, Any?>,
                body: Map<String, Any?>): String {
                return "test"
            }
        }
        val nonceGenerator = object : NonceGenerator {
            override fun newNonce(): String {
                return "test1234"
            }
        }
    }
}
