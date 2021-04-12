package com.github.ryukato.link.developers.sdk.api

import org.apache.commons.lang3.RandomStringUtils

interface NonceGenerator {
    fun newNonce(): String
}

class DefaultStringNonceGenerator: NonceGenerator {
    override fun newNonce(): String {
        return RandomStringUtils.randomAlphanumeric(8)
    }
}
