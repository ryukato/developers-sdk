package com.yyoo.link.developers.sdk.api.factory

import com.yyoo.link.developers.sdk.api.ApiClient
import com.yyoo.link.developers.sdk.api.ApiClientImpl
import com.yyoo.link.developers.sdk.http.HttpClientBuilder
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.util.*

class ApiClientFactory {
    @KtorExperimentalAPI
    fun build(
        apiBaseUrl: String,
        serviceApiKey: String,
        serviceApiSecret: String,
        engineFactory: HttpClientEngineFactory<HttpClientEngineConfig> = CIO,
        enableResponseValidation: Boolean = false,
        requestTimeout: Long = 10000L,
        enableLogging: Boolean = true,
        logger: Logger = Logger.DEFAULT,
        logLevel: LogLevel = LogLevel.BODY,
        serializer: JsonSerializer? = null
    ): ApiClient {
        // TODO decouple concrete HttpClientBuilder
        val httpClientBuilder = HttpClientBuilder()
        httpClientBuilder.engineFactory = engineFactory
        httpClientBuilder.enableLogging = enableLogging
        httpClientBuilder.enableResponseValidation = enableResponseValidation
        httpClientBuilder.requestTimeout = requestTimeout
        httpClientBuilder.logger = logger
        httpClientBuilder.logLevel = logLevel
        httpClientBuilder.serializer = serializer

        val httpClient = httpClientBuilder.build(serviceApiKey, serviceApiSecret)
        return ApiClientImpl(baseUrl(apiBaseUrl), httpClient)
    }

    private fun baseUrl(apiBaseUrl: String): String {
        return if(apiBaseUrl.endsWith("/")) {
            apiBaseUrl.substring(0, apiBaseUrl.length -1)
        } else apiBaseUrl
    }
}
