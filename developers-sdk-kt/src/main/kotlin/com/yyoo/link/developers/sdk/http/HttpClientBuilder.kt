package com.yyoo.link.developers.sdk.http

import com.yyoo.link.developers.sdk.model.dto.ApiKeySecret
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*

interface HttpClientBuilder {
    var serializer: JsonSerializer?
    var logLevel: LogLevel
    var logger: Logger
    var requestTimeout: Long
    var enableResponseValidation: Boolean
    var enableLogging: Boolean
    var engineFactory: HttpClientEngineFactory<HttpClientEngineConfig>

    fun build(apiKeySecret: ApiKeySecret): HttpClient
}
