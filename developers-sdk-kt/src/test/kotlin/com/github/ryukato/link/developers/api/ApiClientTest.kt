package com.github.ryukato.link.developers.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.ryukato.link.developers.helper.testRunBlocking
import com.github.ryukato.link.developers.sdk.api.ApiClient
import com.github.ryukato.link.developers.sdk.api.*

import com.github.ryukato.link.developers.sdk.api.factory.ApiClientFactory
import com.github.ryukato.link.developers.sdk.api.factory.ApiClientFactoryConfig
import com.github.ryukato.link.developers.sdk.http.DefaultHttpClientBuilderImpl
import com.github.ryukato.link.developers.sdk.model.dto.ApiKeySecret
import com.github.ryukato.link.developers.sdk.model.request.*
import com.github.ryukato.link.developers.sdk.model.response.*
import com.github.ryukato.link.developers.util.loadJsonToString
import com.github.ryukato.link.developers.util.toEpochMilli
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

// TODO validate request header-including timestamp and signature

class ApiClientTest {
    private lateinit var mockEngineConfig: MockEngineConfig
    private lateinit var testMockEngineFactory: TestMockEngineFactory
    private lateinit var apiClient: ApiClient

    @KtorExperimentalAPI
    @BeforeEach
    fun setUp() {
        mockEngineConfig = mockEngineConfig()
        testMockEngineFactory = TestMockEngineFactory(mockEngineConfig)

        val config = ApiClientFactoryConfig(
            apiBaseUrl = API_BASE_URL,
            apiKeySecret = ApiKeySecret(SERVICE_API_KEY, SERVICE_API_SECRET),
            logLevel = LogLevel.ALL,
            engineFactory = testMockEngineFactory
        )
        val httpClientBuilder = DefaultHttpClientBuilderImpl()
        apiClient = ApiClientFactory().build(config, httpClientBuilder)
    }

    @KtorExperimentalAPI
    @Test
    fun testTimeApi() = testRunBlocking {
        assertNotNull(apiClient)
        val response = apiClient.time()
        assertNotNull(response)
        assertEquals(1602571690000, response.responseTime)
        assertEquals(1000, response.statusCode)
        assertEquals("Success", response.statusMessage)
        assertNull(response.responseData)
    }

