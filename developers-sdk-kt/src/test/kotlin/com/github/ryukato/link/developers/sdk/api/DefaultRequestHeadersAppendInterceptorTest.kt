package com.github.ryukato.link.developers.sdk.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.ryukato.link.developers.sdk.model.request.OrderBy
import com.github.ryukato.link.developers.sdk.security.NonceGenerator
import com.github.ryukato.link.developers.sdk.security.SignatureGenerator
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
        mockWebServer.start(mockServerPort)
    }

    @AfterAll
    fun tearDownAll() {
        mockWebServer.shutdown()
    }

    @BeforeEach
    fun setUp() {
        mockWebServer.enqueue(MockResponse().setBody("hello, world!"))
    }

    @Test
    fun test_without_body_query_params() {
        interceptor = DefaultRequestHeadersAppendInterceptor(
            clock,
            defaultSignatureGenerator,
            nonceGenerator,
            SERVICE_API_KEY,
            SERVICE_API_SECRET
        )

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

        assertEquals(expectedHeaders.toSortedSet(), loggedHeaders.toSortedSet())
    }

    @Test
    fun test_with_query_params() {
        val capturedQueryParams = mutableSetOf<String>()
        val signatureGenerator = object : SignatureGenerator {
            override fun generate(
                serviceApiSecret: String,
                httpMethod: String,
                path: String,
                timestamp: Long,
                nonce: String,
                flatQueryParam: String,
                body: Map<String, Any?>): String {
                capturedQueryParams.add(flatQueryParam)
                return "test"
            }

            override fun generate(
                serviceApiSecret: String,
                httpMethod: String,
                path: String,
                timestamp: Long,
                nonce: String,
                queryParam: Map<String, List<String?>>,
                body: Map<String, Any?>): String {
                capturedQueryParams.add(queryParam.toSortedMap().map { "${it.key}=${it.value}" }.joinToString("&"))
                return "test"
            }
        }

        interceptor = DefaultRequestHeadersAppendInterceptor(
            clock,
            signatureGenerator,
            nonceGenerator,
            SERVICE_API_KEY,
            SERVICE_API_SECRET
        )

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

        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(mockServerPort)
            .addQueryParameter("contractId", "test-contract-id")
            .addQueryParameter("limit", "10")
            .addQueryParameter("page", "1")
            .addQueryParameter("orderBy", OrderBy.ASC.name)
            .build()

        val request: Request = Request.Builder()
            .url(httpUrl)
            .build()

        client.newCall(request).execute()

        assertEquals(expectedHeaders.toSortedSet(), loggedHeaders.toSortedSet())
        assertTrue(capturedQueryParams.any { it.contains("contractId=[test-contract-id]") })
        assertTrue(capturedQueryParams.any { it.contains("limit=[10]") })
        assertTrue(capturedQueryParams.any { it.contains("page=[1]") })
    }

    @Test
    fun test_with_form_body() {
        val capturedRequestBody = mutableMapOf<String, Any?>()
        val signatureGenerator = createCaptureBodySignatureGenerator(capturedRequestBody)

        interceptor = DefaultRequestHeadersAppendInterceptor(
            clock,
            signatureGenerator,
            nonceGenerator,
            SERVICE_API_KEY,
            SERVICE_API_SECRET
        )

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

        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(mockServerPort)
            .build()
        val requestBody = FormBody.Builder()
            .add("memo", "memo")
            .add("walletAddress", "walletAddress")
            .build();

        val request: Request = Request.Builder()
            .url(httpUrl)
            .post(requestBody)
            .build()

        client.newCall(request).execute()

        assertEquals(expectedHeaders.toSortedSet(), loggedHeaders.toSortedSet())
        assertTrue(capturedRequestBody.containsKey("memo"))
        assertTrue(capturedRequestBody.containsKey("walletAddress"))
    }

    @Test
    fun test_with_json_body() {
        val capturedRequestBody = mutableMapOf<String, Any?>()
        val signatureGenerator = createCaptureBodySignatureGenerator(capturedRequestBody)

        interceptor = DefaultRequestHeadersAppendInterceptor(
            clock,
            signatureGenerator,
            nonceGenerator,
            SERVICE_API_KEY,
            SERVICE_API_SECRET
        )

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

        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(mockServerPort)
            .build()

        val body = mapOf(
            "memo" to "memo",
            "walletAddress" to "walletAddress"
        )

        val requestBody = jacksonObjectMapper().writeValueAsString(body).toRequestBody("application/json; charset=UTF-8".toMediaType())

        val request: Request = Request.Builder()
            .url(httpUrl)
            .post(requestBody)
            .build()

        client.newCall(request).execute()

        assertEquals(expectedHeaders.toSortedSet(), loggedHeaders.toSortedSet())
        assertTrue(capturedRequestBody.containsKey("memo"))
        assertTrue(capturedRequestBody.containsKey("walletAddress"))
    }

    @Test
    fun test_with_empty_json_body() {
        val capturedRequestBody = mutableMapOf<String, Any?>()
        val signatureGenerator = createCaptureBodySignatureGenerator(capturedRequestBody)

        interceptor = DefaultRequestHeadersAppendInterceptor(
            clock,
            signatureGenerator,
            nonceGenerator,
            SERVICE_API_KEY,
            SERVICE_API_SECRET
        )

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

        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(mockServerPort)
            .build()

        val body = emptyMap<String, Any>()

        val requestBody = jacksonObjectMapper().writeValueAsString(body).toRequestBody("application/json; charset=UTF-8".toMediaType())

        val request: Request = Request.Builder()
            .url(httpUrl)
            .post(requestBody)
            .build()

        client.newCall(request).execute()

        assertEquals(expectedHeaders.toSortedSet(), loggedHeaders.toSortedSet())
        assertTrue(capturedRequestBody.isEmpty())
    }


    companion object {
        val expectedHeaders = setOf(
            "service-api-key",
            "Nonce",
            "Signature",
            "Timestamp"
        )
        const val SERVICE_API_KEY = "api-key"
        const val SERVICE_API_SECRET = "secret"

        val clock: Clock = Clock.systemUTC()
        val defaultSignatureGenerator = object : SignatureGenerator {
            override fun generate(
                serviceApiSecret: String,
                httpMethod: String,
                path: String,
                timestamp: Long,
                nonce: String,
                flatQueryParam: String,
                body: Map<String, Any?>): String {
                return "test"
            }

            override fun generate(
                serviceApiSecret: String,
                httpMethod: String,
                path: String,
                timestamp: Long,
                nonce: String,
                queryParam: Map<String, List<String?>>,
                body: Map<String, Any?>): String {
                return "test"
            }
        }
        val nonceGenerator = object : NonceGenerator {
            override fun newNonce(): String {
                return "test1234"
            }
        }

        fun createCaptureBodySignatureGenerator(capturedRequestBody: MutableMap<String, Any?>): SignatureGenerator {
            return object : SignatureGenerator {
                override fun generate(
                    serviceApiSecret: String,
                    httpMethod: String,
                    path: String,
                    timestamp: Long,
                    nonce: String,
                    flatQueryParam: String,
                    body: Map<String, Any?>): String {
                    capturedRequestBody.putAll(body)
                    return "test"
                }

                override fun generate(
                    serviceApiSecret: String,
                    httpMethod: String,
                    path: String,
                    timestamp: Long,
                    nonce: String,
                    queryParam: Map<String, List<String?>>,
                    body: Map<String, Any?>): String {
                    capturedRequestBody.putAll(body)
                    return "test"
                }
            }

        }
    }
}
