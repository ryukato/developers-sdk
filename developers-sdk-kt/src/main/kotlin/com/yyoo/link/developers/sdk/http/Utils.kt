package com.yyoo.link.developers.sdk.http

import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang3.StringUtils
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Suppress("unused")
fun signature(
    serviceApiSecret: String,
    httpMethod: String, // GET, POST, PUT, and DELETE
    pathWithQueryParam: String,
    timestamp: Long,
    nonce: String,
    body: Map<String, Any?> = emptyMap()
): String {
    val bodyTreeMap = TreeMap<String, Any?>()
    bodyTreeMap.putAll(body)

    @Suppress("UNCHECKED_CAST") val data = if (bodyTreeMap.isEmpty()) {
        "$nonce$timestamp$httpMethod$pathWithQueryParam"
    } else {
        val concatenatedBody = bodyTreeMap.map { (k, v) ->
            when (v) {
                is String -> "$k=$v"
                is List<*> -> {
                    val listTreeMap = TreeMap<String, String?>()
                    v as List<Map<String, String>>
                    v.forEachIndexed { index, map ->
                        map.keys.union(listTreeMap.keys).forEach { key ->
                            val value = map[key] ?: StringUtils.EMPTY
                            if (listTreeMap[key] == null) {
                                listTreeMap[key] = "${",".repeat(index)}$value"
                            } else {
                                listTreeMap[key] = "${listTreeMap[key]},$value"
                            }
                        }
                    }
                    listTreeMap.map { (lk, kv) ->
                        "$k.$lk=$kv"
                    }.joinToString("&")
                }
                else -> throw IllegalArgumentException()
            }
        }.joinToString("&")

        if ("?" in pathWithQueryParam) {
            "$nonce$timestamp$httpMethod$pathWithQueryParam&$concatenatedBody"
        } else {
            "$nonce$timestamp$httpMethod$pathWithQueryParam?$concatenatedBody"
        }
    }

    val hmac512 = "HmacSHA512"
    val signingKey = SecretKeySpec(serviceApiSecret.toByteArray(), hmac512)
    val mac = Mac.getInstance(hmac512)
    mac.init(signingKey)
    val rawHmac = mac.doFinal(data.toByteArray())
    return Base64.encodeBase64String(rawHmac)
}
