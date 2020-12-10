package com.yyoo.link.developers.sdk.api

import com.yyoo.link.developers.sdk.model.request.*
import com.yyoo.link.developers.sdk.model.response.*
import java.time.LocalDateTime
import java.time.ZoneOffset

interface ApiClient {
    suspend fun time(): GenericResponse<Unit>
    suspend fun userRequests(requestSessionToken: String): GenericResponse<UserRequestStatus>

    /**
     * Retrieve service information
     */
    suspend fun serviceDetail(serviceId: String): GenericResponse<ServiceDetail>

    // service-token
    /**
     * List all service tokens
     */
    suspend fun serviceTokens(): GenericResponse<Collection<ServiceToken>>

    /**
     * Retrieve service token information
     */
    suspend fun serviceToken(contractId: String): GenericResponse<ServiceToken>

    /**
     * List all service token holders
     */
    suspend fun serviceTokenHolders(
        contractId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<ServiceTokenHolder>>

    /**
     * Update service token information
     */
    suspend fun updateServiceToken(
        contractId: String,
        request: UpdateServiceTokenRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Mint a service token
     */
    suspend fun mintServiceToken(
        contractId: String,
        request: MintServiceTokenRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Burn a service token
     */
    suspend fun burnServiceToken(
        contractId: String,
        request: BurnServiceTokenRequest
    ): GenericResponse<TransactionResponse>

    // transaction
    suspend fun transaction(txHash: String): GenericResponse<TxResultResponse>

    // memo
    /**
     * Save the text
     */
    suspend fun saveMemo(request: MemoRequest): GenericResponse<TransactionResponse>
    suspend fun queryMemo(txHash: String): GenericResponse<Memo>

    // wallet
    /**
     * List all service wallets
     */
    suspend fun wallets(): GenericResponse<Collection<WalletResponse>>

    /**
     * Retrieve service wallet information
     */
    suspend fun wallet(walletAddress: String): GenericResponse<WalletResponse>

    /**
     * Retrieve service wallet transaction history
     * By default 1 day transactions of given wallet will be returned
     */
    suspend fun transactionOfWallet(
        walletAddress: String,
        after: Long? = LocalDateTime.now().minusDays(1).toEpochMilli(),
        before: Long? = LocalDateTime.now().toEpochMilli(),
        limit: Int = 10,
        msgType: String? = null,
        orderBy: OrderBy = OrderBy.ASC,
        page: Int = 1
    ): GenericResponse<Collection<TxResultResponse>>
    // wallet-balance

    /**
     * Retrieve base coin balance (service wallet)
     * Only for Cashew
     */
    suspend fun baseCoinBalanceOfWallet(walletAddress: String): GenericResponse<BaseCoinBalance>

    /**
     * Transfer base coins (service wallet)
     */
    suspend fun transferBaseCoin(
        walletAddress: String,
        request: TransferBaseCoinRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Retrieve balance of all service tokens (service wallet)
     */
    suspend fun serviceTokenBalancesOfWallet(
        walletAddress: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<ServiceTokenBalance>>

    /**
     * Retrieve balance of a specific service token (service wallet)
     */
    suspend fun serviceTokenBalanceOfWallet(
        walletAddress: String,
        contractId: String
    ): GenericResponse<ServiceTokenBalance>

    /**
     * Transfer service tokens (service wallet)
     */
    suspend fun transferServiceToken(
        walletAddress: String,
        contractId: String,
        request: TransferServiceTokenRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Retrieve balance of all fungibles (service wallet)
     */
    suspend fun fungibleTokensBalanceOfWallet(
        walletAddress: String,
        contractId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<FungibleBalance>>

    /**
     * Retrieve balance of a specific fungible (service wallet)
     */
    suspend fun fungibleTokenBalanceOfWallet(
        walletAddress: String,
        contractId: String,
        tokenType: String
    ): GenericResponse<FungibleBalance>

    /**
     * Transfer a fungible (service wallet)
     */
    suspend fun transferFungibleTokenOfWallet(
        walletAddress: String,
        contractId: String,
        tokenType: String,
        request: TransferFungibleTokenRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Retrieve balance of all non-fungibles (service wallet)
     */
    suspend fun nonFungibleTokenBalancesOfWallet(
        walletAddress: String,
        contractId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<NonFungibleBalance>>

    /**
     * Retrieve balance of specific type of non-fungibles (service wallet)
     */
    suspend fun nonFungibleTokenBalancesOfWalletByType(
        walletAddress: String,
        contractId: String,
        tokenType: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<TokenIndex>>

    /**
     * Retrieve balance of a specific non-fungible (service wallet)
     */
    suspend fun nonFungibleTokenBalanceOfWallet(
        walletAddress: String,
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<TokenIndex>

    /**
     * Transfer a non-fungible (service wallet)
     */
    suspend fun transferNonFungibleTokenOfWallet(
        walletAddress: String,
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: TransferNonFungibleRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Batch transfer non-fungibles (service wallet)
     */
    suspend fun batchTransferNonFungibleTokenOfWallet(
        walletAddress: String,
        contractId: String,
        request: BatchTransferNonFungibleRequest
    ): GenericResponse<TransactionResponse>

    // item-tokens
    /**
     * Retrieve item token contract information
     */
    suspend fun itemToken(contractId: String): GenericResponse<ItemToken>

    // fungibles
    /**
     * List all fungibles
     */
    suspend fun fungibleTokens(
        contractId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<FungibleToken>>

    /**
     * Retrieve fungible information
     */
    suspend fun fungibleToken(contractId: String, tokenType: String): GenericResponse<FungibleToken>

    /**
     * Retrieve all fungible holders
     */
    suspend fun fungibleTokenHolders(
        contractId: String,
        tokenType: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<FungibleTokenHolder>>

    /**
     * Create a fungible
     */
    suspend fun createFungible(
        contractId: String,
        request: FungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Update fungible information
     */
    suspend fun updateFungible(
        contractId: String,
        tokenType: String,
        request: FungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Mint a fungible
     */
    suspend fun mintFungible(
        contractId: String,
        tokenType: String,
        request: FungibleTokenMintRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Burn a fungible
     */
    suspend fun burnFungible(
        contractId: String,
        tokenType: String,
        request: FungibleTokenItemTokenBurnRequest
    ): GenericResponse<TransactionResponse>

    // non-fungibles
    /**
     * List all non-fungibles
     */
    suspend fun nonFungibleTokenTypes(
        contractId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<ItemTokenType>>

    /**
     * Create a non-fungible
     */
    suspend fun createNonFungibleType(
        contractId: String,
        request: NonFungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Retrieve a non-fungible token type
     */
    suspend fun nonFungibleTokenType(
        contractId: String,
        tokenType: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<NonFungibleTokenType>

    /**
     * Update a non-fungible token type
     */
    suspend fun updateNonFungibleTokenType(
        contractId: String,
        tokenType: String,
        request: NonFungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Retrieve non-fungible information
     */
    suspend fun nonFungibleToken(
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<NonFungibleId>

    /**
     * Update non-fungible information
     */
    suspend fun updateNonFungibleToken(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: NonFungibleTokenCreateUpdateRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Mint a non-fungible
     */
    suspend fun mintNonFungible(
        contractId: String,
        tokenType: String,
        request: NonFungibleTokenMintRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Retrieve holders of a specific non-fungible token type
     */
    suspend fun nonFungibleTokenTypeHolders(
        contractId: String,
        tokenType: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<NonFungibleTokenTypeHolder>>

    /**
     * Retrieve the holder of a specific non-fungible
     */
    suspend fun nonFungibleTokenHolder(
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<NonFungibleTokenHolder>

    /**
     * Mint multiple non-fungibles
     */
    suspend fun multiMintNonFungible(
        contractId: String,
        request: NonFungibleTokenMultiMintRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Burn a non-fungible
     */
    suspend fun burnNonFungible(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: NonFungibleTokenItemTokenBurnRequest
    ): GenericResponse<TransactionResponse>

    /**
     * List the children of a non-fungible
     */
    suspend fun nonFungibleTokenChildren(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<NonFungibleId>>

    /**
     * Retrieve the parent of a non-fungible
     */
    suspend fun nonFungibleTokenParent(
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<NonFungibleId>

    /**
     * Retrieve the root of a non-fungible
     */
    suspend fun nonFungibleTokenRoot(
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<NonFungibleId>

    /**
     * Attach a non-fungible to another
     */
    suspend fun attachNonFungible(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: NonFungibleTokenItemTokenAttachRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Detach a non-fungible from the parent
     */
    suspend fun detachNonFungible(
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: NonFungibleTokenItemTokenDetachRequest
    ): GenericResponse<TransactionResponse>

    // user api
    /**
     * Retrieve user information
     */
    suspend fun userDetail(userId: String): GenericResponse<UserIdAddress>

    /**
     * Retrieve user wallet transaction history
     */
    suspend fun transactionOfUser(
        userId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<TxResultResponse>>

    /**
     * Retrieve base coin balance (user wallet)
     */
    suspend fun baseCoinBalanceOfUser(userId: String): GenericResponse<BaseCoinBalance>

    /**
     * Retrieve balance of all service tokens (user wallet)
     */
    suspend fun serviceTokenBalancesOfUser(
        userId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<ServiceTokenBalance>>

    /**
     * Retrieve balance of a specific service token (user wallet)
     */
    suspend fun serviceTokenBalanceOfUser(
        userId: String,
        contractId: String
    ): GenericResponse<ServiceTokenBalance>

    /**
     * Retrieve balance of all fungibles (user wallet)
     */
    suspend fun fungibleTokenBalancesOfUser(
        userId: String,
        contractId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<FungibleBalance>>

    /**
     * Retrieve balance of a specific fungible (user wallet)
     */
    suspend fun fungibleTokenBalanceOfUser(
        userId: String,
        contractId: String,
        tokenType: String
    ): GenericResponse<FungibleBalance>

    /**
     * Retrieve balance of all non-fungibles (user wallet)
     */
    suspend fun nonFungibleTokenBalancesOfUser(
        userId: String,
        contractId: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<NonFungibleBalance>>

    /**
     * Retrieve balance of specific type of non-fungibles (user wallet)
     */
    suspend fun nonFungibleTokenBalancesOfUser(
        userId: String,
        contractId: String,
        tokenType: String,
        limit: Int = 10,
        page: Int = 1,
        orderBy: OrderBy = OrderBy.ASC
    ): GenericResponse<Collection<TokenIndex>>

    /**
     * Retrieve balance of a specific non-fungible (service wallet)
     */
    suspend fun nonFungibleTokenBalanceOfUser(
        userId: String,
        contractId: String,
        tokenType: String,
        tokenIndex: String
    ): GenericResponse<TokenIndex>

    /**
     * Retrieve the status of a session token
     */
    suspend fun requestSessionToken(requestSessionToken: String): GenericResponse<RequestSessionStatus>

    /**
     * Commit a transaction signed by a user wallet
     */
    suspend fun commitRequestSession(requestSessionToken: String): GenericResponse<TransactionResponse>

    /**
     * Issue a session token for base coin transfer
     */
    suspend fun issueSessionTokenForBaseCoinTransfer(
        userId: String,
        requestType: RequestType
    ): GenericResponse<RequestSession>

    /**
     * Issue a session token for service token transfer
     */
    suspend fun issueSessionTokenForServiceTokenTransfer(
        userId: String,
        contractId: String,
        requestType: RequestType,
        request: UserServiceTokenTransferRequest
    ): GenericResponse<RequestSession>

    /**
     * Issue a session token for proxy setting
     */
    suspend fun issueSessionTokenForItemTokenProxy(
        userId: String,
        contractId: String,
        requestType: RequestType,
        requestUser: UserItemTokenProxyRequest
    ): GenericResponse<RequestSession>

    /**
     * Retrieve whether the proxy set or not
     */
    suspend fun isProxyOfItemToken(userId: String, contractId: String): GenericResponse<ProxyStatus>

    /**
     * Transfer a fungible (user wallet)
     */
    suspend fun transferFungibleTokenOfUser(
        userId: String,
        contractId: String,
        tokenType: String,
        request: TransferFungibleTokenOfUserRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Transfer a non-fungible (user wallet)
     */
    suspend fun transferNonFungibleTokenOfUser(
        userId: String,
        contractId: String,
        tokenType: String,
        tokenIndex: String,
        request: TransferNonFungibleOfUserRequest
    ): GenericResponse<TransactionResponse>

    /**
     * Batch transfer non-fungibles (user wallet)
     */
    suspend fun batchTransferNonFungibleTokenOfUser(
        userId: String,
        contractId: String,
        request: BatchTransferNonFungibleOfUserRequest
    ): GenericResponse<TransactionResponse>

}


fun LocalDateTime.toEpochMilli(): Long = this.toInstant(ZoneOffset.UTC).toEpochMilli()
