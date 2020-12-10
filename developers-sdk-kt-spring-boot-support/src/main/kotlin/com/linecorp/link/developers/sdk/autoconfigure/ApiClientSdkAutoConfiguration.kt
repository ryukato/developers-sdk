package com.linecorp.link.developers.sdk.autoconfigure

import com.linecorp.link.developers.sdk.api.ApiClient
import com.linecorp.link.developers.sdk.api.factory.ApiClientFactory
import com.linecorp.link.developers.sdk.key.ApiKeySecretDecoder
import com.linecorp.link.developers.sdk.key.ApiKeySecretEncoder
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private val logger = KotlinLogging.logger { }

@Suppress("EXPERIMENTAL_API_USAGE")
@Configuration
@ConfigurationPropertiesScan(basePackages = ["com.linecorp.link.developers.sdk.autoconfigure"])
@EnableConfigurationProperties
class ApiClientSdkAutoConfiguration {



    @Bean
    @ConditionalOnMissingBean(ApiClient::class)
    fun apiClient(
        apiClientProperties: ApiClientProperties,
        apiKeySecretLoader: ApiKeySecretLoader,
        apiClientFactory: ApiClientFactory
    ): ApiClient {
        return apiClientFactory.build(
            apiBaseUrl = apiClientProperties.baseUrl,
            serviceApiKey = apiKeySecretLoader.serviceApiKey(),
            serviceApiSecret = apiKeySecretLoader.serviceApiSecret()
        )
    }

    @Bean
    @ConditionalOnMissingBean(ApiClientFactory::class)
    fun apiClientFactory(): ApiClientFactory = ApiClientFactory()

    @Bean
    @ConditionalOnMissingBean(ApiKeySecretLoader::class)
    fun apiKeySecretLoader(
        apiClientProperties: ApiClientProperties,
        apiKeySecretDecoder: ApiKeySecretDecoder
    ): ApiKeySecretLoader {
        return object : ApiKeySecretLoader {
            override fun serviceApiKey(): String {
                return apiKeySecretDecoder.decodeApiKey(apiClientProperties.serviceApiKey)
            }

            override fun serviceApiSecret(): String {
                return apiKeySecretDecoder.decodeSecret(apiClientProperties.serviceApiSecret)
            }
        }
    }

    @Bean(name = ["base64ApiKeySecretEncoder"])
    @ConditionalOnMissingBean(ApiKeySecretEncoder::class)
    fun apiKeySecretEncoder(): ApiKeySecretEncoder {
        logger.warn { "SHOULD NOT USE this - \"base64ApiKeySecretEncoder\", PLEASE USE MORE SECURE ONE" }
        return base64ApiKeySecretSecret
    }

    @Bean(name = ["base64ApiKeySecretDecoder"])
    @ConditionalOnMissingBean(ApiKeySecretDecoder::class)
    fun apiKeySecretDecoder(): ApiKeySecretDecoder {
        logger.warn { "SHOULD NOT USE this - \"base64ApiKeySecretDecoder\", PLEASE USE MORE SECURE ONE" }
        return base64ApiKeySecretSecret
    }


    companion object {
        val base64ApiKeySecretSecret = Base64ApiKeySecretSecret()
    }
}
