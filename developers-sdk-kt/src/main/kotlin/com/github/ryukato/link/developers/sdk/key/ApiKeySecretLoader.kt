@file:Suppress("unused")

package com.github.ryukato.link.developers.sdk.key

import org.bouncycastle.util.encoders.Base64

interface ApiKeySecretLoader {
    fun serviceApiKey(): String
    fun serviceApiSecret(): String
}


class Base64ApiKeySecretSecret : ApiKeySecretEncoder, ApiKeySecretDecoder {
    override fun encodeApiKey(apiKey: String): String {
        return Base64.toBase64String(apiKey.toByteArray())
    }

    override fun encodeSecret(apiSecret: String): String {
        return Base64.toBase64String(apiSecret.toByteArray())
    }

    override fun decodeApiKey(encodedApiKey: String): String {
        return String(Base64.decode(encodedApiKey)).replace("\n","")
    }

    override fun decodeSecret(encodedApiSecret: String): String {
        return String(Base64.decode(encodedApiSecret)).replace("\n","")
    }
}
