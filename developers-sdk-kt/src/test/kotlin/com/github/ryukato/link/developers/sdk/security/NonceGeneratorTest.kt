package com.github.ryukato.link.developers.sdk.security

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NonceGeneratorTest {
    private lateinit var nonceGenerator: NonceGenerator

    @BeforeEach
    fun setUp() {
        nonceGenerator = DefaultStringNonceGenerator()
    }


    @Test
    fun test_newNonce() {
        val nonce1 = nonceGenerator.newNonce()
        assertTrue(nonce1.length == 8)

        val nonce2 = nonceGenerator.newNonce()
        assertTrue(nonce1 != nonce2)
    }
}
