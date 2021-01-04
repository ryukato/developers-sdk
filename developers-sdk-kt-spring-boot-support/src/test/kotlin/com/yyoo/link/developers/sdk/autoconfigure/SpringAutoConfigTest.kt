package com.yyoo.link.developers.sdk.autoconfigure

import com.github.ryukato.link.developers.sdk.api.ApiClient
import com.github.ryukato.link.developers.sdk.key.ApiKeySecretDecoder
import com.github.ryukato.link.developers.sdk.key.ApiKeySecretEncoder
import com.github.ryukato.link.developers.sdk.key.ApiKeySecretLoader
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
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
		val apiClientProperties = context.getBean(ApiClientProperties::class.java)
		assertNotNull(apiClientProperties)
		assertNotNull(apiClientProperties.baseUrl)
		assertNotNull(apiClientProperties.serviceApiKey)
		assertNotNull(apiClientProperties.serviceApiSecret)
		assertNotNull(context.getBean(ApiKeySecretLoader::class.java))
		val base64ApiKeySecretEncoder: ApiKeySecretEncoder =
			context.getBean("base64ApiKeySecretEncoder", ApiKeySecretEncoder::class.java)
		assertNotNull(base64ApiKeySecretEncoder)
		val base64ApiKeySecretDecoder =
			context.getBean("base64ApiKeySecretDecoder", ApiKeySecretDecoder::class.java)
		assertNotNull(base64ApiKeySecretDecoder)
		val apiClient = context.getBean(ApiClient::class.java)
		assertNotNull(apiClient)

		runBlocking {
			val timeResponse = apiClient.time()
			assertNotNull(timeResponse)

			val serviceTokens = apiClient.serviceTokens()
			assertNotNull(serviceTokens)

			apiClient.itemToken("323d700a")
		}
	}
}
