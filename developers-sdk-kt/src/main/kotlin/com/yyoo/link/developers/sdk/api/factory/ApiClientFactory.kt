package com.yyoo.link.developers.sdk.api.factory

import com.yyoo.link.developers.sdk.api.ApiClient
import com.yyoo.link.developers.sdk.api.ApiClientImpl
import com.yyoo.link.developers.sdk.http.HttpClientBuilder
import com.yyoo.link.developers.sdk.http.HttpClientBuilderImpl
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.util.*

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
        return if(apiBaseUrl.endsWith("/")) {
            apiBaseUrl.substring(0, apiBaseUrl.length -1)
        } else apiBaseUrl
    }
}
