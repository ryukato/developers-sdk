package com.github.ryukato.link.developers.sdk.api

import mu.KotlinLogging
import org.apache.commons.codec.binary.Base64
import java.util.TreeMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val logger = KotlinLogging.logger { }

interface SignatureGenerator {
    fun generate(
        serviceApiSecret: String,
        httpMethod: String,
        path: String,
        timestamp: Long,
        nonce: String,
        flatQueryParam: String,
        body: Map<String, Any?> = emptyMap(),
    ): String

    fun generate(
        serviceApiSecret: String,
        httpMethod: String,
        path: String,
        timestamp: Long,
        nonce: String,
        queryParam: Map<String, List<String?>> = emptyMap(),
        body: Map<String, Any?> = emptyMap(),
    ): String
}


class DefaultSignatureGenerator(
    private val queryParameterFlattener: QueryParameterFlattener,
    private val requestBodyFlattener: RequestBodyFlattener,
) : SignatureGenerator {
    override fun generate(
        serviceApiSecret: String,
        httpMethod: String,
        path: String,
        timestamp: Long,
        nonce: String,
        queryParam: Map<String, List<String?>>,
        body: Map<String, Any?>,
    ): String {
        val flattenQueryParam = queryParameterFlattener.flatten(queryParam)

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
        flatQueryParam: String,
        body: Map<String, Any?>,
    ): String {
        val data = signatureTarget(body, nonce, timestamp, httpMethod, path, flatQueryParam)
        logger.debug { "signature data:$data, from httpMethod$httpMethod, path: $path, timestamp: $timestamp, nonce:$nonce, flatQueryParam: $flatQueryParam, body: $body" }
        val rawHmac = rawSignature(serviceApiSecret, data)
        return Base64.encodeBase64String(rawHmac)
    }

    private fun rawSignature(serviceApiSecret: String, data: String): ByteArray? {
        val signingKey = SecretKeySpec(serviceApiSecret.toByteArray(), HNAC_512_SECRET_ALGORITHM)
        val mac = Mac.getInstance(HNAC_512_SECRET_ALGORITHM)
        mac.init(signingKey)
        return mac.doFinal(data.toByteArray())
    }

    private fun signatureTarget(body: Map<String, Any?>, nonce: String, timestamp: Long, httpMethod: String, path: String, flatQueryParam: String): String {
        val bodyTreeMap = sortBody(body)
        val flattenBody = requestBodyFlattener.flatten(bodyTreeMap)
        val stringBuilder = StringBuilder()
        stringBuilder.append("$nonce$timestamp$httpMethod$path")
        if (flatQueryParam.isNotBlank()) {
            if ("?" in flatQueryParam) {
                stringBuilder.append(flatQueryParam)
            } else {
                stringBuilder.append("?").append(flatQueryParam)
            }
        }
        if (flattenBody.isNotBlank()) {
            if (!stringBuilder.contains('?')) {
                stringBuilder.append("?")
            }
            stringBuilder.append("&").append(flattenBody)
        }

        return stringBuilder.toString()
    }

    private fun sortBody(body: Map<String, Any?>): TreeMap<String, Any?> {
        val bodyTreeMap = TreeMap<String, Any?>()
        bodyTreeMap.putAll(body)
        return bodyTreeMap
    }

    companion object {
        private const val HNAC_512_SECRET_ALGORITHM = "HmacSHA512"
    }
}
