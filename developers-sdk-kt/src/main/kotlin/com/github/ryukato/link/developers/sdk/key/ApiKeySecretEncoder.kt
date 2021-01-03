package com.github.ryukato.link.developers.sdk.key

interface ApiKeySecretEncoder {
    fun encodeApiKey(apiKey: String): String
    fun encodeSecret(apiSecret: String): String
}
