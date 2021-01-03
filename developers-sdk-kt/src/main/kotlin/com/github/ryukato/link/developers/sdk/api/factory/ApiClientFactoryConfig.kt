package com.github.ryukato.link.developers.sdk.api.factory

import com.github.ryukato.link.developers.sdk.model.dto.ApiKeySecret
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.util.*

data class ApiClientFactoryConfig @KtorExperimentalAPI constructor(
    val apiBaseUrl: String,
    val apiKeySecret: ApiKeySecret,
    val engineFactory: HttpClientEngineFactory<HttpClientEngineConfig> = CIO,
    val enableResponseValidation: Boolean = false,
    val requestTimeout: Long = 10000L,
    val enableLogging: Boolean = true,
    val logger: Logger = Logger.DEFAULT,
    val logLevel: LogLevel = LogLevel.BODY,
    val serializer: JsonSerializer = JacksonSerializer()
) {
    init {
        require(apiBaseUrl.isNotBlank()) { "invalid api-base-url: blank" }
    }
}
