package com.github.ryukato.link.developers.sdk.key

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

/*
 * To generate test samples, run following commands
 *  - encoding: echo -n '349c7604-31dc-4488-a92c-ecc869c53bd1' | base64
 *  - decoding echo -n 'N2ExMjI3NDktZDVhZS00ZDU0LTk5ZDItMGFiZTRiN2IxNWVh' | base64 -D
 */
class Base64ApiKeySecretSecretTest {
	class TestApiKeyEncoderParameterProvider : ArgumentsProvider {
		override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
			return Stream.of(
				Arguments.of(
					"349c7604-31dc-4488-a92c-ecc869c53bd1",
					"MzQ5Yzc2MDQtMzFkYy00NDg4LWE5MmMtZWNjODY5YzUzYmQx"
				),
				Arguments.of(
					"7a122749-d5ae-4d54-99d2-0abe4b7b15ea",
					"N2ExMjI3NDktZDVhZS00ZDU0LTk5ZDItMGFiZTRiN2IxNWVh"
				)
			)
		}
	}

	class TestApiKeyDecoderParameterProvider : ArgumentsProvider {
		override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
			return Stream.of(
				Arguments.of(
					"MzQ5Yzc2MDQtMzFkYy00NDg4LWE5MmMtZWNjODY5YzUzYmQx",
					"349c7604-31dc-4488-a92c-ecc869c53bd1"
				),
				Arguments.of(
					"N2ExMjI3NDktZDVhZS00ZDU0LTk5ZDItMGFiZTRiN2IxNWVh",
					"7a122749-d5ae-4d54-99d2-0abe4b7b15ea"
				)
			)
		}
	}

	private lateinit var apiKeySecretEncoder: ApiKeySecretEncoder
	private lateinit var apiKeySecretDecoder: ApiKeySecretDecoder
	@BeforeEach
	fun setUp() {
		apiKeySecretEncoder = Base64ApiKeySecretSecret()
		apiKeySecretDecoder = apiKeySecretEncoder as ApiKeySecretDecoder
	}

	@ArgumentsSource(TestApiKeyEncoderParameterProvider::class)
	@ParameterizedTest
	fun `test encoding`(testInput: String, expectedValue: String) {
		assertEquals(expectedValue, apiKeySecretEncoder.encodeApiKey(testInput))
	}

	@ArgumentsSource(TestApiKeyDecoderParameterProvider::class)
	@ParameterizedTest
	fun `test decoding`(testInput: String, expectedValue: String) {
		assertEquals(expectedValue, apiKeySecretDecoder.decodeApiKey(testInput))
	}

}
