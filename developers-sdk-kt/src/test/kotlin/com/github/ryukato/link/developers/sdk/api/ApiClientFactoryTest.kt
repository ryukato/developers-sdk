package com.github.ryukato.link.developers.sdk.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Request
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.http.GET
import java.util.TreeMap

class ApiClientFactoryTest {
    private lateinit var apiClientFactory: ApiClientFactory

    @BeforeEach
    fun setUp() {
        apiClientFactory = ApiClientFactory()
    }

    @Test
    fun test_build() {
        val apiClient: ApiClient = apiClientFactory.build(
            "http://localhost:8080",
            object : RequestHeadersAppender {
                override fun createNewHeaders(request: Request): Headers {
                    return request.headers.newBuilder().build()
                }
            },
            object: RequestQueryParameterOrderer {
                override fun sort(queryParams: Map<String, List<String?>>): Map<String, List<String?>> {
                    return TreeMap<String, List<String?>>(queryParams)
                }

                override fun orderedQueryParameters(request: Request): HttpUrl {
                    return request.url
                }
            },
            false,
            jacksonObjectMapper()
        )

        assertNotNull(apiClient)
    }
}

interface TestApiService {
    @GET("")
    fun test(): String
}
