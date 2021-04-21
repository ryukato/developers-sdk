package com.github.ryukato.link.developers.sdk.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.ryukato.link.developers.sdk.api.wrapper.ApiClientWrapper
import com.github.ryukato.link.developers.sdk.model.request.BatchTransferNonFungibleRequest
import com.github.ryukato.link.developers.sdk.model.request.BurnFromServiceTokenRequest
import com.github.ryukato.link.developers.sdk.model.request.FungibleTokenBurnRequest
import com.github.ryukato.link.developers.sdk.model.request.FungibleTokenCreateUpdateRequest
import com.github.ryukato.link.developers.sdk.model.request.FungibleTokenMintRequest
import com.github.ryukato.link.developers.sdk.model.request.MemoRequest
import com.github.ryukato.link.developers.sdk.model.request.MintServiceTokenRequest
import com.github.ryukato.link.developers.sdk.model.request.MultiMintNonFungible
import com.github.ryukato.link.developers.sdk.model.request.NonFungibleTokenBurnRequest
import com.github.ryukato.link.developers.sdk.model.request.NonFungibleTokenCreateUpdateRequest
import com.github.ryukato.link.developers.sdk.model.request.NonFungibleTokenItemTokenAttachRequest
import com.github.ryukato.link.developers.sdk.model.request.NonFungibleTokenItemTokenDetachRequest
import com.github.ryukato.link.developers.sdk.model.request.NonFungibleTokenMintRequest
import com.github.ryukato.link.developers.sdk.model.request.NonFungibleTokenMultiMintRequest
import com.github.ryukato.link.developers.sdk.model.request.OrderBy
import com.github.ryukato.link.developers.sdk.model.request.TokenId
import com.github.ryukato.link.developers.sdk.model.request.TransferBaseCoinRequest
import com.github.ryukato.link.developers.sdk.model.request.TransferFungibleTokenRequest
import com.github.ryukato.link.developers.sdk.model.request.TransferNonFungibleRequest
import com.github.ryukato.link.developers.sdk.model.request.TransferServiceTokenRequest
import com.github.ryukato.link.developers.sdk.model.request.UpdateServiceTokenRequest
import com.github.ryukato.link.developers.sdk.model.response.GenericResponse
import com.github.ryukato.link.developers.sdk.model.response.NonFungibleId
import com.github.ryukato.link.developers.sdk.model.response.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Clock
import java.time.LocalDateTime

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
    private lateinit var requestQueryParameterOrderer: RequestQueryParameterOrderer

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
        requestQueryParameterOrderer = DefaultRequestQueryParameterOrderer()

        apiClient = ApiClientFactory().build(
            HOST_URL,
            requestHeadersAppender,
            requestQueryParameterOrderer,
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
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testServiceTokens(): Unit = runBlocking {
        val response = apiClient.serviceTokens()
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testServiceToken(): Unit = runBlocking {
        val response = apiClient.serviceToken(SERVICE_TOKEN_CONTRACT_ID)
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testServiceTokenHolders(): Unit = runBlocking {
        val response = apiClient.serviceTokenHolders(
            SERVICE_TOKEN_CONTRACT_ID,
            10,
            1,
            OrderBy.DESC
        )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testUpdateServiceToken(): Unit = runBlocking {
        val response = apiClient.updateServiceToken(
            SERVICE_TOKEN_CONTRACT_ID,
            UpdateServiceTokenRequest(
                ownerAddress1,
                ownerAddress1Secret,
                "yyoosvctoken12"
            )
        )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testMintServiceToken(): Unit = runBlocking {
        val response = apiClient.mintServiceToken(
            SERVICE_TOKEN_CONTRACT_ID,
            MintServiceTokenRequest(
                ownerAddress = ownerAddress1,
                ownerSecret = ownerAddress1Secret,
                toAddress = ownerAddress2,
                amount = "1000"
            )
        )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testBurnServiceToken(): Unit = runBlocking {
        val response = apiClient.burnFromServiceToken(
            SERVICE_TOKEN_CONTRACT_ID,
            BurnFromServiceTokenRequest(
                ownerAddress = ownerAddress1,
                ownerSecret = ownerAddress1Secret,
                fromAddress = ownerAddress1,
                amount = "1"
            )
        )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }


    @Test
    fun testQueryTransaction(): Unit = runBlocking {
        val response =
            apiClient.transaction("C96F342BAA477DC18B6B085F8F55EEA27572DF37D999BE67BC5DECFA15F65FA6")
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testSaveMemoAndQuery(): Unit {
        val testMemo = "test"
        runBlocking {
            val response =
                apiClient.saveMemo(
                    MemoRequest(
                        memo = testMemo,
                        walletAddress = ownerAddress1,
                        walletSecret = ownerAddress1Secret
                    )
                )
            assertNotNull(response)
            assertEquals(1002, response.statusCode)
//            val txHash = response.responseData?.txHash!!
//
//            delay(2000)
//            val memoResponse = apiClient.queryMemo(txHash)
//            val actualMemo = memoResponse.responseData?.memo
//            assertEquals(testMemo, actualMemo)
        }
    }

    @Test
    fun testQueryWallets(): Unit = runBlocking {
        val response =
            apiClient.wallets()
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryWallet(): Unit = runBlocking {
        val response =
            apiClient.wallet(ownerAddress1)
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryWalletTransactions(): Unit = runBlocking {
        val response =
            apiClient.transactionOfWallet(
                walletAddress = ownerAddress1,
                after = LocalDateTime.now().minusYears(1).toEpochMilli(),
                before = LocalDateTime.now().toEpochMilli(),
                msgType = "token/MsgMint",
                limit = 10,
                page = 1,
                orderBy = OrderBy.DESC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryBaseCoinBalance(): Unit = runBlocking {
        val response =
            apiClient.baseCoinBalanceOfWallet(
                walletAddress = ownerAddress1
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testTransferBaseCoinBalance(): Unit = runBlocking {
        val response =
            apiClient.transferBaseCoin(
                walletAddress = ownerAddress1,
                TransferBaseCoinRequest(
                    walletSecret = ownerAddress1Secret,
                    toAddress = ownerAddress2,
                    amount = "1"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testQueryServiceTokenBalancesOfWallet(): Unit = runBlocking {
        val response =
            apiClient.serviceTokenBalancesOfWallet(
                walletAddress = ownerAddress1
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryServiceTokenBalanceOfWallet(): Unit = runBlocking {
        val response =
            apiClient.serviceTokenBalanceOfWallet(
                walletAddress = ownerAddress1,
                contractId = SERVICE_TOKEN_CONTRACT_ID
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testTransferServiceToken(): Unit = runBlocking {
        val response =
            apiClient.transferServiceToken(
                walletAddress = ownerAddress1,
                contractId = SERVICE_TOKEN_CONTRACT_ID,
                TransferServiceTokenRequest(
                    walletSecret = ownerAddress1Secret,
                    toAddress = ownerAddress2,
                    amount = "1"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testQueryFungibleTokensBalanceOfWallet(): Unit = runBlocking {
        val response =
            apiClient.fungibleTokensBalanceOfWallet(
                walletAddress = ownerAddress1,
                contractId = ITEM_TOKEN_CONTRACT_ID,
                page = 1,
                limit = 10,
                orderBy = OrderBy.DESC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryFungibleTokenBalanceOfWallet(): Unit = runBlocking {
        val response =
            apiClient.fungibleTokenBalanceOfWallet(
                walletAddress = ownerAddress1,
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = FUNGIBLE_TOKEN_TYPE
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testTransferFungibleTokenOfWallet(): Unit = runBlocking {
        val response =
            apiClient.transferFungibleTokenOfWallet(
                walletAddress = ownerAddress1,
                contractId = SERVICE_TOKEN_CONTRACT_ID,
                tokenType = FUNGIBLE_TOKEN_TYPE,
                TransferFungibleTokenRequest(
                    walletSecret = ownerAddress1Secret,
                    toAddress = ownerAddress2,
                    amount = "1"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testQueryNonFungibleTokenBalancesOfWallet(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenBalancesOfWallet(
                walletAddress = ownerAddress1,
                contractId = ITEM_TOKEN_CONTRACT_ID,
                page = 1,
                limit = 10,
                orderBy = OrderBy.DESC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryNonFungibleTokenBalanceOfWalletByType(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenBalancesOfWalletByType(
                walletAddress = ownerAddress1,
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                page = 1,
                limit = 10,
                orderBy = OrderBy.DESC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryNonFungibleTokenBalanceOfWallet(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenBalanceOfWallet(
                walletAddress = ownerAddress1,
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX2,
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testTransferNonFungibleTokenOfWallet(): Unit = runBlocking {
        val response =
            apiClient.transferNonFungibleTokenOfWallet(
                walletAddress = ownerAddress1,
                contractId = SERVICE_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX,
                TransferNonFungibleRequest(
                    walletSecret = ownerAddress1Secret,
                    toAddress = ownerAddress2
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testBatchTransferNonFungibleTokenOfWallet(): Unit = runBlocking {
        val response =
            apiClient.batchTransferNonFungibleTokenOfWallet(
                walletAddress = ownerAddress1,
                contractId = SERVICE_TOKEN_CONTRACT_ID,
                request = BatchTransferNonFungibleRequest(
                    walletSecret = ownerAddress1Secret,
                    toAddress = ownerAddress2,
                    transferList = TokenId.fromMulti(setOf("1000000200000001", "1000000200000002"))
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testQueryItemToken(): Unit = runBlocking {
        val response =
            apiClient.itemToken(contractId = ITEM_TOKEN_CONTRACT_ID)
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryFungibleTokens(): Unit = runBlocking {
        val response =
            apiClient.fungibleTokens(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                page = 1,
                limit = 10,
                orderBy = OrderBy.DESC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryFungibleToken(): Unit = runBlocking {
        val response =
            apiClient.fungibleToken(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = FUNGIBLE_TOKEN_TYPE
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryFungibleTokenHolders(): Unit = runBlocking {
        val response =
            apiClient.fungibleTokenHolders(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = FUNGIBLE_TOKEN_TYPE,
                page = 1,
                limit = 10,
                orderBy = OrderBy.DESC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testCreateFungible(): Unit = runBlocking {
        val response =
            apiClient.createFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                request = FungibleTokenCreateUpdateRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    name = "test111"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testUpdateFungible(): Unit = runBlocking {
        val response =
            apiClient.updateFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = FUNGIBLE_TOKEN_TYPE,
                request = FungibleTokenCreateUpdateRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    name = "test222"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testMintFungible(): Unit = runBlocking {
        val response =
            apiClient.mintFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = FUNGIBLE_TOKEN_TYPE,
                request = FungibleTokenMintRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    toAddress = ownerAddress2,
                    amount = "10"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testBurnFungible(): Unit = runBlocking {
        val response =
            apiClient.burnFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = FUNGIBLE_TOKEN_TYPE,
                request = FungibleTokenBurnRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    fromAddress = ownerAddress2,
                    amount = "1"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testQueryNonFungibleTokenTypes(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenTypes(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                page = 1,
                limit = 10,
                orderBy = OrderBy.DESC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryNonFungibleTokenType(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenType(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                page = 1,
                limit = 10,
                orderBy = OrderBy.DESC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testCreateNonFungibleType(): Unit = runBlocking {
        val response =
            apiClient.createNonFungibleType(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                request = NonFungibleTokenCreateUpdateRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    name = "nftType1"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testUpdateNonFungibleTokenType(): Unit = runBlocking {
        val response =
            apiClient.updateNonFungibleTokenType(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                request = NonFungibleTokenCreateUpdateRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    name = "nft022"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testQueryNonFungibleToken(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleToken(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX2
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testUpdateNonFungibleToken(): Unit = runBlocking {
        val response =
            apiClient.updateNonFungibleToken(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX2,
                request = NonFungibleTokenCreateUpdateRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    name = "nft022"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testMintNonFungible(): Unit = runBlocking {
        val response =
            apiClient.mintNonFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                request = NonFungibleTokenMintRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    toAddress = ownerAddress2,
                    name = "nft100"
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testQueryNonFungibleTokenTypeHolders(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenTypeHolders(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                page = 1,
                limit = 2,
                orderBy = OrderBy.ASC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testQueryNonFungibleTokenHolder(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenHolder(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX2
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testMultiMintNonFungible(): Unit = runBlocking {
        val response =
            apiClient.multiMintNonFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                request = NonFungibleTokenMultiMintRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    toAddress = ownerAddress2,
                    mintList = listOf(
                        MultiMintNonFungible(
                            tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                            name = "nft300"
                        )
                    )
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testBurnNonFungible(): Unit = runBlocking {
        val response =
            apiClient.burnNonFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX2,
                request = NonFungibleTokenBurnRequest(
                    ownerAddress = ownerAddress1,
                    ownerSecret = ownerAddress1Secret,
                    fromAddress = ownerAddress2
                )
            )
        assertNotNull(response)
        assertEquals(1002, response.statusCode)
    }

    @Test
    fun testNonFungibleTokenChildren(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenChildren(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX2,
                page = 1,
                limit = 2,
                orderBy = OrderBy.ASC
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testNonFungibleTokenParent(): Unit = runBlocking {
        val wrappedResponse : ResultWrapper<GenericResponse<NonFungibleId>> =
        ApiClientWrapper(jacksonObjectMapper()).invoke {
            apiClient.nonFungibleTokenParent(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX2
            )
        }

        assertTrue(wrappedResponse is ResultWrapper.GenericError)
        assertEquals(404, (wrappedResponse as ResultWrapper.GenericError).code)
        assertEquals(4047, wrappedResponse.error?.statusCode)

        @Suppress("USELESS_IS_CHECK")
        println(
            when(wrappedResponse) {
                is ResultWrapper.NetworkError -> Unit // do some error handling
                is ResultWrapper.GenericError -> wrappedResponse.error // do some error handling
                is ResultWrapper.Success -> wrappedResponse.value // // do handle successful response
            }
        )
    }

    @Test
    fun testNonFungibleTokenRoot(): Unit = runBlocking {
        val response =
            apiClient.nonFungibleTokenRoot(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX2
            )
        assertNotNull(response)
        assertEquals(1000, response.statusCode)
    }

    @Test
    fun testAttachAndDetachNonFungible(): Unit = runBlocking {
        val attachResponse =
            apiClient.attachNonFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX3,
                request = NonFungibleTokenItemTokenAttachRequest(
                    parentTokenId = "$NON_FUNGIBLE_TOKEN_TYPE$NON_FUNGIBLE_TOKEN_INDEX2",
                    serviceWalletAddress = ownerAddress1,
                    serviceWalletSecret = ownerAddress1Secret,
                    tokenHolderAddress = ownerAddress1
                )
            )
        assertNotNull(attachResponse)
        assertEquals(1002, attachResponse.statusCode)

        delay(2000) //wait for tx confirmed
        val detachResponse =
            apiClient.detachNonFungible(
                contractId = ITEM_TOKEN_CONTRACT_ID,
                tokenType = NON_FUNGIBLE_TOKEN_TYPE,
                tokenIndex = NON_FUNGIBLE_TOKEN_INDEX3,
                request = NonFungibleTokenItemTokenDetachRequest(
                    serviceWalletAddress = ownerAddress1,
                    serviceWalletSecret = ownerAddress1Secret,
                    tokenHolderAddress = ownerAddress1
                )
            )
        assertNotNull(detachResponse)
//        assertEquals(1002, detachResponse.statusCode)
        println(detachResponse)
    }

    companion object {
        const val HOST_URL = "https://test-api.blockchain.line.me"
        const val SERVICE_ID = "5016b367-eae8-44cb-8052-6672b498d894"
        const val SERVICE_TOKEN_CONTRACT_ID = "493aba33"
        const val ITEM_TOKEN_CONTRACT_ID = "1c396d46"
        const val FUNGIBLE_TOKEN_TYPE = "00000002"
        const val NON_FUNGIBLE_TOKEN_TYPE = "10000002"
        const val NON_FUNGIBLE_TOKEN_INDEX = "00000002"
        const val NON_FUNGIBLE_TOKEN_INDEX2 = "00000003"
        const val NON_FUNGIBLE_TOKEN_INDEX3 = "00000006"

        const val LINE_USER_ID = "U9fc03e78e1ae958b1bd3633cfb48acb9"
        const val LINE_USER_WALLET_ADDRESS = "tlink1p07h3gj6n0mhqlj0tdxaltlsk727hrujcprmqu"

    }
}

