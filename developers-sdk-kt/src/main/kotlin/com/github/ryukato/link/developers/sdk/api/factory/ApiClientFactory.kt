package com.github.ryukato.link.developers.sdk.api.factory

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.ryukato.link.developers.sdk.api.ApiClient
import com.github.ryukato.link.developers.sdk.api.ApiClientImpl
import com.github.ryukato.link.developers.sdk.api.NONCE_HEADER
import com.github.ryukato.link.developers.sdk.api.SERVICE_API_KEY_HEADER
import com.github.ryukato.link.developers.sdk.api.SIGNATURE_HEADER
import com.github.ryukato.link.developers.sdk.api.TIMESTAMP_HEADER
import com.github.ryukato.link.developers.sdk.http.HttpClientBuilder
import com.github.ryukato.link.developers.sdk.security.NonceGenerator
import com.github.ryukato.link.developers.sdk.security.SignatureGenerator
import io.ktor.util.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.jaxb.JaxbConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.Clock

class ApiClientFactory {
    @KtorExperimentalAPI
    fun build(
        config: ApiClientFactoryConfig,
        httpClientBuilder: HttpClientBuilder
    ): ApiClient {
        // TODO decouple concrete HttpClientBuilder
        httpClientBuilder.engineFactory = config.engineFactory
        httpClientBuilder.enableLogging = config.enableLogging
        httpClientBuilder.enableResponseValidation = config.enableResponseValidation
        httpClientBuilder.requestTimeout = config.requestTimeout
        httpClientBuilder.logger = config.logger
        httpClientBuilder.logLevel = config.logLevel
        httpClientBuilder.serializer = config.serializer

        val httpClient = httpClientBuilder.build(config.apiKeySecret)
        return ApiClientImpl(baseUrl(config.apiBaseUrl), httpClient)
    }

    private fun baseUrl(apiBaseUrl: String): String {
        return if (apiBaseUrl.endsWith("/")) {
            apiBaseUrl.substring(0, apiBaseUrl.length - 1)
        } else apiBaseUrl
    }

    fun build(
        retrofit: Retrofit
    ): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    fun retrofit(
        baseUrl: String,
        okHttp3Client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .addConverterFactory(JaxbConverterFactory.create())
            .client(okHttp3Client)
            .build()
    }

    fun httpClient(
        applicationClock: Clock,
        signatureGenerator: SignatureGenerator,
        nonceGenerator: NonceGenerator,
        enableLogging: Boolean,
        serviceApiKey: String,
        serviceApiSecret: String,
    ): OkHttpClient {
        val logLevel = if (!enableLogging) HttpLoggingInterceptor.Level.NONE else HttpLoggingInterceptor.Level.BODY
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(logLevel)
        val apiKeyInterceptor = Interceptor { chain ->
            val timestamp = applicationClock.instant().toEpochMilli()
            val nonce = nonceGenerator.newNonce()
            val request = chain.request()
            val queryParams: Map<String, List<String?>> = request.url.queryParameterNames.map {
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

            chain.proceed(request.newBuilder().headers(headers).build())
        }
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor).build()
    }
}
