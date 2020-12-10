package com.yyoo.link.developers.sdk.exception

import java.lang.IllegalArgumentException
import java.lang.RuntimeException

fun blankApiBaseUrl() = IllegalArgumentException("invalid api-base-url: blank")
fun blankServiceApiKey() = IllegalArgumentException("invalid service-api-key: blank")


class InvalidResponseValueException(message: String): RuntimeException(message)
