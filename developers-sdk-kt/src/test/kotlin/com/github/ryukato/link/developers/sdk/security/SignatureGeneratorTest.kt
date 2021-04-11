package com.github.ryukato.link.developers.sdk.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyMap
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SignatureGeneratorTest {
    private lateinit var signatureGenerator: SignatureGenerator

    @Mock
    private lateinit var requestBodyFlattener: RequestBodyFlattener

    @Mock
    private lateinit var queryParameterFlattener: QueryParameterFlattener


    @BeforeEach
    fun setUp() {
        signatureGenerator = DefaultSignatureGenerator(
            queryParameterFlattener,
            requestBodyFlattener
        )
    }

    @Test
    fun test() {
        `when`(requestBodyFlattener.flatten(emptyMap())).thenAnswer { "" }
        val serviceApiSecret = "test-api-secret"
        val httpMethod = "GET"
        val path = "/v1/test-api"
        val timestamp = 1617350132629
        val nonce = "test"
        val flatQueryParam = "limit=10&page=1&orderBy=desc"
        val signature = signatureGenerator.generate(
            serviceApiSecret,
            httpMethod,
            path, timestamp, nonce, flatQueryParam
        )
        assertNotNull(signature)
        val expectedSignature = "2DKzLI81cUnwur83SXFbk5tNuWi3dC+mfRnXtab8Jkwl70Ex+FpgxOKdo3ozynxzl6HDm5he2GT6IORSQ7zzxg=="
        assertEquals(expectedSignature, signature)
    }

    @Test
    fun test_many_query_parameters() {
        val page = "0"
        val limit = "10"
        val orderBy = "desc"
        val msgType = "collection/MsgTransferNFTFrom"
        val after = "1614563991000"
        val before = "1617155991000"

        `when`(requestBodyFlattener.flatten(emptyMap())).thenAnswer { "" }
        `when`(queryParameterFlattener.flatten(anyMap())).thenAnswer { "after=$after&before=$before&limit=$limit&msgType=$msgType&orderBy=$orderBy&page=$page" }
        val serviceApiSecret = "7d55f1f5-0f6f-426e-909c-47913aa09e72"
        val httpMethod = "GET"
        val path = "/v1/wallets/tlink1ey2p39e4l78h49pm28z5ms62ycd06sgrprtps5/transactions"
        val timestamp = 1617503164770
        val nonce = "805d1b42"
        val queryParams = mapOf<String, List<String?>>(
            "limit" to listOf(limit),
            "page" to listOf(page),
            "orderBy" to listOf(orderBy),
            "msgType" to listOf(msgType),
            "after" to listOf(after),
            "before" to listOf(before)
        )
        val signature = signatureGenerator.generate(
            serviceApiSecret,
            httpMethod,
            path, timestamp, nonce, queryParams
        )
        assertNotNull(signature)
        val expectedSignature = "Iq4lDCgzMmtFrZHuE0b7Xu6PqaqnoVJlG2WxMtuAHWuB8hoG98swyb578LMZMUbHLE3D1ldQA1U4hxSPyxiFSA=="
        assertEquals(expectedSignature, signature)
    }
}
