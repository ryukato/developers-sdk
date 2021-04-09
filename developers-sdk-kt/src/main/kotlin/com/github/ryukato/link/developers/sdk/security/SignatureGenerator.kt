package com.github.ryukato.link.developers.sdk.security

import org.apache.commons.codec.binary.Base64
import java.util.TreeMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

interface SignatureGenerator {
    fun generate(
        serviceApiSecret: String,
        httpMethod: String,
        path: String,
        timestamp: Long,
        nonce: String,
        pathWithQueryParam: String,
        body: Map<String, Any?> = emptyMap(),
    ): String

    fun generate(
        serviceApiSecret: String,
        httpMethod: String,
        path: String,
        timestamp: Long,
        nonce: String,
        queryParam: Map<String, Any?> = emptyMap(),
        body: Map<String, Any?> = emptyMap(),
    ): String
}


class DefaultSignatureGenerator(
    private val requestBodyFlattener: RequestBodyFlattener,
) : SignatureGenerator {
    override fun generate(
        serviceApiSecret: String,
        httpMethod: String,
        path: String,
        timestamp: Long,
        nonce: String,
        queryParam: Map<String, Any?>,
        body: Map<String, Any?>,
    ): String {
        val flattenQueryParam = requestBodyFlattener.flatten(queryParam)
        return generate(
            serviceApiSecret,
            httpMethod,
            path,
            timestamp,
            nonce,
            flattenQueryParam,
            body,
        )
    }

    override fun generate(
        serviceApiSecret: String,
        httpMethod: String,
        path: String,
        timestamp: Long,
        nonce: String,
        pathWithQueryParam: String,
        body: Map<String, Any?>,
    ): String {
        val bodyTreeMap = TreeMap<String, Any?>()
        bodyTreeMap.putAll(body)
        val flattenBody = requestBodyFlattener.flatten(body)

        val data = if ("?" in pathWithQueryParam) {
            "$nonce$timestamp$httpMethod$pathWithQueryParam&$flattenBody"
        } else {
            "$nonce$timestamp$httpMethod$pathWithQueryParam?$flattenBody"
        }


        val hmac512 = "HmacSHA512"
        val signingKey = SecretKeySpec(serviceApiSecret.toByteArray(), hmac512)
        val mac = Mac.getInstance(hmac512)
        mac.init(signingKey)
        val rawHmac = mac.doFinal(data.toByteArray())
        return Base64.encodeBase64String(rawHmac)
    }
}
