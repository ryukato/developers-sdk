package com.github.ryukato.link.developers.sdk.http

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*
import org.apache.commons.lang3.RandomStringUtils

internal class ApiClientRequestFeature(val config: Config) {
    /**
     * [ApiClientRequestFeature] feature configuration
     */
    class Config {
        var serviceApiKey: String? = null
        var serviceApiSecret: String? = null
    }

    fun nonce(): String = RandomStringUtils.randomAlphanumeric(8)
    fun timestamp(): String = System.currentTimeMillis().toString()

    companion object Feature : HttpClientFeature<Config, ApiClientRequestFeature> {
        private val json = io.ktor.client.features.json.defaultSerializer()
        private const val SERVICE_API_KEY_HEADER = "service-api-key"
        private const val SIGNATURE = "Signature"
        private const val TIMESTAMP = "Timestamp"
        private const val NONCE = "Nonce"

        override val key: AttributeKey<ApiClientRequestFeature> = AttributeKey("ApiRequestHeaders")

        override fun prepare(block: Config.() -> Unit): ApiClientRequestFeature {
            val config = Config().apply(block)
            return ApiClientRequestFeature(config)
        }

        override fun install(feature: ApiClientRequestFeature, scope: HttpClient) {

            scope.requestPipeline.intercept(HttpRequestPipeline.Transform) {
                val config = feature.config
                val serviceApiKey = config.serviceApiKey ?: throw RuntimeException("service-api-key missing")
                val nonce = feature.nonce()
                val timestamp = this.context.headers[TIMESTAMP] ?: feature.timestamp()

                @Suppress("UNCHECKED_CAST") val body = when (this.context.body) {
                    is Map<*, *> -> this.context.body as Map<String, Any>
                    else -> emptyMap()
                }

                val signature = signature(
                    config.serviceApiSecret!!,
                    context.method.value,
                    context.url.encodedPath,
                    timestamp.toLong(),
                    nonce,
                    body
                )
                context.headers.append(SERVICE_API_KEY_HEADER, serviceApiKey)
                context.headers.append(SIGNATURE, signature)
                context.headers.append(NONCE, nonce)
                if (this.context.headers[TIMESTAMP] == null) {
                    context.headers.append(TIMESTAMP, timestamp)
                }
                proceedWith(json.write(this.context.body))
            }
        }
    }
}

@Suppress("FunctionName")
internal fun HttpClientConfig<*>.ApiClientRequestFeature(block: ApiClientRequestFeature.Config.() -> Unit = {}) {
    install(ApiClientRequestFeature, block)
}
