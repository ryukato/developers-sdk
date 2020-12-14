@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.yyoo.link.developers.sdk.http

import com.yyoo.link.developers.sdk.model.dto.ApiKeySecret
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.util.*

internal class HttpClientBuilderImpl: HttpClientBuilder {
    @KtorExperimentalAPI
    override var engineFactory: HttpClientEngineFactory<HttpClientEngineConfig> = CIO
    override var enableResponseValidation: Boolean = false
    override var requestTimeout: Long = 10000L
    override var enableLogging: Boolean = true
    override var logger: Logger = Logger.DEFAULT
    override var logLevel: LogLevel = LogLevel.BODY
    override lateinit var serializer: JsonSerializer

    @KtorExperimentalAPI
    override fun build(
        apiKeySecret: ApiKeySecret
    ): HttpClient {
        return HttpClient(engineFactory) {
            this.expectSuccess = this@HttpClientBuilderImpl.enableResponseValidation
            this.engine {
                if (this is CIOEngineConfig) {
                    this.requestTimeout = this@HttpClientBuilderImpl.requestTimeout
                }
            }
            if (enableLogging) {
                this.install(Logging) {
                    this.logger = this@HttpClientBuilderImpl.logger
                    this.level = this@HttpClientBuilderImpl.logLevel
                }
            }
            this.install(ApiClientRequestFeature) {
                this.serviceApiKey = apiKeySecret.key
                this.serviceApiSecret = apiKeySecret.secret

            }
            this.install(JsonFeature) {
                this.serializer = this@HttpClientBuilderImpl.serializer
            }
        }
    }
}