    @KtorExperimentalAPI
    @Test
    fun testUserRequests() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.userRequests(REQUEST_SESSION_TOKEN)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.status, RequestSessionTokenStatus.Authorized)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun serviceDetail() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.serviceDetail(TEST_SERVICE_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.serviceId, TEST_SERVICE_ID)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun serviceTokens() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.serviceTokens()
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.toList()?.get(0)?.serviceId, TEST_SERVICE_ID)

        }
    }

    @KtorExperimentalAPI
    @Test
    fun serviceToken() {
        testRunBlocking {
            val response = apiClient.serviceToken(TEST_SERVICE_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.serviceId, TEST_SERVICE_ID)

        }
    }

    @KtorExperimentalAPI
    @Test
    fun serviceTokenHolders() {
        testRunBlocking {
            val response = apiClient.serviceTokenHolders(TEST_SERVICE_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.toList()?.get(0)?.address, TEST_ADDRESS)

        }
    }

    @KtorExperimentalAPI
    @Test
    fun updateServiceToken() {
        testRunBlocking {
            val response =
                apiClient.updateServiceToken(TEST_SERVICE_TOKEN_CONTRACT_ID, updateServiceTokenRequest)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)

        }
    }

    @KtorExperimentalAPI
    @Test
    fun burnServiceToken() {
        testRunBlocking {
            val response =
                apiClient.burnServiceToken(TEST_SERVICE_TOKEN_CONTRACT_ID, burnServiceTokenRequest)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)

        }
    }

    @KtorExperimentalAPI
    @Test
    fun mintServiceTokenWithToAddress() {
        testRunBlocking {
            val response =
                apiClient.mintServiceToken(TEST_SERVICE_TOKEN_CONTRACT_ID, mintServiceTokenToAddressRequest)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)

        }
    }

    @KtorExperimentalAPI
    @Test
    fun mintServiceTokenWithToUserId() {
        testRunBlocking {
            val response =
                apiClient.mintServiceToken(TEST_SERVICE_TOKEN_CONTRACT_ID, mintServiceTokenToUserIdRequest)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)

        }
    }

    // transaction
    @KtorExperimentalAPI
    @Test
    fun transactionResult() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.transaction(TEST_TX_HASH)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    // memo
    @KtorExperimentalAPI
    @Test
    fun submitMemo() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.saveMemo(memoRequest)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)

        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryMemo() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.queryMemo(TEST_TX_HASH)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)

        }
    }

    // operator-wallet
    @KtorExperimentalAPI
    @Test
    fun queryWallets() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.wallets()
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.wallet(TEST_ADDRESS)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryTransactionsOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.transactionOfWallet(TEST_ADDRESS)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryBaseCoinOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.baseCoinBalanceOfWallet(TEST_ADDRESS)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryServiceTokenBalancesOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.serviceTokenBalancesOfWallet(TEST_ADDRESS)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryServiceTokenBalanceOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.serviceTokenBalanceOfWallet(TEST_ADDRESS, TEST_SERVICE_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun transferBaseCoin() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.transferBaseCoin(TEST_ADDRESS, transferBaseCoinRequest)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun transferServiceToken() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.transferServiceToken(
                    TEST_ADDRESS,
                    TEST_SERVICE_TOKEN_CONTRACT_ID,
                    transferServiceTokenRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryFungibleTokenBalancesOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.fungibleTokensBalanceOfWallet(TEST_ADDRESS, TEST_ITEM_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryFungibleTokenBalanceOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.fungibleTokenBalanceOfWallet(
                    TEST_ADDRESS,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun transferFungibleToken() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.transferFungibleTokenOfWallet(
                    TEST_ADDRESS,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE,
                    transferFungibleTokenRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryNonFungibleTokenBalancesOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.nonFungibleTokenBalancesOfWallet(TEST_ADDRESS, TEST_ITEM_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryNonFungibleTokenIndicesOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.nonFungibleTokenBalancesOfWalletByType(
                    TEST_ADDRESS,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryNonFungibleTokenBalanceOfWallet() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.nonFungibleTokenBalanceOfWallet(
                    TEST_ADDRESS,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE,
                    TEST_TOKEN_INDEX
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun transferNonFungibleToken() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.transferNonFungibleTokenOfWallet(
                    TEST_ADDRESS,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE,
                    TEST_TOKEN_INDEX,
                    transferNonFungibleRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun batchTransferNonFungibleToken() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.batchTransferNonFungibleTokenOfWallet(
                    TEST_ADDRESS,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    batchTransferNonFungibleRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun itemToken() {
        testRunBlocking {
            val response = apiClient.itemToken(TEST_ITEM_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.serviceId, TEST_SERVICE_ID)
            assertEquals(response.responseData?.contractId, TEST_ITEM_TOKEN_CONTRACT_ID)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun fungibleTokens() {
        testRunBlocking {
            val response = apiClient.fungibleTokens(TEST_ITEM_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            val fungibleTokens = response.responseData!!
            fungibleTokens.forEach {
                assertEquals(it.tokenType, TEST_FUNGIBLE_TOKEN_TYPE)
            }
        }
    }

    @KtorExperimentalAPI
    @Test
    fun fungibleToken() {
        testRunBlocking {
            val response = apiClient.fungibleToken(TEST_ITEM_TOKEN_CONTRACT_ID, TEST_FUNGIBLE_TOKEN_TYPE)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData!!.tokenType, TEST_FUNGIBLE_TOKEN_TYPE)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun fungibleTokenHolders() {
        testRunBlocking {
            val response =
                apiClient.fungibleTokenHolders(TEST_ITEM_TOKEN_CONTRACT_ID, TEST_FUNGIBLE_TOKEN_TYPE)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData!!.toList()[0].walletAddress, TEST_ADDRESS)
            assertEquals(response.responseData!!.toList()[0].amount, TEST_AMOUNT)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun createFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.createFungible(TEST_ITEM_TOKEN_CONTRACT_ID, fungibleTokenCreateUpdateRequest)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun updateFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.updateFungible(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_FUNGIBLE_TOKEN_TYPE,
                    fungibleTokenCreateUpdateRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun mintFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.mintFungible(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_FUNGIBLE_TOKEN_TYPE,
                    fungibleTokenMintRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun burnFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.burnFungible(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_FUNGIBLE_TOKEN_TYPE,
                    fungibleTokenItemTokenBurnRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun nonFungibleTokens() {
        testRunBlocking {
            val response =
                apiClient.nonFungibleTokenTypes(TEST_ITEM_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            val nonFungibleTokens = response.responseData!!
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TYPE_NAME, nonFungibleTokens.toList()[0].name)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TYPE_META, nonFungibleTokens.toList()[0].meta)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TYPE, nonFungibleTokens.toList()[0].tokenType)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TOTAL_SUPPLY, nonFungibleTokens.toList()[0].totalSupply)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TOTAL_MINT, nonFungibleTokens.toList()[0].totalMint)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TOTAL_BURN, nonFungibleTokens.toList()[0].totalBurn)

        }
    }

    @KtorExperimentalAPI
    @Test
    fun createNonFungible() {
        testRunBlocking {
            val response =
                apiClient.createNonFungibleType(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    nonFungibleTokenCreateUpdateRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun nonFungibleTokenTypes() {
        testRunBlocking {
            val response =
                apiClient.nonFungibleTokenType(TEST_ITEM_TOKEN_CONTRACT_ID, TEST_NON_FUNGIBLE_TOKEN_TYPE)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            val nonFungibleTokenType = response.responseData!!

            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TYPE_NAME, nonFungibleTokenType.name)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TYPE_META, nonFungibleTokenType.meta)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TYPE, nonFungibleTokenType.tokenType)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TOTAL_SUPPLY.toBigInteger(), nonFungibleTokenType.totalSupply)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TOTAL_MINT.toBigInteger(), nonFungibleTokenType.totalMint)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_TOTAL_BURN.toBigInteger(), nonFungibleTokenType.totalBurn)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_INDEX, nonFungibleTokenType.tokens[0].tokenIndex)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_NAME, nonFungibleTokenType.tokens[0].name)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_META, nonFungibleTokenType.tokens[0].meta)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun updateNonFungibleTokenType() {
        testRunBlocking {
            val response =
                apiClient.updateNonFungibleTokenType(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    nonFungibleTokenCreateUpdateRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun nonFungibleTokenId() {
        testRunBlocking {
            val response =
                apiClient.nonFungibleToken(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            val nonFungibleTokenType = response.responseData!!

            val expectedTokenId = TEST_NON_FUNGIBLE_TOKEN_TYPE + TEST_NON_FUNGIBLE_TOKEN_INDEX
            assertEquals(expectedTokenId, nonFungibleTokenType.tokenId)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_NAME, nonFungibleTokenType.name)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_META, nonFungibleTokenType.meta)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun updateNonFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.updateNonFungibleToken(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX,
                    nonFungibleTokenCreateUpdateRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun mintNonFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.mintNonFungible(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    nonFungibleTokenMintRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun nonFungibleTokenTypeHolders() {
        testRunBlocking {
            val response =
                apiClient.nonFungibleTokenTypeHolders(TEST_ITEM_TOKEN_CONTRACT_ID, TEST_NON_FUNGIBLE_TOKEN_TYPE)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(TEST_ADDRESS, response.responseData!!.toList()[0].walletAddress)
            assertEquals(TEST_NUMBER_OF_INDEX, response.responseData!!.toList()[0].numberOfIndex)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun nonFungibleTokenHolder() {
        testRunBlocking {
            val response =
                apiClient.nonFungibleTokenHolder(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(TEST_ADDRESS, response.responseData!!.walletAddress)
            assertEquals(
                TEST_NON_FUNGIBLE_TOKEN_TYPE + TEST_NON_FUNGIBLE_TOKEN_INDEX,
                response.responseData!!.tokenId
            )
            assertEquals(TEST_AMOUNT, response.responseData!!.amount)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun multiMintNonFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.multiMintNonFungible(TEST_ITEM_TOKEN_CONTRACT_ID, nonFungibleTokenMultiMintRequest)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun burnNonFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.burnNonFungible(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX,
                    nonFungibleTokenItemTokenBurnRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun nonFungibleTokenChildren() {
        testRunBlocking {
            val response =
                apiClient.nonFungibleTokenChildren(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            val nonFungibleTokenIds = response.responseData!!.toList()

            val expectedTokenId = TEST_NON_FUNGIBLE_TOKEN_TYPE + TEST_NON_FUNGIBLE_TOKEN_INDEX
            assertEquals(expectedTokenId, nonFungibleTokenIds[0].tokenId)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_NAME, nonFungibleTokenIds[0].name)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_META, nonFungibleTokenIds[0].meta)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun nonFungibleTokenParent() {
        testRunBlocking {
            val response =
                apiClient.nonFungibleTokenParent(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            val nonFungibleTokenId = response.responseData!!

            val expectedTokenId = TEST_NON_FUNGIBLE_TOKEN_TYPE + TEST_NON_FUNGIBLE_TOKEN_INDEX
            assertEquals(expectedTokenId, nonFungibleTokenId.tokenId)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_NAME, nonFungibleTokenId.name)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_META, nonFungibleTokenId.meta)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun nonFungibleTokenRoot() {
        testRunBlocking {
            val response =
                apiClient.nonFungibleTokenRoot(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            val nonFungibleTokenId = response.responseData!!

            val expectedTokenId = TEST_NON_FUNGIBLE_TOKEN_TYPE + TEST_NON_FUNGIBLE_TOKEN_INDEX
            assertEquals(expectedTokenId, nonFungibleTokenId.tokenId)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_NAME, nonFungibleTokenId.name)
            assertEquals(TEST_NON_FUNGIBLE_TOKEN_META, nonFungibleTokenId.meta)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun attachNonFungibleToken() {
        testRunBlocking {
            val response =
                apiClient.attachNonFungible(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX,
                    nonFungibleTokenItemTokenAttachRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun detachNonFungibleToken() {
        testRunBlocking {
            val request = NonFungibleTokenItemTokenDetachRequest(
                serviceWalletAddress = TEST_ADDRESS,
                serviceWalletSecret = TEST_SECRET,
                tokenHolderAddress = TEST_ADDRESS
            )
            val response =
                apiClient.detachNonFungible(
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    TEST_NON_FUNGIBLE_TOKEN_INDEX,
                    request
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(response.responseData?.txHash, TEST_TX_HASH)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun userDetail() {
        testRunBlocking {
            val response = apiClient.userDetail(TEST_USER_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(TEST_USER_ID, response.responseData?.userId)
            assertEquals(TEST_ADDRESS, response.responseData?.address)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryTransactionsOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.transactionOfUser(TEST_USER_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryBaseCoinOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.baseCoinBalanceOfUser(TEST_USER_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryServiceTokenBalancesOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.serviceTokenBalancesOfUser(TEST_USER_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryServiceTokenBalanceOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.serviceTokenBalanceOfUser(TEST_USER_ID, TEST_SERVICE_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryFungibleTokenBalancesOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.fungibleTokenBalancesOfUser(TEST_USER_ID, TEST_ITEM_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryFungibleTokenBalanceOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.fungibleTokenBalanceOfWallet(
                    TEST_ADDRESS,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryNonFungibleTokenBalancesOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.nonFungibleTokenBalancesOfUser(TEST_USER_ID, TEST_ITEM_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryNonFungibleTokenIndicesOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.nonFungibleTokenBalancesOfUser(
                    TEST_USER_ID,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun queryNonFungibleTokenBalanceOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.nonFungibleTokenBalanceOfUser(
                    TEST_USER_ID,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE,
                    TEST_TOKEN_INDEX
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun requestSessionToken() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.requestSessionToken(TEST_REQUEST_SESSION_TOKEN)
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
            assertEquals(RequestSessionStatus.AUTHORIZED, response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun issueSessionTokenForBaseCoinTransfer() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.issueSessionTokenForBaseCoinTransfer(
                    TEST_USER_ID,
                    RequestType.AOA
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun issueSessionTokenForServiceTokenTransfer() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.issueSessionTokenForServiceTokenTransfer(
                    TEST_USER_ID,
                    TEST_SERVICE_TOKEN_CONTRACT_ID,
                    RequestType.AOA,
                    userServiceTokenTransferRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun issueSessionTokenForItemTokenProxy() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.issueSessionTokenForItemTokenProxy(
                    TEST_USER_ID,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    RequestType.AOA,
                    userAssetProxyRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun issueSessionTokenForServiceTokenProxy() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.issueSessionTokenForServiceTokenProxy(
                    TEST_USER_ID,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    RequestType.AOA,
                    userAssetProxyRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun isProxyOfItemToken() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.isProxyOfItemToken(TEST_USER_ID, TEST_ITEM_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1585467705781, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertTrue(response.responseData?.isApproved ?: false)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun isProxyOfServiceToken() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response = apiClient.isProxyOfServiceToken(TEST_USER_ID, TEST_SERVICE_TOKEN_CONTRACT_ID)
            assertNotNull(response)
            assertEquals(1585467705781, response.responseTime)
            assertEquals(1000, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertTrue(response.responseData?.isApproved ?: false)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun transferServiceTokenOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.transferServiceTokenOfUser(
                    TEST_USER_ID,
                    TEST_SERVICE_TOKEN_CONTRACT_ID,
                    transferTokenOfUserRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun transferFungibleTokenOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.transferFungibleTokenOfUser(
                    TEST_USER_ID,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE,
                    transferTokenOfUserRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun transferNonFungibleTokenOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.transferNonFungibleTokenOfUser(
                    TEST_USER_ID,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    TEST_TOKEN_TYPE,
                    TEST_TOKEN_INDEX,
                    transferNonFungibleOfUserRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun batchTransferNonFungibleTokenOfUser() {
        testRunBlocking {
            assertNotNull(apiClient)
            val response =
                apiClient.batchTransferNonFungibleTokenOfUser(
                    TEST_USER_ID,
                    TEST_ITEM_TOKEN_CONTRACT_ID,
                    batchTransferNonFungibleOfUserRequest
                )
            assertNotNull(response)
            assertEquals(1602571690000, response.responseTime)
            assertEquals(1002, response.statusCode)
            assertEquals("Success", response.statusMessage)
            assertNotNull(response.responseData)
        }
    }


    companion object {
        @Suppress("unused")
        private val json = io.ktor.client.features.json.defaultSerializer()

        private val objectMapper = ObjectMapper().registerModule(KotlinModule())
        const val API_BASE_URL = "http://localhost"
        const val SERVICE_API_KEY = "test-api-key"
        const val SERVICE_API_SECRET = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff"
        const val REQUEST_SESSION_TOKEN = "test-user-session-token"
        const val TEST_SERVICE_ID = "test-service-id"
        const val TEST_SERVICE_TOKEN_CONTRACT_ID = "3336b76f"
        const val TEST_ITEM_TOKEN_CONTRACT_ID = "61e14383"
        const val TEST_FUNGIBLE_TOKEN_TYPE = "0000004a"
        const val TEST_TOKEN_TYPE = "00000001"
        const val TEST_TOKEN_INDEX = "00000003"
        const val TEST_ADDRESS = "tlink1ny5qydw6m4dknqjny55lymf30vtswzwqktjyw7"
        const val TEST_SECRET = "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU="
        const val TEST_AMOUNT = "300"
        const val TEST_WALLET_SECRET = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff"
        const val TEST_TX_HASH = "B4450297E89B9635DBD302D478CA311A7B31D579434B725A96D1D3870B35DFBC"
        private const val TEST_SYMBOL = "TEST"
        private const val TEST_IMG_URL = "http://test-image-uri.com/test-image1"

        private const val TEST_CREATED_AT = 1585353697000

        const val TEST_NON_FUNGIBLE_TOKEN_TYPE_NAME = "y1gcofvx0y86"
        const val TEST_NON_FUNGIBLE_TOKEN_TYPE_META = ""
        const val TEST_NON_FUNGIBLE_TOKEN_TYPE = "10000001"
        const val TEST_NON_FUNGIBLE_TOKEN_TOTAL_SUPPLY = "0"
        const val TEST_NON_FUNGIBLE_TOKEN_TOTAL_MINT = "0"
        const val TEST_NON_FUNGIBLE_TOKEN_TOTAL_BURN = "0"
        const val TEST_NON_FUNGIBLE_TOKEN_INDEX = "00000001"
        const val TEST_NON_FUNGIBLE_TOKEN_NAME = "NFT index name"
        const val TEST_NON_FUNGIBLE_TOKEN_META = "NFT index meta"
        const val TEST_NUMBER_OF_INDEX = "5"

        private const val TEST_USER_ID = "U556719f559479aab8b8f74c488bf6317"
        private const val TEST_REQUEST_SESSION_TOKEN = "PeCfp-wcx55qygg8TwSe_b52szQ"
        private const val TEST_REDIRECT_URI = ""

        private val nonFungibleTokenCreateUpdateRequest = NonFungibleTokenCreateUpdateRequest(
            ownerAddress = "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            ownerSecret = "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            name = "yVvznw2RICXtz11Lw",
            meta = "235v234r01234"
        )

        private val updateServiceTokenRequest = UpdateServiceTokenRequest(
            "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            "name",
            "meta"
        )

        private val fungibleTokenCreateUpdateRequest = FungibleTokenCreateUpdateRequest(
            ownerAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            ownerSecret = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            name = "4W1Vj9U8tYf"
        )

        private val memoRequest = MemoRequest(
            "test-memo",
            "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff"
        )

        private val burnServiceTokenRequest = BurnServiceTokenRequest(
            "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            "10"
        )

        private val fungibleTokenItemTokenBurnRequest = FungibleTokenItemTokenBurnRequest(
            ownerAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            ownerSecret = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            fromAddress = TEST_ADDRESS,
            amount = "1000"
        )

        private val batchTransferNonFungibleOfUserRequest = BatchTransferNonFungibleOfUserRequest(
            ownerAddress = TEST_ADDRESS,
            ownerSecret = TEST_WALLET_SECRET,
            toAddress = TEST_ADDRESS,
            transferList = listOf(TokenId(TEST_TOKEN_TYPE + TEST_TOKEN_INDEX))
        )

        private val nonFungibleTokenItemTokenAttachRequest = NonFungibleTokenItemTokenAttachRequest(
            parentTokenId = TEST_NON_FUNGIBLE_TOKEN_TYPE + TEST_NON_FUNGIBLE_TOKEN_INDEX,
            serviceWalletAddress = TEST_ADDRESS,
            serviceWalletSecret = TEST_SECRET,
            tokenHolderAddress = TEST_ADDRESS
        )

        private val transferTokenOfUserRequest = TransferTokenOfUserRequest(
            ownerAddress = TEST_ADDRESS,
            ownerSecret = TEST_WALLET_SECRET,
            toAddress = TEST_ADDRESS,
            amount = "1000"
        )

        private val transferFungibleTokenRequest = TransferFungibleTokenRequest(
            walletSecret = TEST_WALLET_SECRET,
            toAddress = TEST_ADDRESS,
            amount = "1000"
        )

        private val transferNonFungibleOfUserRequest = TransferNonFungibleOfUserRequest(
            ownerAddress = TEST_ADDRESS,
            ownerSecret = TEST_WALLET_SECRET,
            toAddress = TEST_ADDRESS
        )

        private val mintServiceTokenToAddressRequest = MintServiceTokenRequest(
            ownerAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            ownerSecret = TEST_WALLET_SECRET,
            toAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            amount = "10"
        )

        private val transferBaseCoinRequest = TransferBaseCoinRequest(
            walletSecret = TEST_WALLET_SECRET,
            toAddress = TEST_ADDRESS,
            amount = "1000"
        )

        private val mintServiceTokenToUserIdRequest = MintServiceTokenRequest(
            ownerAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            ownerSecret = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            toUserId = "toUserId",
            amount = "10"
        )

        private val nonFungibleTokenItemTokenBurnRequest = NonFungibleTokenItemTokenBurnRequest(
            ownerAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            ownerSecret = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            fromAddress = TEST_ADDRESS
        )

        private val userAssetProxyRequest = UserAssetProxyRequest(TEST_ADDRESS, TEST_REDIRECT_URI)

        private val nonFungibleTokenMultiMintRequest = NonFungibleTokenMultiMintRequest(
            ownerAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            ownerSecret = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            toAddress = TEST_ADDRESS,
            mintList = listOf(
                MultiMintNonFungible(
                    tokenType = TEST_NON_FUNGIBLE_TOKEN_TYPE,
                    name = TEST_NON_FUNGIBLE_TOKEN_NAME,
                    meta = TEST_NON_FUNGIBLE_TOKEN_META
                )
            )
        )

        private val userServiceTokenTransferRequest = UserServiceTokenTransferRequest(
            toAddress = TEST_ADDRESS,
            amount = TEST_AMOUNT,
            landingUri = TEST_REDIRECT_URI
        )

        private val transferServiceTokenRequest = TransferServiceTokenRequest(
            walletSecret = TEST_WALLET_SECRET,
            toAddress = TEST_ADDRESS,
            amount = "1000"
        )

        private val nonFungibleTokenMintRequest = NonFungibleTokenMintRequest(
            ownerAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            ownerSecret = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            toAddress = TEST_ADDRESS,
            name = TEST_NON_FUNGIBLE_TOKEN_NAME,
            meta = TEST_NON_FUNGIBLE_TOKEN_META
        )

        private val batchTransferNonFungibleRequest = BatchTransferNonFungibleRequest(
            walletSecret = TEST_WALLET_SECRET,
            toAddress = TEST_ADDRESS,
            transferList = listOf(TokenId(TEST_TOKEN_TYPE + TEST_TOKEN_INDEX))
        )

        private val fungibleTokenMintRequest = FungibleTokenMintRequest(
            ownerAddress = "tlink1zakxz8fhzzc4zzhegeg92kr682pvtglhfywls8",
            ownerSecret = "ab3YseZ7Vy0mUY/C+B+KWR6NcWs5MQ9ljBAUdi4aoff",
            toAddress = TEST_ADDRESS,
            amount = "1000"
        )

        private val transferNonFungibleRequest = TransferNonFungibleRequest(
            walletSecret = TEST_WALLET_SECRET,
            toAddress = TEST_ADDRESS
        )

        @Suppress("BlockingMethodInNonBlockingContext")
        private fun mockEngineConfig(): MockEngineConfig {
            return MockEngineConfig().apply {
                addHandler { request ->
                    when (request.method) {
                        HttpMethod.Get -> when (request.url.fullPath) {
                            TIME_API_PATH -> {
                                respond(
                                    content = timeResponse(),
                                    headers = headers()
                                )
                            }
                            USER_REQUESTS_PATH.replace("{requestSessionToken}", REQUEST_SESSION_TOKEN) -> {
                                respond(
                                    content = userRequestStatusResponse(),
                                    headers = headers()
                                )
                            }
                            SERVICE_DETAIL_API_PATH.replace("{serviceId}", TEST_SERVICE_ID) -> {
                                respond(
                                    content = serviceDetail(TEST_SERVICE_ID),
                                    headers = headers()
                                )
                            }
                            SERVICE_TOKENS_PATH -> {
                                respond(
                                    content = serviceTokens(TEST_SERVICE_ID),
                                    headers = headers()
                                )
                            }
                            SERVICE_TOKEN_PATH.replace("{contractId}", TEST_SERVICE_TOKEN_CONTRACT_ID) -> {
                                respond(
                                    content = serviceToken(TEST_SERVICE_ID, TEST_SERVICE_TOKEN_CONTRACT_ID),
                                    headers = headers()
                                )
                            }
                            SERVICE_TOKEN_HOLDERS_PATH
                                .replace("{contractId}", TEST_SERVICE_TOKEN_CONTRACT_ID)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = serviceTokenHolders(),
                                    headers = headers()
                                )
                            }
                            TRANSACTION_PATH.replace("{txHash}", TEST_TX_HASH) -> {
                                respond(
                                    content = txResultResponse(),
                                    headers = headers()
                                )
                            }
                            MEMO_BY_TX_HASH_PATH.replace("{txHash}", TEST_TX_HASH) -> {
                                respond(
                                    content = memoResponse(),
                                    headers = headers()
                                )
                            }
                            WALLET_LIST_PATH -> {
                                respond(
                                    content = walletList(),
                                    headers = headers()
                                )
                            }
                            WALLET_PATH.replace("{walletAddress}", TEST_ADDRESS) -> {
                                respond(
                                    content = wallet(),
                                    headers = headers()
                                )
                            }
                            WALLET_BASE_COIN_BALANCE_PATH.replace("{walletAddress}", TEST_ADDRESS),
                            USER_BASE_COIN_BALANCE_PATH.replace("{userId}", TEST_USER_ID) -> {
                                respond(
                                    content = baseCoinBalance(),
                                    headers = headers()
                                )
                            }
                            WALLET_SERVICE_TOKENS_BALANCE_PATH.replace("{walletAddress}", TEST_ADDRESS)
                                .plus("?limit=10&page=1&orderBy=asc"),
                            USER_SERVICE_TOKENS_BALANCE_PATH.replace("{userId}", TEST_USER_ID)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = serviceTokenBalances(),
                                    headers = headers()
                                )
                            }
                            WALLET_SERVICE_TOKEN_BALANCE_PATH
                                .replace("{walletAddress}", TEST_ADDRESS)
                                .replace("{contractId}", TEST_SERVICE_TOKEN_CONTRACT_ID),
                            USER_SERVICE_TOKEN_BALANCE_PATH
                                .replace("{userId}", TEST_USER_ID)
                                .replace("{contractId}", TEST_SERVICE_TOKEN_CONTRACT_ID) -> {
                                respond(
                                    content = serviceTokenBalance(),
                                    headers = headers()
                                )
                            }
                            WALLET_FUNGIBLE_TOKENS_BALANCE_PATH
                                .replace("{walletAddress}", TEST_ADDRESS)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .plus("?limit=10&page=1&orderBy=asc"),
                            USER_FUNGIBLE_TOKENS_BALANCE_PATH
                                .replace("{userId}", TEST_USER_ID)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = fungibleTokensBalance(),
                                    headers = headers()
                                )
                            }
                            WALLET_FUNGIBLE_TOKEN_BALANCE_PATH
                                .replace("{walletAddress}", TEST_ADDRESS)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_TOKEN_TYPE),
                            USER_FUNGIBLE_TOKEN_BALANCE_PATH
                                .replace("{userId}", TEST_USER_ID)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_TOKEN_TYPE) -> {
                                respond(
                                    content = fungibleTokenBalance(),
                                    headers = headers()
                                )
                            }
                            WALLET_NON_FUNGIBLE_TOKENS_BALANCE_PATH
                                .replace("{walletAddress}", TEST_ADDRESS)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .plus("?limit=10&page=1&orderBy=asc"),
                            USER_NON_FUNGIBLE_TOKENS_BALANCE_PATH
                                .replace("{userId}", TEST_USER_ID)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = nonFungibleTokensBalance(),
                                    headers = headers()
                                )
                            }
                            WALLET_NON_FUNGIBLE_TOKEN_BALANCES_BY_TYPE_PATH
                                .replace("{walletAddress}", TEST_ADDRESS)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_TOKEN_TYPE)
                                .plus("?limit=10&page=1&orderBy=asc"),
                            USER_NON_FUNGIBLE_TOKEN_BALANCES_BY_TYPE_PATH
                                .replace("{userId}", TEST_USER_ID)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_TOKEN_TYPE)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = nonFungibleTokenIndices(),
                                    headers = headers()
                                )
                            }
                            WALLET_NON_FUNGIBLE_TOKEN_BALANCE_PATH
                                .replace("{walletAddress}", TEST_ADDRESS)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_TOKEN_TYPE)
                                .replace("{tokenIndex}", TEST_TOKEN_INDEX),
                            USER_NON_FUNGIBLE_TOKEN_BALANCE_PATH
                                .replace("{userId}", TEST_USER_ID)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_TOKEN_TYPE)
                                .replace("{tokenIndex}", TEST_TOKEN_INDEX) -> {
                                respond(
                                    content = nonFungibleTokenIndex(),
                                    headers = headers()
                                )
                            }
                            WALLET_TRANSACTIONS_PATH.replace("{walletAddress}", TEST_ADDRESS)
                                .plus("?limit=10&page=1&orderBy=asc"),
                            USER_TRANSACTIONS_PATH.replace("{userId}", TEST_USER_ID)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = txResultsResponse(),
                                    headers = headers()
                                )
                            }
                            ITEM_TOKEN_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID) -> {
                                respond(
                                    content = itemTokenInfoResponse(),
                                    headers = headers()
                                )
                            }
                            FUNGIBLE_TOKENS_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = fungibleTokensResponse(),
                                    headers = headers()
                                )
                            }
                            FUNGIBLE_TOKEN_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_FUNGIBLE_TOKEN_TYPE) -> {
                                respond(
                                    content = fungibleTokenResponse(),
                                    headers = headers()
                                )
                            }
                            FUNGIBLE_TOKEN_HOLDERS_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_FUNGIBLE_TOKEN_TYPE)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = fungibleTokenHoldersResponse(),
                                    headers = headers()
                                )
                            }
                            NON_FUNGIBLE_TOKENS_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = nonFungibleTokensResponse(),
                                    headers = headers()
                                )
                            }
                            NON_FUNGIBLE_TOKEN_TYPE_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = nonFungibleTokenTypesResponse(),
                                    headers = headers()
                                )
                            }
                            NON_FUNGIBLE_TOKEN_ID_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                .replace("{tokenIndex}", TEST_NON_FUNGIBLE_TOKEN_INDEX) -> {
                                respond(
                                    content = nonFungibleTokenTypeIdResponse(),
                                    headers = headers()
                                )
                            }
                            NON_FUNGIBLE_TOKEN_TYPE_HOLDERS_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = nonFungibleTokenTypeHolders(),
                                    headers = headers()
                                )
                            }
                            NON_FUNGIBLE_TOKEN_HOLDER_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                .replace("{tokenIndex}", TEST_NON_FUNGIBLE_TOKEN_INDEX) -> {
                                respond(
                                    content = nonFungibleTokenHolder(),
                                    headers = headers()
                                )
                            }
                            NON_FUNGIBLE_TOKEN_CHILDREN_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                .replace("{tokenIndex}", TEST_NON_FUNGIBLE_TOKEN_INDEX)
                                .plus("?limit=10&page=1&orderBy=asc") -> {
                                respond(
                                    content = nonFungibleTokenTypeIdsResponse(),
                                    headers = headers()
                                )
                            }
                            NON_FUNGIBLE_TOKEN_PARENT_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                .replace("{tokenIndex}", TEST_NON_FUNGIBLE_TOKEN_INDEX) -> {
                                respond(
                                    content = nonFungibleTokenTypeIdResponse(),
                                    headers = headers()
                                )
                            }
                            NON_FUNGIBLE_TOKEN_ROOT_PATH
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                .replace("{tokenIndex}", TEST_NON_FUNGIBLE_TOKEN_INDEX) -> {
                                respond(
                                    content = nonFungibleTokenTypeIdResponse(),
                                    headers = headers()
                                )
                            }
                            USER_DETAIL_PATH.replace("{userId}", TEST_USER_ID) -> {
                                respond(
                                    content = userDetailResponse(),
                                    headers = headers()
                                )
                            }
                            REQUEST_SESSION_TOKEN_PATH.replace(
                                "{requestSessionToken}", TEST_REQUEST_SESSION_TOKEN
                            ) -> {
                                respond(
                                    content = requestSessionToken(),
                                    headers = headers()
                                )
                            }
                            USER_ITEM_TOKEN_IS_PROXY_PATH
                                .replace("{userId}", TEST_USER_ID)
                                .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID),
                            USER_SERVICE_TOKEN_IS_PROXY_PATH
                                .replace("{userId}", TEST_USER_ID)
                                .replace("{contractId}", TEST_SERVICE_TOKEN_CONTRACT_ID) -> {
                                respond(
                                    content = isItemTokenProxy(),
                                    headers = headers()
                                )
                            }

                            else -> error("Unhandled ${request.url}")
                        }
                        HttpMethod.Put -> {
                            when (request.url.fullPath to (request.body as TextContent).text) {
                                SERVICE_TOKEN_PATH.replace("{contractId}", TEST_SERVICE_TOKEN_CONTRACT_ID)
                                        to objectMapper.writeValueAsString(updateServiceTokenRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                FUNGIBLE_TOKEN_UPDATE_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_FUNGIBLE_TOKEN_TYPE)
                                        to objectMapper.writeValueAsString(fungibleTokenCreateUpdateRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                NON_FUNGIBLE_TOKEN_TYPE_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                        to objectMapper.writeValueAsString(nonFungibleTokenCreateUpdateRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                NON_FUNGIBLE_TOKEN_ID_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                    .replace("{tokenIndex}", TEST_NON_FUNGIBLE_TOKEN_INDEX)
                                        to objectMapper.writeValueAsString(nonFungibleTokenCreateUpdateRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                else -> error("Unhandled ${request.url}")
                            }
                        }
                        HttpMethod.Post -> {
                            when (request.url.fullPath to if (request.body is TextContent) (request.body as TextContent).text else "") {
                                SERVICE_TOKEN_BURN_PATH.replace(
                                    "{contractId}",
                                    TEST_SERVICE_TOKEN_CONTRACT_ID
                                ) to objectMapper.writeValueAsString(burnServiceTokenRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                SERVICE_TOKEN_MINT_PATH.replace(
                                    "{contractId}",
                                    TEST_SERVICE_TOKEN_CONTRACT_ID
                                ) to objectMapper.writeValueAsString(mintServiceTokenToAddressRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                SERVICE_TOKEN_MINT_PATH.replace(
                                    "{contractId}",
                                    TEST_SERVICE_TOKEN_CONTRACT_ID
                                ) to objectMapper.writeValueAsString(mintServiceTokenToUserIdRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                MEMO_PATH to objectMapper.writeValueAsString(memoRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                BASE_COIN_TRANSFER_PATH
                                    .replace(
                                        "{walletAddress}",
                                        TEST_ADDRESS
                                    ) to objectMapper.writeValueAsString(transferBaseCoinRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                SERVICE_TOKEN_TRANSFER_PATH
                                    .replace("{walletAddress}", TEST_ADDRESS)
                                    .replace(
                                        "{contractId}",
                                        TEST_SERVICE_TOKEN_CONTRACT_ID
                                    ) to objectMapper.writeValueAsString(transferServiceTokenRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                WALLET_FUNGIBLE_TOKEN_TRANSFER_PATH
                                    .replace("{walletAddress}", TEST_ADDRESS)
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_TOKEN_TYPE)
                                        to objectMapper.writeValueAsString(transferFungibleTokenRequest),
                                USER_FUNGIBLE_TOKEN_TRANSFER_PATH
                                    .replace("{userId}", TEST_USER_ID)
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_TOKEN_TYPE)
                                        to objectMapper.writeValueAsString(transferTokenOfUserRequest),
                                USER_SERVICE_TOKEN_TRANSFER_PATH
                                    .replace("{userId}", TEST_USER_ID)
                                    .replace(
                                        "{contractId}",
                                        TEST_SERVICE_TOKEN_CONTRACT_ID
                                    ) to objectMapper.writeValueAsString(transferTokenOfUserRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }

                                WALLET_NON_FUNGIBLE_TOKEN_TRANSFER_PATH
                                    .replace("{walletAddress}", TEST_ADDRESS)
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_TOKEN_TYPE)
                                    .replace(
                                        "{tokenIndex}",
                                        TEST_TOKEN_INDEX
                                    ) to objectMapper.writeValueAsString(transferNonFungibleRequest),
                                USER_NON_FUNGIBLE_TOKEN_TRANSFER_PATH
                                    .replace("{userId}", TEST_USER_ID)
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_TOKEN_TYPE)
                                    .replace(
                                        "{tokenIndex}",
                                        TEST_TOKEN_INDEX
                                    ) to objectMapper.writeValueAsString(transferNonFungibleOfUserRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                WALLET_NON_FUNGIBLE_TOKEN_BATCH_TRANSFER_PATH
                                    .replace("{walletAddress}", TEST_ADDRESS)
                                    .replace(
                                        "{contractId}",
                                        TEST_ITEM_TOKEN_CONTRACT_ID
                                    ) to objectMapper.writeValueAsString(batchTransferNonFungibleRequest),
                                USER_NON_FUNGIBLE_TOKEN_BATCH_TRANSFER_PATH
                                    .replace("{userId}", TEST_USER_ID)
                                    .replace(
                                        "{contractId}",
                                        TEST_ITEM_TOKEN_CONTRACT_ID
                                    ) to objectMapper.writeValueAsString(batchTransferNonFungibleOfUserRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                FUNGIBLE_TOKEN_CREATE_PATH
                                    .replace(
                                        "{contractId}",
                                        TEST_ITEM_TOKEN_CONTRACT_ID
                                    ) to objectMapper.writeValueAsString(fungibleTokenCreateUpdateRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                FUNGIBLE_TOKEN_MINT_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace(
                                        "{tokenType}",
                                        TEST_FUNGIBLE_TOKEN_TYPE
                                    ) to objectMapper.writeValueAsString(fungibleTokenMintRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                FUNGIBLE_TOKEN_BURN_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace(
                                        "{tokenType}",
                                        TEST_FUNGIBLE_TOKEN_TYPE
                                    ) to objectMapper.writeValueAsString(fungibleTokenItemTokenBurnRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                NON_FUNGIBLE_TOKENS_PATH
                                    .replace(
                                        "{contractId}",
                                        TEST_ITEM_TOKEN_CONTRACT_ID
                                    ) to objectMapper.writeValueAsString(nonFungibleTokenCreateUpdateRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                NON_FUNGIBLE_TOKEN_MINT_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace(
                                        "{tokenType}",
                                        TEST_NON_FUNGIBLE_TOKEN_TYPE
                                    ) to objectMapper.writeValueAsString(nonFungibleTokenMintRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                NON_FUNGIBLE_TOKEN_MULTI_MINT_PATH
                                    .replace(
                                        "{contractId}",
                                        TEST_ITEM_TOKEN_CONTRACT_ID
                                    ) to objectMapper.writeValueAsString(nonFungibleTokenMultiMintRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                NON_FUNGIBLE_TOKEN_BURN_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                    .replace(
                                        "{tokenIndex}",
                                        TEST_NON_FUNGIBLE_TOKEN_INDEX
                                    ) to objectMapper.writeValueAsString(nonFungibleTokenItemTokenBurnRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                NON_FUNGIBLE_TOKEN_PARENT_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                    .replace(
                                        "{tokenIndex}",
                                        TEST_NON_FUNGIBLE_TOKEN_INDEX
                                    ) to objectMapper.writeValueAsString(nonFungibleTokenItemTokenAttachRequest) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                ISSUE_SESSION_TOKEN_FOR_BASE_COIN_PATH
                                    .replace("{userId}", TEST_USER_ID)
                                    .plus("?requestType=aoa") to "" -> {
                                    respond(
                                        content = requestSession(),
                                        headers = headers()
                                    )
                                }
                                ISSUE_SESSION_TOKEN_FOR_SERVICE_TOKEN_PATH
                                    .replace("{userId}", TEST_USER_ID)
                                    .replace("{contractId}", TEST_SERVICE_TOKEN_CONTRACT_ID)
                                    .plus("?requestType=aoa")
                                        to objectMapper.writeValueAsString(userServiceTokenTransferRequest) -> {
                                    respond(
                                        content = requestSession(),
                                        headers = headers()
                                    )
                                }
                                ISSUE_SESSION_TOKEN_FOR_ITEM_TOKEN_PROXY
                                    .replace("{userId}", TEST_USER_ID)
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .plus("?requestType=aoa")
                                        to objectMapper.writeValueAsString(userAssetProxyRequest),
                                ISSUE_SESSION_TOKEN_FOR_SERVICE_TOKEN_PROXY
                                    .replace("{userId}", TEST_USER_ID)
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .plus("?requestType=aoa")
                                        to objectMapper.writeValueAsString(userAssetProxyRequest) -> {
                                    respond(
                                        content = requestSession(),
                                        headers = headers()
                                    )
                                }
                                else -> error("Unhandled ${request.url}")
                            }
                        }
                        HttpMethod.Delete -> {
                            when (request.url.fullPath) {
                                NON_FUNGIBLE_TOKEN_PARENT_PATH
                                    .replace("{contractId}", TEST_ITEM_TOKEN_CONTRACT_ID)
                                    .replace("{tokenType}", TEST_NON_FUNGIBLE_TOKEN_TYPE)
                                    .replace("{tokenIndex}", TEST_NON_FUNGIBLE_TOKEN_INDEX) -> {
                                    respond(
                                        content = transactionResponse(),
                                        headers = headers()
                                    )
                                }
                                else -> error("Unhandled ${request.url}")
                            }
                        }
                        else -> error("Unhandled ${request.method}")
                    }

                }
            }
        }

        private fun headers(): Headers {
            return headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
        }

        private fun timeResponse(): String {
            return objectMapper.writeValueAsString(
                GenericResponse<Unit>(
                    1602571690000,
                    1000,
                    "Success"
                )
            )
        }

        private fun userRequestStatusResponse(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    UserRequestStatus(RequestSessionTokenStatus.Authorized)
                )
            )
        }

        private fun serviceDetail(serviceId: String): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    ServiceDetail(serviceId, "name", "description", "CATEGORY")
                )
            )
        }

        private fun serviceTokens(serviceId: String): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(
                        testServiceToken(serviceId)
                    )
                )
            )
        }

        private fun serviceToken(serviceId: String, contractId: String): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    testServiceToken(serviceId, contractId)
                )
            )
        }

        private fun serviceTokenHolders(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(
                        ServiceTokenHolder(TEST_ADDRESS, null, "44")
                    )
                )
            )
        }

        private fun txResultResponse(): String {
            return loadJsonToString("./data/tx-result.json", this::class.java)
        }

        private fun txResultsResponse(): String {
            return loadJsonToString("./data/tx-results.json", this::class.java)
        }

        private fun memoResponse(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    Memo("test-memo")
                )
            )
        }

        private fun walletList(): String {
            return loadJsonToString("./data/operator-wallet-list.json", this::class.java)
        }

        private fun wallet(): String {
            return loadJsonToString("./data/operator-wallet.json", this::class.java)
        }

        private fun baseCoinBalance(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    BaseCoinBalance(
                        "TC", 6, "1000000"
                    )
                )
            )
        }

        private fun serviceTokenBalances(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(testServiceTokenBalance())
                )
            )
        }

        private fun fungibleTokensBalance(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(testFungibleTokenBalance())
                )
            )
        }

        private fun fungibleTokenBalance(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    testFungibleTokenBalance()
                )
            )
        }

        private fun nonFungibleTokensBalance(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(testNonFungibleTokenBalance())
                )
            )
        }

        private fun nonFungibleTokenIndices(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(testNonFungibleTokenIndex())
                )
            )
        }

        private fun nonFungibleTokenIndex(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    testNonFungibleTokenIndex()
                )
            )
        }

        private fun itemTokenInfoResponse(): String {

            val itemToken = ItemToken(
                "61e14383",
                "https://image-base-uri/",
                "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
                TEST_SERVICE_ID,
                1584070104000
            )
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    itemToken
                )
            )
        }

        private fun fungibleTokensResponse(): String {
            val fungibleToken = fungibleToken()
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(fungibleToken)
                )
            )
        }

        private fun fungibleToken(): FungibleToken {
            return FungibleToken(
                "TOKEN0313",
                "",
                TEST_FUNGIBLE_TOKEN_TYPE,
                "0",
                "0",
                "0",
                1585378323000
            )
        }

        private fun fungibleTokenHolders(): Collection<FungibleTokenHolder> {
            return listOf(
                FungibleTokenHolder(
                    null,
                    TEST_ADDRESS,
                    TEST_AMOUNT
                )
            )
        }

        private fun fungibleTokenResponse(): String {
            val fungibleToken = fungibleToken()
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    fungibleToken
                )
            )
        }

        private fun fungibleTokenHoldersResponse(): String {
            val fungibleTokenHolders = fungibleTokenHolders()
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    fungibleTokenHolders
                )
            )
        }

        private fun nonFungibleTokensResponse(): String {
            val nonFungibleToken = nonFungibleToken()
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(nonFungibleToken)
                )
            )
        }

        private fun nonFungibleTokenTypesResponse(): String {
            val nonFungibleToken = nonFungibleTokenType()
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    nonFungibleToken
                )
            )
        }

        private fun nonFungibleTokenTypeIdResponse(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    nonFungibleId()
                )
            )
        }

        private fun nonFungibleId(): NonFungibleId {
            return NonFungibleId(
                TEST_NON_FUNGIBLE_TOKEN_TYPE + TEST_NON_FUNGIBLE_TOKEN_INDEX,
                TEST_NON_FUNGIBLE_TOKEN_NAME,
                TEST_NON_FUNGIBLE_TOKEN_META,
                TEST_CREATED_AT,
                null
            )
        }

        private fun nonFungibleTokenTypeIdsResponse(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(nonFungibleId())
                )
            )
        }

        private fun nonFungibleTokenTypeHolders(): String {
            val holders = NonFungibleTokenTypeHolder(
                null,
                TEST_ADDRESS,
                TEST_NUMBER_OF_INDEX
            )
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    listOf(holders)
                )
            )
        }

        private fun nonFungibleTokenHolder(): String {
            val holder = NonFungibleTokenHolder(
                TEST_NON_FUNGIBLE_TOKEN_TYPE + TEST_NON_FUNGIBLE_TOKEN_INDEX,
                null,
                TEST_ADDRESS,
                TEST_AMOUNT
            )
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    holder
                )
            )
        }

        private fun nonFungibleToken(): ItemTokenType {
            return ItemTokenType(
                TEST_NON_FUNGIBLE_TOKEN_TYPE_NAME,
                TEST_NON_FUNGIBLE_TOKEN_TYPE_META,
                TEST_NON_FUNGIBLE_TOKEN_TYPE,
                TEST_NON_FUNGIBLE_TOKEN_TOTAL_SUPPLY,
                TEST_NON_FUNGIBLE_TOKEN_TOTAL_MINT,
                TEST_NON_FUNGIBLE_TOKEN_TOTAL_BURN,
                TEST_CREATED_AT
            )
        }

        private fun nonFungibleTokenType(): NonFungibleTokenType {
            return NonFungibleTokenType(
                TEST_NON_FUNGIBLE_TOKEN_TYPE_NAME,
                TEST_NON_FUNGIBLE_TOKEN_TYPE_META,
                TEST_NON_FUNGIBLE_TOKEN_TYPE,
                TEST_NON_FUNGIBLE_TOKEN_TOTAL_SUPPLY.toBigInteger(), // why bigInteger?
                TEST_NON_FUNGIBLE_TOKEN_TOTAL_MINT.toBigInteger(),
                TEST_NON_FUNGIBLE_TOKEN_TOTAL_BURN.toBigInteger(),
                TEST_CREATED_AT,
                listOf(
                    NonFungibleIndex(
                        TEST_NON_FUNGIBLE_TOKEN_INDEX,
                        TEST_NON_FUNGIBLE_TOKEN_NAME,
                        TEST_NON_FUNGIBLE_TOKEN_META,
                        1584075664000,
                        null
                    )
                )
            )
        }

        private fun serviceTokenBalance(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    testServiceTokenBalance()
                )
            )
        }

        private fun testServiceTokenBalance(): ServiceTokenBalance {
            return ServiceTokenBalance(
                TEST_SERVICE_TOKEN_CONTRACT_ID,
                "test-svc-token1",
                TEST_SYMBOL,
                TEST_IMG_URL,
                4,
                "1000"
            )
        }

        private fun testFungibleTokenBalance(): FungibleBalance {
            return FungibleBalance(
                "00000001",
                "test-fungible-token",
                "",
                "10"
            )
        }

        private fun testNonFungibleTokenBalance(): NonFungibleBalance {
            return NonFungibleBalance(
                "00000001",
                "test-fungible-token",
                "",
                TEST_TOKEN_INDEX
            )
        }

        private fun testNonFungibleTokenIndex(): TokenIndex {
            return TokenIndex(TEST_TOKEN_INDEX, "test", "")
        }

        private fun transactionResponse(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1002,
                    "Success",
                    TransactionResponse(TEST_TX_HASH)
                )
            )
        }

        private fun testServiceToken(serviceId: String, contractId: String = "contractId"): ServiceToken {

            return ServiceToken(
                contractId,
                TEST_ADDRESS,
                LocalDateTime.now().toEpochMilli(),
                serviceId,
                6,
                "name",
                "symbol",
                "meta",
                "imgUri",
                "1000040",
                "1000042",
                "2"
            )
        }

        private fun userDetailResponse(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1002,
                    "Success",
                    UserIdAddress(TEST_USER_ID, TEST_ADDRESS)
                )
            )
        }

        private fun requestSessionToken(): String {
            return """
                {
                    "responseTime": 1602571690000,
                    "statusCode": 1000,
                    "statusMessage": "Success",
                    "responseData": {
                        "status": "Authorized"
                    }
                }
            """.trimIndent()
        }

        private fun isItemTokenProxy(): String {
            return """
                {
                    "responseTime": 1585467705781,
                    "statusCode": 1000,
                    "statusMessage": "Success",
                    "responseData": {
                        "isApproved": true
                    }
                }
            """.trimIndent()
        }

        private fun requestSession(): String {
            return objectMapper.writeValueAsString(
                GenericResponse(
                    1602571690000,
                    1000,
                    "Success",
                    RequestSession(TEST_REQUEST_SESSION_TOKEN, TEST_REDIRECT_URI)
                )
            )
        }
    }

    class TestMockEngineFactory(private val config: MockEngineConfig) :
        HttpClientEngineFactory<MockEngineConfig> {
        override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine {
            return MockEngine.Companion.create {
                config.requestHandlers.forEach { this.addHandler(it) }
            }
        }
    }
}

