package com.github.ryukato.link.developers.sdk.api

const val SERVICE_API_KEY_HEADER = "service-api-key"
const val SIGNATURE_HEADER = "Signature"
const val TIMESTAMP_HEADER = "Timestamp"
const val NONCE_HEADER = "Nonce"

const val USER_REQUESTS_PATH = "/v1/user-requests/{requestSessionToken}"

// time
const val TIME_API_PATH = "/v1/time"

// service
const val SERVICE_DETAIL_API_PATH = "/v1/services/{serviceId}"

// service-token
const val SERVICE_TOKENS_PATH = "/v1/service-tokens"
const val SERVICE_TOKEN_PATH = "/v1/service-tokens/{contractId}"
const val SERVICE_TOKEN_BURN_PATH = "/v1/service-tokens/{contractId}/burn-from"
const val SERVICE_TOKEN_MINT_PATH = "/v1/service-tokens/{contractId}/mint"
const val SERVICE_TOKEN_HOLDERS_PATH = "$SERVICE_TOKEN_PATH/holders"

// transaction
const val TRANSACTION_PATH = "/v1/transactions/{txHash}"

// memo
const val MEMO_PATH = "/v1/memos"
const val MEMO_BY_TX_HASH_PATH = "/v1/memos/{txHash}"

// wallet
const val WALLET_LIST_PATH = "/v1/wallets"
const val WALLET_PATH = "$WALLET_LIST_PATH/{walletAddress}"
const val WALLET_SERVICE_TOKENS_BALANCE_PATH = "$WALLET_PATH/service-tokens"
const val WALLET_BASE_COIN_BALANCE_PATH = "$WALLET_PATH/base-coin"
const val WALLET_SERVICE_TOKEN_BALANCE_PATH = "$WALLET_SERVICE_TOKENS_BALANCE_PATH/{contractId}"
const val WALLET_FUNGIBLE_TOKENS_BALANCE_PATH = "$WALLET_PATH/item-tokens/{contractId}/fungibles"
const val WALLET_FUNGIBLE_TOKEN_BALANCE_PATH = "$WALLET_FUNGIBLE_TOKENS_BALANCE_PATH/{tokenType}"
const val WALLET_TRANSACTIONS_PATH = "$WALLET_PATH/transactions"

const val WALLET_NON_FUNGIBLE_TOKENS_BALANCE_PATH =
    "$WALLET_PATH/item-tokens/{contractId}/non-fungibles"

const val WALLET_NON_FUNGIBLE_TOKEN_BALANCES_BY_TYPE_PATH =
    "$WALLET_PATH/item-tokens/{contractId}/non-fungibles/{tokenType}"

const val WALLET_NON_FUNGIBLE_TOKEN_BALANCE_PATH =
    "$WALLET_NON_FUNGIBLE_TOKEN_BALANCES_BY_TYPE_PATH/{tokenIndex}"

// transfer
const val SERVICE_TOKEN_TRANSFER_PATH =
    "$WALLET_SERVICE_TOKEN_BALANCE_PATH/transfer"

const val BASE_COIN_TRANSFER_PATH = "$WALLET_BASE_COIN_BALANCE_PATH/transfer"

const val WALLET_NON_FUNGIBLE_TOKEN_TRANSFER_PATH = "$WALLET_NON_FUNGIBLE_TOKEN_BALANCE_PATH/transfer"
const val WALLET_NON_FUNGIBLE_TOKEN_BATCH_TRANSFER_PATH =
    "$WALLET_PATH/item-tokens/{contractId}/non-fungibles/batch-transfer"

const val WALLET_FUNGIBLE_TOKEN_TRANSFER_PATH = "$WALLET_FUNGIBLE_TOKEN_BALANCE_PATH/transfer"

// item-tokens
const val ITEM_TOKEN_PATH = "/v1/item-tokens/{contractId}"
const val FUNGIBLE_TOKENS_PATH = "/v1/item-tokens/{contractId}/fungibles"
const val FUNGIBLE_TOKEN_PATH = "/v1/item-tokens/{contractId}/fungibles/{tokenType}"
const val FUNGIBLE_TOKEN_HOLDERS_PATH = "/v1/item-tokens/{contractId}/fungibles/{tokenType}/holders"
const val FUNGIBLE_TOKEN_CREATE_PATH = "/v1/item-tokens/{contractId}/fungibles"
const val FUNGIBLE_TOKEN_UPDATE_PATH = "/v1/item-tokens/{contractId}/fungibles/{tokenType}"
const val FUNGIBLE_TOKEN_MINT_PATH = "/v1/item-tokens/{contractId}/fungibles/{tokenType}/mint"
const val FUNGIBLE_TOKEN_BURN_PATH = "/v1/item-tokens/{contractId}/fungibles/{tokenType}/burn"

