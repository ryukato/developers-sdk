@file:Suppress("DuplicatedCode")

package com.yyoo.link.developers.sdk.api

import com.yyoo.link.developers.sdk.model.request.*
import com.yyoo.link.developers.sdk.model.response.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope

internal class ApiClientImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient
) : ApiClient {
    private val json = io.ktor.client.features.json.defaultSerializer()
    override suspend fun time(): GenericResponse<Unit> = coroutineScope {
        httpClient.get<GenericResponse<Unit>> {
            url("$baseUrl$TIME_API_PATH")
        }
    }

    // service
    override suspend fun serviceDetail(serviceId: String): GenericResponse<ServiceDetail> {
        val path = buildApiPath(SERVICE_DETAIL_API_PATH, mapOf("serviceId" to serviceId))

        return coroutineScope {
            httpClient.get<GenericResponse<ServiceDetail>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun userRequests(
        requestSessionToken: String
    ): GenericResponse<UserRequestStatus> {
        val path = buildApiPath(USER_REQUESTS_PATH, mapOf("requestSessionToken" to requestSessionToken))
        return coroutineScope {
            httpClient.get<GenericResponse<UserRequestStatus>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    // service-token
    override suspend fun serviceTokens(): GenericResponse<Collection<ServiceToken>> =
        coroutineScope {
            httpClient.get<GenericResponse<Collection<ServiceToken>>> {
                addTimeStampHeader(this)
                url("$baseUrl$SERVICE_TOKENS_PATH")
            }
        }

    override suspend fun serviceToken(contractId: String): GenericResponse<ServiceToken> =
        coroutineScope {
            httpClient.get<GenericResponse<ServiceToken>> {
                addTimeStampHeader(this)
                url("$baseUrl${SERVICE_TOKEN_PATH.replace("{contractId}", contractId)}")
            }
        }

    override suspend fun serviceTokenHolders(
        contractId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<ServiceTokenHolder>> {
        val path = SERVICE_TOKEN_HOLDERS_PATH.replace("{contractId}", contractId)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<ServiceTokenHolder>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun updateServiceToken(
        contractId: String,
        request: UpdateServiceTokenRequest
    ): GenericResponse<TransactionResponse> {
        val path = SERVICE_TOKEN_PATH.replace("{contractId}", contractId)
        return coroutineScope {
            httpClient.put<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun burnServiceToken(
        contractId: String,
        request: BurnServiceTokenRequest
    ): GenericResponse<TransactionResponse> {
        val path = SERVICE_TOKEN_BURN_PATH.replace("{contractId}", contractId)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun mintServiceToken(
        contractId: String,
        request: MintServiceTokenRequest
    ): GenericResponse<TransactionResponse> = coroutineScope {
        httpClient.post<GenericResponse<TransactionResponse>> {
            addTimeStampHeader(this)
            url("$baseUrl${SERVICE_TOKEN_MINT_PATH.replace("{contractId}", contractId)}")
            this.body = json.write(request)
        }
    }

    // transaction
    override suspend fun transaction(txHash: String): GenericResponse<TxResultResponse> = coroutineScope {
        httpClient.get<GenericResponse<TxResultResponse>> {
            addTimeStampHeader(this)
            url("$baseUrl$TRANSACTION_PATH".replace("{txHash}", txHash))
        }
    }

    // memo
    override suspend fun saveMemo(request: MemoRequest): GenericResponse<TransactionResponse> = coroutineScope {
        httpClient.post<GenericResponse<TransactionResponse>> {
            addTimeStampHeader(this)
            url("$baseUrl$MEMO_PATH")
            this.body = json.write(request)
        }
    }

    override suspend fun queryMemo(txHash: String): GenericResponse<Memo> = coroutineScope {
        httpClient.get<GenericResponse<Memo>> {
            addTimeStampHeader(this)
            url("$baseUrl$MEMO_BY_TX_HASH_PATH".replace("{txHash}", txHash))
        }
    }

    // wallet
    override suspend fun wallets(): GenericResponse<Collection<WalletResponse>> = coroutineScope {
        httpClient.get<GenericResponse<Collection<WalletResponse>>> {
            addTimeStampHeader(this)
            url("$baseUrl$WALLET_LIST_PATH")
        }
    }

    override suspend fun wallet(walletAddress: String): GenericResponse<WalletResponse> = coroutineScope {
        httpClient.get<GenericResponse<WalletResponse>> {
            addTimeStampHeader(this)
            url("$baseUrl${WALLET_PATH.replace("{walletAddress}", walletAddress)}")
        }
    }

    override suspend fun transactionOfWallet(
        walletAddress: String,
        after: Long?,
        before: Long?,
        limit: Int,
        msgType: String?,
        orderBy: OrderBy,
        page: Int
    ): GenericResponse<Collection<TxResultResponse>> {
        val path = WALLET_TRANSACTIONS_PATH.replace("{walletAddress}", walletAddress)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<TxResultResponse>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun baseCoinBalanceOfWallet(walletAddress: String): GenericResponse<BaseCoinBalance> {
        val path = WALLET_BASE_COIN_BALANCE_PATH.replace("{walletAddress}", walletAddress)
        return coroutineScope {
            httpClient.get<GenericResponse<BaseCoinBalance>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    // wallet-balance
    override suspend fun serviceTokenBalancesOfWallet(
        walletAddress: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<ServiceTokenBalance>> {
        val path = WALLET_SERVICE_TOKENS_BALANCE_PATH.replace("{walletAddress}", walletAddress)
        val urlString = "$baseUrl$path"

        return coroutineScope {
            httpClient.get<GenericResponse<Collection<ServiceTokenBalance>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun transferBaseCoin(
        walletAddress: String,
        request: TransferBaseCoinRequest
    ): GenericResponse<TransactionResponse> {
        val path = BASE_COIN_TRANSFER_PATH
            .replace("{walletAddress}", walletAddress)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    // wallet-balance
    override suspend fun serviceTokenBalanceOfWallet(
        walletAddress: String,
        contractId: String
    ): GenericResponse<ServiceTokenBalance> {
        val path = WALLET_SERVICE_TOKEN_BALANCE_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)
        return coroutineScope {
            httpClient.get<GenericResponse<ServiceTokenBalance>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun transferServiceToken(
        walletAddress: String,
        contractId: String,
        request: TransferServiceTokenRequest
    ): GenericResponse<TransactionResponse> {
        val path = SERVICE_TOKEN_TRANSFER_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun fungibleTokensBalanceOfWallet(
        walletAddress: String,
        contractId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<FungibleBalance>> {
        val path = WALLET_FUNGIBLE_TOKENS_BALANCE_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)

        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<FungibleBalance>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun fungibleTokenBalanceOfWallet(
        walletAddress: String,
        contractId: String,
        tokenType: String
    ): GenericResponse<FungibleBalance> =
        coroutineScope {
            val path = WALLET_FUNGIBLE_TOKEN_BALANCE_PATH
                .replace("{walletAddress}", walletAddress)
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
            httpClient.get<GenericResponse<FungibleBalance>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }

    override suspend fun transferFungibleTokenOfWallet(
        walletAddress: String,
        contractId: String,
        tokenType: String,
        request: TransferFungibleTokenRequest
    ): GenericResponse<TransactionResponse> {
        val path = WALLET_FUNGIBLE_TOKEN_TRANSFER_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun nonFungibleTokenBalancesOfWallet(
        walletAddress: String,
        contractId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<NonFungibleBalance>> {
        val path = WALLET_NON_FUNGIBLE_TOKENS_BALANCE_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<NonFungibleBalance>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun nonFungibleTokenBalancesOfWalletByType(
        walletAddress: String,
        contractId: String,
        tokenType: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<TokenIndex>> {
        val path = WALLET_NON_FUNGIBLE_TOKEN_BALANCES_BY_TYPE_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)

        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<TokenIndex>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun nonFungibleTokenBalanceOfWallet(
        walletAddress: String,
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<TokenIndex> = coroutineScope {
        val path = WALLET_NON_FUNGIBLE_TOKEN_BALANCE_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
            .replace("{tokenIndex}", tokenIndex)
        httpClient.get<GenericResponse<TokenIndex>> {
            addTimeStampHeader(this)
            url("$baseUrl$path")
        }
    }

    override suspend fun transferNonFungibleTokenOfWallet(
        walletAddress: String,
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: TransferNonFungibleRequest
    ): GenericResponse<TransactionResponse> {
        val path = WALLET_NON_FUNGIBLE_TOKEN_TRANSFER_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
            .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun batchTransferNonFungibleTokenOfWallet(
        walletAddress: String,
        contractId: String,
        request: BatchTransferNonFungibleRequest
    ): GenericResponse<TransactionResponse> {
        val path = WALLET_NON_FUNGIBLE_TOKEN_BATCH_TRANSFER_PATH
            .replace("{walletAddress}", walletAddress)
            .replace("{contractId}", contractId)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    // item-tokens
    override suspend fun itemToken(contractId: String): GenericResponse<ItemToken> {
        return coroutineScope {
            httpClient.get<GenericResponse<ItemToken>> {
                addTimeStampHeader(this)
                url("$baseUrl${ITEM_TOKEN_PATH.replace("{contractId}", contractId)}")
            }
        }
    }

    override suspend fun fungibleTokens(
        contractId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<FungibleToken>> {
        val urlString = "$baseUrl${FUNGIBLE_TOKENS_PATH.replace("{contractId}", contractId)}"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<FungibleToken>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun fungibleToken(contractId: String, tokenType: String): GenericResponse<FungibleToken> {
        val path = FUNGIBLE_TOKEN_PATH
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.get<GenericResponse<FungibleToken>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun fungibleTokenHolders(
        contractId: String,
        tokenType: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<FungibleTokenHolder>> {
        val path = FUNGIBLE_TOKEN_HOLDERS_PATH
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)

        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<FungibleTokenHolder>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun createFungible(
        contractId: String,
        request: FungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse> = coroutineScope {
        httpClient.post<GenericResponse<TransactionResponse>> {
            addTimeStampHeader(this)
            url("$baseUrl${FUNGIBLE_TOKEN_CREATE_PATH.replace("{contractId}", contractId)}")
            this.body = json.write(request)
        }
    }

    override suspend fun updateFungible(
        contractId: String,
        tokenType: String,
        request: FungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse> {
        val path =
            FUNGIBLE_TOKEN_UPDATE_PATH.replace("{contractId}", contractId).replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.put<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun mintFungible(
        contractId: String,
        tokenType: String,
        request: FungibleTokenMintRequest
    ): GenericResponse<TransactionResponse> {
        val path =
            FUNGIBLE_TOKEN_MINT_PATH.replace("{contractId}", contractId).replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun burnFungible(
        contractId: String,
        tokenType: String,
        request: FungibleTokenItemTokenBurnRequest
    ): GenericResponse<TransactionResponse> {
        val path =
            FUNGIBLE_TOKEN_BURN_PATH.replace("{contractId}", contractId).replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun nonFungibleTokenTypes(
        contractId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<ItemTokenType>> {
        val urlString = "$baseUrl${NON_FUNGIBLE_TOKENS_PATH.replace("{contractId}", contractId)}"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<ItemTokenType>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun createNonFungibleType(
        contractId: String,
        request: NonFungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse> {
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl${NON_FUNGIBLE_TOKENS_PATH.replace("{contractId}", contractId)}")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun nonFungibleTokenType(
        contractId: String,
        tokenType: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<NonFungibleTokenType> {
        val path = NON_FUNGIBLE_TOKEN_TYPE_PATH.replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<NonFungibleTokenType>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    it.parameters.append("limit", limit.toString())
                    it.parameters.append("page", page.toString())
                    it.parameters.append("orderBy", orderBy.toParameter())
                }
            }
        }
    }

    override suspend fun updateNonFungibleTokenType(
        contractId: String,
        tokenType: String,
        request: NonFungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse> {
        val path = NON_FUNGIBLE_TOKEN_TYPE_PATH
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.put<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun nonFungibleToken(
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<NonFungibleId> {
        val path =
            NON_FUNGIBLE_TOKEN_ID_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
                .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.get<GenericResponse<NonFungibleId>> {
                addTimeStampHeader(this)
                url {
                    url("$baseUrl$path")
                }
            }
        }
    }

    override suspend fun updateNonFungibleToken(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: NonFungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse> {
        val path = NON_FUNGIBLE_TOKEN_ID_PATH
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
            .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.put<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun mintNonFungible(
        contractId: String,
        tokenType: String,
        request: NonFungibleTokenMintRequest
    ): GenericResponse<TransactionResponse> {
        val path =
            NON_FUNGIBLE_TOKEN_MINT_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun nonFungibleTokenTypeHolders(
        contractId: String,
        tokenType: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<NonFungibleTokenTypeHolder>> {
        val path = NON_FUNGIBLE_TOKEN_TYPE_HOLDERS_PATH.replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<NonFungibleTokenTypeHolder>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun nonFungibleTokenHolder(
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<NonFungibleTokenHolder> {
        val path =
            NON_FUNGIBLE_TOKEN_HOLDER_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
                .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.get<GenericResponse<NonFungibleTokenHolder>> {
                addTimeStampHeader(this)
                url {
                    url("$baseUrl$path")
                }
            }
        }
    }

    override suspend fun multiMintNonFungible(
        contractId: String,
        request: NonFungibleTokenMultiMintRequest
    ): GenericResponse<TransactionResponse> {
        val path =
            NON_FUNGIBLE_TOKEN_MULTI_MINT_PATH
                .replace("{contractId}", contractId)

        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun burnNonFungible(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: NonFungibleTokenItemTokenBurnRequest
    ): GenericResponse<TransactionResponse> {
        val path =
            NON_FUNGIBLE_TOKEN_BURN_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
                .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun nonFungibleTokenChildren(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<NonFungibleId>> {
        val path =
            NON_FUNGIBLE_TOKEN_CHILDREN_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
                .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<NonFungibleId>>> {
                addTimeStampHeader(this)
                url {
                    url("$baseUrl$path")
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun nonFungibleTokenParent(
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<NonFungibleId> {
        val path =
            NON_FUNGIBLE_TOKEN_PARENT_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
                .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.get<GenericResponse<NonFungibleId>> {
                addTimeStampHeader(this)
                url {
                    url("$baseUrl$path")
                }
            }
        }
    }

    override suspend fun nonFungibleTokenRoot(
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<NonFungibleId> {
        val path =
            NON_FUNGIBLE_TOKEN_ROOT_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
                .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.get<GenericResponse<NonFungibleId>> {
                addTimeStampHeader(this)
                url {
                    url("$baseUrl$path")
                }
            }
        }
    }

    override suspend fun attachNonFungible(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: NonFungibleTokenItemTokenAttachRequest
    ): GenericResponse<TransactionResponse> {
        val path =
            NON_FUNGIBLE_TOKEN_PARENT_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
                .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun detachNonFungible(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: NonFungibleTokenItemTokenDetachRequest
    ): GenericResponse<TransactionResponse> {
        val path =
            NON_FUNGIBLE_TOKEN_PARENT_PATH
                .replace("{contractId}", contractId)
                .replace("{tokenType}", tokenType)
                .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.delete<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun userDetail(userId: String): GenericResponse<UserIdAddress> {
        val path = USER_DETAIL_PATH.replace("{userId}", userId)

        return coroutineScope {
            httpClient.get<GenericResponse<UserIdAddress>> {
                addTimeStampHeader(this)
                url {
                    url("$baseUrl$path")
                }
            }
        }
    }

    override suspend fun transactionOfUser(
        userId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<TxResultResponse>> {
        val path = USER_TRANSACTIONS_PATH.replace("{userId}", userId)

        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<TxResultResponse>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun baseCoinBalanceOfUser(userId: String): GenericResponse<BaseCoinBalance> {
        val path = USER_BASE_COIN_BALANCE_PATH.replace("{userId}", userId)
        return coroutineScope {
            httpClient.get<GenericResponse<BaseCoinBalance>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun serviceTokenBalancesOfUser(
        userId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<ServiceTokenBalance>> {
        val path = USER_SERVICE_TOKENS_BALANCE_PATH.replace("{userId}", userId)

        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<ServiceTokenBalance>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun serviceTokenBalanceOfUser(
        userId: String,
        contractId: String
    ): GenericResponse<ServiceTokenBalance> {
        val path = USER_SERVICE_TOKEN_BALANCE_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)

        return coroutineScope {
            httpClient.get<GenericResponse<ServiceTokenBalance>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun fungibleTokenBalancesOfUser(
        userId: String,
        contractId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<FungibleBalance>> {
        val path = USER_FUNGIBLE_TOKENS_BALANCE_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<FungibleBalance>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun fungibleTokenBalanceOfUser(
        userId: String,
        contractId: String,
        tokenType: String
    ): GenericResponse<FungibleBalance> {
        val path = USER_FUNGIBLE_TOKEN_BALANCE_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.get<GenericResponse<FungibleBalance>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun nonFungibleTokenBalancesOfUser(
        userId: String,
        contractId: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<NonFungibleBalance>> {
        val path = USER_NON_FUNGIBLE_TOKENS_BALANCE_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<NonFungibleBalance>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun nonFungibleTokenBalancesOfUser(
        userId: String,
        contractId: String,
        tokenType: String,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ): GenericResponse<Collection<TokenIndex>> {
        val path = USER_NON_FUNGIBLE_TOKEN_BALANCES_BY_TYPE_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)

        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.get<GenericResponse<Collection<TokenIndex>>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    buildPagingUrl(it, limit, page, orderBy)
                }
            }
        }
    }

    override suspend fun nonFungibleTokenBalanceOfUser(
        userId: String,
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<TokenIndex> {
        val path = USER_NON_FUNGIBLE_TOKEN_BALANCE_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
            .replace("{tokenIndex}", tokenIndex)

        return coroutineScope {
            httpClient.get<GenericResponse<TokenIndex>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun requestSessionToken(requestSessionToken: String): GenericResponse<RequestSessionStatus> {
        val path =
            REQUEST_SESSION_TOKEN_PATH
                .replace("{requestSessionToken}", requestSessionToken)

        return coroutineScope {
            httpClient.get<GenericResponse<RequestSessionStatus>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun commitRequestSession(requestSessionToken: String): GenericResponse<TransactionResponse> {
        val path = COMMIT_SESSION_TOKEN_PATH.replace("{requestSessionToken}", requestSessionToken)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun issueSessionTokenForBaseCoinTransfer(
        userId: String,
        requestType: RequestType
    ): GenericResponse<RequestSession> {
        val path = ISSUE_SESSION_TOKEN_FOR_BASE_COIN_PATH
            .replace("{userId}", userId)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.post<GenericResponse<RequestSession>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    it.parameters.append("requestType", requestType.toParameter())
                }
            }
        }
    }

    override suspend fun issueSessionTokenForServiceTokenTransfer(
        userId: String,
        contractId: String,
        requestType: RequestType,
        request: UserServiceTokenTransferRequest
    ): GenericResponse<RequestSession> {
        val path = ISSUE_SESSION_TOKEN_FOR_SERVICE_TOKEN_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.post<GenericResponse<RequestSession>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    it.parameters.append("requestType", requestType.toParameter())
                }
                this.body = json.write(request)
            }
        }
    }

    override suspend fun issueSessionTokenForItemTokenProxy(
        userId: String,
        contractId: String,
        requestType: RequestType,
        requestUser: UserItemTokenProxyRequest
    ): GenericResponse<RequestSession> {
        val path = ISSUE_SESSION_TOKEN_FOR_ITEM_TOKEN_PROXY
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
        val urlString = "$baseUrl$path"
        return coroutineScope {
            httpClient.post<GenericResponse<RequestSession>> {
                addTimeStampHeader(this)
                url {
                    takeFrom(urlString)
                    it.parameters.append("requestType", requestType.toParameter())
                }
                this.body = json.write(requestUser)
            }
        }
    }

    override suspend fun isProxyOfItemToken(userId: String, contractId: String): GenericResponse<ProxyStatus> {
        val path = USER_ITEM_TOKEN_IS_PROXY_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)

        return coroutineScope {
            httpClient.get<GenericResponse<ProxyStatus>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
            }
        }
    }

    override suspend fun transferFungibleTokenOfUser(
        userId: String,
        contractId: String,
        tokenType: String,
        request: TransferFungibleTokenOfUserRequest
    ): GenericResponse<TransactionResponse> {
        val path = USER_FUNGIBLE_TOKEN_TRANSFER_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun transferNonFungibleTokenOfUser(
        userId: String,
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: TransferNonFungibleOfUserRequest
    ): GenericResponse<TransactionResponse> {
        val path = USER_NON_FUNGIBLE_TOKEN_TRANSFER_PATH
            .replace("{userId}", userId)
            .replace("{contractId}", contractId)
            .replace("{tokenType}", tokenType)
            .replace("{tokenIndex}", tokenIndex)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    override suspend fun batchTransferNonFungibleTokenOfUser(
        userId: String,
        contractId: String,
        request: BatchTransferNonFungibleOfUserRequest
    ): GenericResponse<TransactionResponse> {
        val pathVariablesMap = mapOf("userId" to userId, "contractId" to contractId)
        val path = buildApiPath(USER_NON_FUNGIBLE_TOKEN_BATCH_TRANSFER_PATH, pathVariablesMap)
        return coroutineScope {
            httpClient.post<GenericResponse<TransactionResponse>> {
                addTimeStampHeader(this)
                url("$baseUrl$path")
                this.body = json.write(request)
            }
        }
    }

    // TODO config strict mode or generous mode
    // stric mode -> throw exception when missing pair for path variable (* currently applied
    // generous mode -> just log missed path variable in warn log level
    private fun buildApiPath(apiPath: String, pathVariablesMap: Map<String, String>): String {
        require(pathVariablesMap.keys.filterNot { apiPath.contains("{$it}") }.isEmpty()) {
            "Missed path-variable - path: $apiPath, path-variables: $pathVariablesMap"
        }
        return pathVariablesMap.entries.fold(apiPath) { acc, (key, value) -> acc.replace("{$key}", value) }
    }

    private suspend fun addTimeStampHeader(
        httpRequestBuilder: HttpRequestBuilder
    ) {
        httpRequestBuilder.header("Timestamp", time().responseTime)
    }

    private fun buildPagingUrl(
        it: URLBuilder,
        limit: Int,
        page: Int,
        orderBy: OrderBy
    ) {
        it.parameters.append("limit", limit.toString())
        it.parameters.append("page", page.toString())
        it.parameters.append("orderBy", orderBy.toParameter())
    }
}
