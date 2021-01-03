package com.github.ryukato.link.developers.sdk.key

interface ApiKeySecretDecoder {
    fun decodeApiKey(encodedApiKey: String): String
    fun decodeSecret(encodedApiSecret: String): String
}