const val NON_FUNGIBLE_TOKENS_PATH = "/v1/item-tokens/{contractId}/non-fungibles"
const val NON_FUNGIBLE_TOKEN_TYPE_PATH = "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}"
const val NON_FUNGIBLE_TOKEN_ID_PATH =
    "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}/{tokenIndex}"

const val NON_FUNGIBLE_TOKEN_MINT_PATH = "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}/mint"
const val NON_FUNGIBLE_TOKEN_MULTI_MINT_PATH = "/v1/item-tokens/{contractId}/non-fungibles/multi-mint"
const val NON_FUNGIBLE_TOKEN_BURN_PATH =
    "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}/{tokenIndex}/burn"
const val NON_FUNGIBLE_TOKEN_TYPE_HOLDERS_PATH =
    "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}/holders"
const val NON_FUNGIBLE_TOKEN_HOLDER_PATH =
    "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}/{tokenIndex}/holder"
const val NON_FUNGIBLE_TOKEN_CHILDREN_PATH =
    "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}/{tokenIndex}/children"
const val NON_FUNGIBLE_TOKEN_PARENT_PATH =
    "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}/{tokenIndex}/parent"

const val NON_FUNGIBLE_TOKEN_ROOT_PATH =
    "/v1/item-tokens/{contractId}/non-fungibles/{tokenType}/{tokenIndex}/root"

// user api path
const val USER_DETAIL_PATH = "/v1/users/{userId}"
const val USER_TRANSACTIONS_PATH = "/v1/users/{userId}/transactions"
const val USER_BASE_COIN_BALANCE_PATH = "/v1/users/{userId}/base-coin"
const val USER_SERVICE_TOKENS_BALANCE_PATH = "/v1/users/{userId}/service-tokens"
const val USER_SERVICE_TOKEN_BALANCE_PATH = "/v1/users/{userId}/service-tokens/{contractId}"

const val USER_FUNGIBLE_TOKENS_BALANCE_PATH = "/v1/users/{userId}/item-tokens/{contractId}/fungibles"
const val USER_FUNGIBLE_TOKEN_BALANCE_PATH = "/v1/users/{userId}/item-tokens/{contractId}/fungibles/{tokenType}"

const val USER_NON_FUNGIBLE_TOKENS_BALANCE_PATH = "/v1/users/{userId}/item-tokens/{contractId}/non-fungibles"
const val USER_NON_FUNGIBLE_TOKEN_BALANCES_BY_TYPE_PATH =
    "/v1/users/{userId}/item-tokens/{contractId}/non-fungibles/{tokenType}"
const val USER_NON_FUNGIBLE_TOKEN_BALANCE_PATH =
    "/v1/users/{userId}/item-tokens/{contractId}/non-fungibles/{tokenType}/{tokenIndex}"

const val REQUEST_SESSION_TOKEN_PATH = "/v1/user-requests/{requestSessionToken}"
const val COMMIT_SESSION_TOKEN_PATH = "/v1/user-requests/{requestSessionToken}/commit"
const val ISSUE_SESSION_TOKEN_FOR_BASE_COIN_PATH = "/v1/users/{userId}/base-coin/request-transfer"
const val ISSUE_SESSION_TOKEN_FOR_SERVICE_TOKEN_PATH =
    "/v1/users/{userId}/service-tokens/{contractId}/request-transfer"
const val ISSUE_SESSION_TOKEN_FOR_SERVICE_TOKEN_PROXY = "/v1/users/{userId}/service-tokens/{contractId}/request-proxy"
const val ISSUE_SESSION_TOKEN_FOR_ITEM_TOKEN_PROXY = "/v1/users/{userId}/item-tokens/{contractId}/request-proxy"
const val USER_SERVICE_TOKEN_IS_PROXY_PATH = "/v1/users/{userId}/service-tokens/{contractId}/proxy"
const val USER_ITEM_TOKEN_IS_PROXY_PATH = "/v1/users/{userId}/item-tokens/{contractId}/proxy"
const val USER_SERVICE_TOKEN_TRANSFER_PATH =
    "/v1/users/{userId}/service-tokens/{contractId}/transfer"
const val USER_FUNGIBLE_TOKEN_TRANSFER_PATH =
    "/v1/users/{userId}/item-tokens/{contractId}/fungibles/{tokenType}/transfer"
const val USER_NON_FUNGIBLE_TOKEN_TRANSFER_PATH =
    "/v1/users/{userId}/item-tokens/{contractId}/non-fungibles/{tokenType}/{tokenIndex}/transfer"
const val USER_NON_FUNGIBLE_TOKEN_BATCH_TRANSFER_PATH = "/v1/users/{userId}/item-tokens/{contractId}/non-fungibles/batch-transfer"
