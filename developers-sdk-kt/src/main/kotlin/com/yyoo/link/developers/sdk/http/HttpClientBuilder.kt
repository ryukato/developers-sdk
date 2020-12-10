@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.yyoo.link.developers.sdk.http

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.util.*
// TODO create super type for this and inject to ApiClientFactory
internal class HttpClientBuilder {
    @KtorExperimentalAPI
    var engineFactory: HttpClientEngineFactory<HttpClientEngineConfig> = CIO
    var enableResponseValidation: Boolean = false
    var requestTimeout: Long = 10000L
    var enableLogging: Boolean = true
    var logger: Logger = Logger.DEFAULT
    var logLevel: LogLevel = LogLevel.BODY
    var serializer: JsonSerializer? = null

    @KtorExperimentalAPI
    fun build(
        serviceApiKey: String,
        serviceApiSecret: String
    ): HttpClient {
        return HttpClient(engineFactory) {
            this.expectSuccess = this@HttpClientBuilder.enableResponseValidation
            this.engine {
                if (this is CIOEngineConfig) {
                    this.requestTimeout = this@HttpClientBuilder.requestTimeout
                }
            }
            if (enableLogging) {
                this.install(Logging) {
                    this.logger = this@HttpClientBuilder.logger
                    this.level = this@HttpClientBuilder.logLevel
                }
            }
            this.install(ApiClientRequestFeature) {
                this.serviceApiKey = serviceApiKey
                this.serviceApiSecret = serviceApiSecret

            }
            this.install(JsonFeature) {
                this.serializer = this@HttpClientBuilder.serializer ?: JacksonSerializer()
            }
        }
    }
}


