package com.github.ryukato.link.developers.sdk.model.request

import java.math.BigInteger

data class UpdateServiceTokenRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val name: String?,
    val meta: String?
)

data class BurnServiceTokenRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val amount: String
)

data class MintServiceTokenRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val amount: String
)

data class MemoRequest(
    val memo: String,
    val walletAddress: String,
    val walletSecret: String
)

data class TransferBaseCoinRequest(
    val walletSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val amount: String
) : AbstractTransactionRequest(toAddress, toUserId)

data class TransferServiceTokenRequest(
    val walletSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val amount: String
) : AbstractTransactionRequest(toAddress, toUserId)

data class TransferFungibleTokenRequest(
    val walletSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val amount: String
) : AbstractTransactionRequest(toAddress, toUserId)

data class TransferTokenOfUserRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val amount: String
) : AbstractTransactionRequest(toAddress, toUserId)

data class TransferNonFungibleRequest(
    val walletSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null
) : AbstractTransactionRequest(toAddress, toUserId)

data class TransferNonFungibleOfUserRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null
) : AbstractTransactionRequest(toAddress, toUserId)

data class BatchTransferNonFungibleRequest(
    val walletSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val transferList: Collection<TokenId>
) : AbstractTransactionRequest(toAddress, toUserId)

data class BatchTransferNonFungibleOfUserRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val transferList: Collection<TokenId>
) : AbstractTransactionRequest(toAddress, toUserId)

data class TokenId(val tokenId: String) {
    private val tokenIdFormat = "\\d{8}\\d{8}".toRegex()
    private val tokenIdLength = 16
    init {

        require(tokenId.length == tokenIdLength) { "Invalid tokenId: length of token-id has to be 16" }
        require(tokenIdFormat.matches(tokenId)) { "Invalid tokenId: invalid format of tokenId, valid format is $tokenIdFormat" }
    }
}

data class FungibleTokenCreateUpdateRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val name: String,
    val meta: String? = null
)

data class FungibleTokenMintRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val amount: String
) : AbstractTransactionRequest(toAddress, toUserId)

data class FungibleTokenItemTokenBurnRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val fromUserId: String? = null,
    val fromAddress: String? = null,
    val amount: String
) : AbstractItemTokenBurnTransactionRequest(fromUserId, fromAddress)

data class NonFungibleTokenCreateUpdateRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val name: String,
    val meta: String? = null
)

data class NonFungibleTokenMintRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val name: String,
    val meta: String
) : AbstractTransactionRequest(toAddress, toUserId)

data class NonFungibleTokenMultiMintRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val toAddress: String? = null,
    val toUserId: String? = null,
    val mintList: Collection<MultiMintNonFungible>
) : AbstractTransactionRequest(toAddress, toUserId)

data class MultiMintNonFungible(
    val tokenType: String,
    val name: String,
    val meta: String?
)

data class NonFungibleTokenItemTokenBurnRequest(
    val ownerAddress: String,
    val ownerSecret: String,
    val fromUserId: String? = null,
    val fromAddress: String? = null
) : AbstractItemTokenBurnTransactionRequest(fromUserId, fromAddress)

data class NonFungibleTokenItemTokenAttachRequest(
    val parentTokenId: String,
    val serviceWalletAddress: String,
    val serviceWalletSecret: String,
    val tokenHolderAddress: String? = null,
    val tokenHolderUserId: String? = null
) {
    init {
        require(tokenHolderAddress != null || tokenHolderUserId != null) {
            "tokenHolderAddress or tokenHolderUserId, one of them is required"
        }
    }
}

data class NonFungibleTokenItemTokenDetachRequest(
    val serviceWalletAddress: String,
    val serviceWalletSecret: String,
    val tokenHolderAddress: String? = null,
    val tokenHolderUserId: String? = null
) {
    init {
        require(tokenHolderAddress != null || tokenHolderUserId != null) {
            "tokenHolderAddress or tokenHolderUserId, one of them is required"
        }
    }
}

data class UserServiceTokenTransferRequest(
    val toAddress: String? = null,
    val toUserId: String? = null,
    val amount: String,
    val landingUri: String?
) : AbstractTransactionRequest(toAddress, toUserId) {
    init {
        try {
            if (amount.toBigInteger() <= BigInteger.ZERO) {
                throw IllegalArgumentException("Invalid amount - $amount is less than zero ")
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid amount - $amount is not a number ")
        }
    }
}

data class UserAssetProxyRequest(
    val ownerAddress: String,
    val landingUri: String?
)

abstract class AbstractItemTokenBurnTransactionRequest(
    fromUserId: String?,
    fromAddress: String?
) {
    init {
        require(fromAddress != null || fromUserId != null) { "fromAddress or fromUserId, one of them is required" }
    }
}

abstract class AbstractTransactionRequest(
    toAddress: String?,
    toUserId: String?
) {
    init {
        require(toAddress != null || toUserId != null) { "toAddress or toUserId, one of them is required" }
    }
}


enum class OrderBy {
    ASC, DESC;
    fun toParameter(): String = this.name.toLowerCase()
}

enum class RequestType {
    REDIRECT_URI, AOA;
    fun toParameter(): String = this.name.toLowerCase()
}
