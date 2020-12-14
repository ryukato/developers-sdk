package com.yyoo.link.developers.sdk.autoconfigure

import com.yyoo.link.developers.sdk.api.ApiClient
import com.yyoo.link.developers.sdk.autoconfigure.ApiKeySecretLoader
import com.yyoo.link.developers.sdk.key.ApiKeySecretDecoder
import com.yyoo.link.developers.sdk.key.ApiKeySecretEncoder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest(
    classes = [TestApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class SpringAutoConfigTest {
    @Autowired
    private lateinit var context: ApplicationContext

    @Test
    fun test() {
        assertNotNull(context)
        assertNotNull(context.getBean(ApiClientProperties::class.java))
        assertNotNull(context.getBean(ApiKeySecretLoader::class.java))
        val base64ApiKeySecretEncoder = context.getBean("base64ApiKeySecretEncoder")
        assertNotNull(base64ApiKeySecretEncoder)
        assertTrue(base64ApiKeySecretEncoder is ApiKeySecretEncoder)
        val base64ApiKeySecretDecoder = context.getBean("base64ApiKeySecretDecoder")
        assertNotNull(base64ApiKeySecretDecoder)
        assertTrue(base64ApiKeySecretDecoder is ApiKeySecretDecoder)
        assertNotNull(context.getBean(ApiClient::class.java))
    }
}
