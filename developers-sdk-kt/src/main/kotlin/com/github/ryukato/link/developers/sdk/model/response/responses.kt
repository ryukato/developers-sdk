package com.github.ryukato.link.developers.sdk.model.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.github.ryukato.link.developers.sdk.exception.InvalidResponseValueException
import java.math.BigInteger
import java.util.Date


data class GenericResponse<T>(
    val responseTime: Long,
    val statusCode: Int,
    val statusMessage: String,
    val responseData: T? = null
)

data class ServiceDetail(
    val serviceId: String,
    val name: String,
    val description: String?,
    val category: String
)

class UserRequestStatus(val status: RequestSessionTokenStatus)
enum class RequestSessionTokenStatus {
    Authorized,
    Unauthorized;
}

data class ServiceToken(
    val contractId: String,
    val ownerAddress: String,
    val createdAt: Long,
    val serviceId: String,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val meta: String,
    val imgUri: String,
    val totalSupply: String,
    val totalMint: String,
    val totalBurn: String
)

data class ServiceTokenHolder(
    val address: String,
    val userId: String?,
    val amount: String
)

data class TransactionResponse(val txHash: String)

data class TxResultResponse(
    val height: Long,
    @JsonProperty("txhash") val txHash: String,
    val codespace: String?,
    val code: Int,
    val index: Int,
    val data: String?,
    val logs: List<LogResponse>?,
    val info: String?,
    val gasWanted: Long?,
    val gasUsed: Long,
    val tx: TypedValueResponse<StdTxResponse>,
    val timestamp: Date
)

data class LogResponse(
    val msgIndex: Int,
    val log: String,
    val events: List<EventResponse>?
)

data class EventResponse(
    val type: String,
    val attributes: List<KeyValueResponse<String>>
)

data class KeyValueResponse<T>(val key: String, val value: T?)
data class TypedValueResponse<T : Any?>(val type: String, val value: T)

data class StdTxResponse(
    val msg: List<TypedValueResponse<*>>,
    val fee: FeeResponse,
    val memo: String,
    val signatures: List<SignatureResponse>
)

data class FeeResponse(val gas: BigInteger, val amount: List<CoinResponse>)
data class SignatureResponse(
    val pubKey: TypedValueResponse<ByteArray>?,
    val signature: ByteArray
)

data class BaseCoinBalance(
    val symbol: String,
    val decimals: Long,
    val amount: String
)

data class CoinResponse(val denom: String, val amount: BigInteger) {
    override fun toString(): String {
        return amount.toString() + denom
    }
}

data class Memo(val memo: String)

data class WalletResponse(
    val name: String,
    val walletAddress: String,
    val createdAt: Long
)

data class ServiceTokenBalance(
    val contractId: String,
    val name: String,
    val symbol: String,
    val imgUri: String,
    val decimals: Long,
    val amount: String
)

data class FungibleBalance(
    val tokenType: String,
    val name: String,
    val meta: String,
    val amount: String
)

data class NonFungibleBalance(
    val tokenType: String,
    val name: String,
    val meta: String,
    val numberOfIndex: String
)

data class TokenIndex(val tokenIndex: String, val name: String, val meta: String)

data class ItemToken(
    val contractId: String,
    val baseImgUri: String,
    val ownerAddress: String,
    val serviceId: String,
    val createdAt: Long
)

data class FungibleToken(
    val name: String,
    val meta: String,
    val tokenType: String,
    val totalSupply: String,
    val totalMint: String,
    val totalBurn: String,
    val createdAt: Long
)

data class FungibleTokenHolder(
    val userId: String?,
    val walletAddress: String,
    val amount: String
)

data class ItemTokenType(
    val name: String,
    val meta: String,
    val tokenType: String,
    val totalSupply: String,
    val totalMint: String,
    val totalBurn: String,
    val createdAt: Long
)

data class NonFungibleTokenType(
    val name: String,
    val meta: String,
    val tokenType: String,
    val totalSupply: BigInteger,
    val totalMint: BigInteger,
    val totalBurn: BigInteger,
    val createdAt: Long,
    @JsonProperty("token")
    val tokens: List<NonFungibleIndex>
)

data class NonFungibleIndex(
    val tokenIndex: String,
    val name: String,
    val meta: String,
    val createdAt: Long,
    val burnedAt: Long?
)

data class NonFungibleId(
    val tokenId: String,
    val name: String,
    val meta: String,
    val createdAt: Long,
    val burnedAt: Long?
)

data class NonFungibleTokenTypeHolder(
    val userId: String?,
    val walletAddress: String,
    val numberOfIndex: String
)

data class NonFungibleTokenHolder(
    val tokenId: String,
    val userId: String?,
    val walletAddress: String,
    val amount: String
)

data class UserIdAddress(val userId: String, val address: String)
data class RequestSession(val requestSessionToken: String, val redirectUri: String)

@JsonDeserialize(using = RequestSessionStatusDeserializer::class)
enum class RequestSessionStatus {
    AUTHORIZED, UNAUTHORIZED
}

class RequestSessionStatusDeserializer(): StdDeserializer<RequestSessionStatus>(RequestSessionStatus::class.java) {
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): RequestSessionStatus {
        val node: JsonNode = jsonParser.codec.readTree(jsonParser)

        val status = node["status"].asText()

        return RequestSessionStatus.values().firstOrNull() {
            it.name == status.toUpperCase()
        } ?: throw InvalidResponseValueException("invalid request-session-status")
    }
}

data class ProxyStatus(val isApproved: Boolean)

data class ErrorResponse(
    val responseTime: Long,
    val statusCode: Int,
    val statusMessage: String
)

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}
