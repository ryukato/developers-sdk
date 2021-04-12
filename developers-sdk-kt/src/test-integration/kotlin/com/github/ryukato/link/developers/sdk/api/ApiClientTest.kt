package com.github.ryukato.link.developers.sdk.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Clock

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiClientTest {
    // from vm argument at runtime
    private lateinit var serviceApiKey: String
    private lateinit var serviceApiSecret: String
    private lateinit var ownerAddress1: String
    private lateinit var ownerAddress1Secret: String
    private lateinit var ownerAddress2: String

    private lateinit var apiClient: ApiClient

    private val applicationClock: Clock = Clock.systemUTC()
    private val nonceGenerator = DefaultStringNonceGenerator()
    private val signatureGenerator = DefaultSignatureGenerator(
        DefaultOrderedQueryParameterFlattener(),
        DefaultRequestBodyFlattener()
    )

    private lateinit var requestHeadersAppender: RequestHeadersAppender

    @BeforeAll
    fun setUpAll() {
        serviceApiKey = System.getenv()["service-api-key"]!!
        serviceApiSecret = System.getenv()["service-api-secret"]!!
        ownerAddress1 = System.getenv()["owner-address1"]!!
        ownerAddress1Secret = System.getenv()["owner-address1-secret"]!!
        ownerAddress2 = System.getenv()["owner-address2"]!!

        requestHeadersAppender = DefaultRequestHeadersAppender(
            applicationClock,
            signatureGenerator,
            nonceGenerator,
            serviceApiKey,
            serviceApiSecret
        )
        apiClient = ApiClientFactory().build(
            HOST_URL,
            requestHeadersAppender,
            true,
            jacksonObjectMapper()
        )
    }

    @Test
    fun testTime(): Unit = runBlocking {
        val response = apiClient.time()
        assertNotNull(response)
    }

    @Test
    fun testServiceDetail(): Unit = runBlocking {
        val response = apiClient.serviceDetail(SERVICE_ID)
        assertNotNull(response)
    }

    companion object {
        const val HOST_URL = "https://test-api.blockchain.line.me"
        const val SERVICE_ID = "5016b367-eae8-44cb-8052-6672b498d894"
        const val SERVICE_TOKEN_CONTRACT_ID = "493aba33"
        const val ITEM_TOKEN_CONTRACT_ID = "1c396d46"
        const val LINE_USER_ID = "U9fc03e78e1ae958b1bd3633cfb48acb9"
        const val LINE_USER_WALLET_ADDRESS = "tlink1p07h3gj6n0mhqlj0tdxaltlsk727hrujcprmqu"

    }
}

