package com.github.ryukato.link.developers.sdk.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.ryukato.link.developers.sdk.security.NonceGenerator
import com.github.ryukato.link.developers.sdk.security.SignatureGenerator
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.time.Clock

class DefaultRequestHeadersAppendInterceptor(
    private val applicationClock: Clock,
    private val signatureGenerator: SignatureGenerator,
    private val nonceGenerator: NonceGenerator,
    private val serviceApiKey: String,
    private val serviceApiSecret: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        @Suppress("DuplicatedCode")
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
                extractBodyFromRequest(request)
            }
        }
        val signature = signatureGenerator.generate(
            serviceApiSecret,
            request.method,
            request.url.encodedPath,
            timestamp,
            nonce,
            queryParams,
            body,
        )

        @Suppress("DuplicatedCode")
        val headers = newHeaders(request, signature, nonceGenerator.newNonce(), timestamp)

        return chain.proceed(request.newBuilder().headers(headers).build())
    }

    private fun newHeaders(request: Request, signature: String, nonce: String, timestamp: Long): Headers {
        return request.headers.newBuilder()
            .add(SERVICE_API_KEY_HEADER, serviceApiKey)
            .add(NONCE_HEADER, nonce)
            .add(SIGNATURE_HEADER, signature)
            .add(TIMESTAMP_HEADER, "$timestamp")
            .build()
    }

    private fun extractBodyFromRequest(request: Request): Map<String, Any> = when {
        request.body?.contentType() == JSON_MEDIA_TYPE -> {
            jsonBodyToMap(request)
        }
        request.body?.contentType() == FORM_MEDIA_TYPE -> {
            formBodyToMap(request)
        }
        else -> {
            throw IllegalArgumentException("Not supported media type")
        }
    }

    private fun jsonBodyToMap(request: Request): Map<String, Any> {
        return request.body?.let {
            val typeObject = object : TypeReference<Map<String, Any>>() {}
            val buffer = Buffer()
            it.writeTo(buffer)
            jacksonObjectMapper.readValue(buffer.inputStream(), typeObject)
        } ?: emptyMap()
    }

    private fun formBodyToMap(request: Request): Map<String, Any> {
        return (request.body)?.let {
            (request.body as FormBody).toMap()
        } ?: emptyMap()
    }

    companion object {
        private val jacksonObjectMapper = jacksonObjectMapper()
        private val JSON_MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
        private val FORM_MEDIA_TYPE = "application/x-www-form-urlencoded".toMediaType()
    }
}

fun FormBody.toMap(): Map<String, String> {
    return this.let { formBody ->
        (0 until formBody.size).map {
            formBody.name(it) to formBody.value(it)
        }.toMap()
    }
}
