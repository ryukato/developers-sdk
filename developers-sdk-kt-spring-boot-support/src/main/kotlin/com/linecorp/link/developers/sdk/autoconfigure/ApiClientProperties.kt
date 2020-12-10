package com.linecorp.link.developers.sdk.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "developers-api", ignoreUnknownFields = false)
data class ApiClientProperties(val baseUrl: String, val serviceApiKey: String, val serviceApiSecret: String)
