package com.yyoo.link.developers.sdk.autoconfigure

import com.yyoo.link.developers.sdk.api.ApiClient
import com.yyoo.link.developers.sdk.api.factory.ApiClientFactory
import com.yyoo.link.developers.sdk.api.factory.ApiClientFactoryConfig
import com.yyoo.link.developers.sdk.http.DefaultHttpClientBuilderImpl
import com.yyoo.link.developers.sdk.http.HttpClientBuilder
import com.yyoo.link.developers.sdk.key.ApiKeySecretDecoder
import com.yyoo.link.developers.sdk.key.ApiKeySecretEncoder
import com.yyoo.link.developers.sdk.key.ApiKeySecretLoader
import com.yyoo.link.developers.sdk.key.Base64ApiKeySecretSecret
import com.yyoo.link.developers.sdk.model.dto.ApiKeySecret
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private val logger = KotlinLogging.logger { }

@Suppress("EXPERIMENTAL_API_USAGE")
@Configuration
@ConfigurationPropertiesScan(basePackages = ["com.yyoo.link.developers.sdk.autoconfigure"])
@EnableConfigurationProperties
class ApiClientSdkAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApiClient::class)
    fun apiClient(
        apiClientProperties: ApiClientProperties,
        apiKeySecretLoader: ApiKeySecretLoader,
        apiClientFactory: ApiClientFactory
    ): ApiClient {
        val config = ApiClientFactoryConfig(
            apiBaseUrl = apiClientProperties.baseUrl,
            apiKeySecret = ApiKeySecret(
                apiKeySecretLoader.serviceApiKey(),
                apiKeySecretLoader.serviceApiSecret()
            )
        )
        val httpClientBuilder: HttpClientBuilder = DefaultHttpClientBuilderImpl()
        return apiClientFactory.build(config, httpClientBuilder)
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
