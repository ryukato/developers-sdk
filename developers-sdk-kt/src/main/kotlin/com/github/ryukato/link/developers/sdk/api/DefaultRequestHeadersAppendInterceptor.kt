package com.github.ryukato.link.developers.sdk.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.ryukato.link.developers.sdk.security.NonceGenerator
import com.github.ryukato.link.developers.sdk.security.SignatureGenerator
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.time.Clock

class DefaultRequestHeadersAppendInterceptor(
    private val applicationClock: Clock,
    private val signatureGenerator: SignatureGenerator,
    private val nonceGenerator: NonceGenerator,
    private val serviceApiKey: String,
    private val serviceApiSecret: String,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val timestamp = applicationClock.instant().toEpochMilli()
        val nonce = nonceGenerator.newNonce()
        val request = chain.request()
        val queryParams: Map<String, Any> = request.url.queryParameterNames.map {
            it to request.url.queryParameterValues(it)
        }.toMap()
        val body: Map<String, Any> = if (request.method == "GET") {
            emptyMap()
        } else {
            if (request.body?.contentLength() ?: 0 < 0) {
                emptyMap()
            } else {
                val typeObject = object : TypeReference<Map<String, Any>>() {
                }
                val buffer = Buffer()
                request.body?.writeTo(buffer)
                jacksonObjectMapper().readValue(buffer.inputStream(), typeObject)
            }
        }
        val headers = request.headers.newBuilder()
            .add(SERVICE_API_KEY_HEADER, serviceApiKey)
            .add(NONCE_HEADER, nonceGenerator.newNonce())
            .add(SIGNATURE_HEADER, signatureGenerator.generate(
                serviceApiSecret,
                request.method,
                request.url.encodedPath,
                timestamp,
                nonce,
                queryParams,
                body,
            ))
            .add(TIMESTAMP_HEADER, "$timestamp")
            .build()

        return chain.proceed(request.newBuilder().headers(headers).build())
    }
}
