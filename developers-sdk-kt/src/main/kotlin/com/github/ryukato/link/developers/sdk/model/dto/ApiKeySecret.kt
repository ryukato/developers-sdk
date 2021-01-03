package com.github.ryukato.link.developers.sdk.model.dto

data class ApiKeySecret(val key: String, val secret: String) {
    init {
        require(key.isNotBlank()) { "invalid service-api-key: blank" }
        require(secret.isNotBlank()) { "invalid service-api-secret: blank" }
    }
}
