package com.github.ryukato.link.developers.sdk.api

import okhttp3.HttpUrl
import okhttp3.Request
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class RequestQueryParameterOrdererTest {
    private lateinit var requestQueryParameterOrderer: RequestQueryParameterOrderer

    @BeforeEach
    fun setUp() {
        requestQueryParameterOrderer = DefaultRequestQueryParameterOrderer()
    }

    @Test
    fun test_order_query_params() {
        val after = LocalDateTime.now().minusYears(1).toEpochMilli().toString()
        val orderBy = "desc"
        val page = "1"
        val limit = "10"
        val before = LocalDateTime.now().toEpochMilli().toString()
        val msgType = "token/MsgMint"
        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .addQueryParameter("before", before)
            .addQueryParameter("msgType", msgType)
            .addQueryParameter("limit", limit)
            .addQueryParameter("page", page)
            .addQueryParameter("orderBy", orderBy)
            .addQueryParameter("after", after)
            .build()
        val request: Request = Request.Builder()
            .url(httpUrl)
            .build()
        val httpUrlWithOrderedQueryParams = requestQueryParameterOrderer.orderedQueryParameters(request)
        val expectedQueryParams = sortedMapOf(
            "after" to after,
            "before" to before,
            "limit" to limit,
            "msgType" to msgType,
            "orderBy" to orderBy,
            "page" to page)
        assertEquals(expectedQueryParams.keys, httpUrlWithOrderedQueryParams.queryParameterNames)

        expectedQueryParams.forEach { (k, v) ->
            assertEquals(v, httpUrlWithOrderedQueryParams.queryParameter(k))
        }
    }

    @Test
    fun test_exclude_null_query_params() {
        val before = LocalDateTime.now().toEpochMilli().toString()

        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .addQueryParameter("before", before)
            .addQueryParameter("after", null)
            .build()
        val request: Request = Request.Builder()
            .url(httpUrl)
            .build()
        val httpUrlWithOrderedQueryParams = requestQueryParameterOrderer.orderedQueryParameters(request)
        val expectedQueryParams = sortedMapOf("before" to before)
        assertEquals(expectedQueryParams.keys, httpUrlWithOrderedQueryParams.queryParameterNames)

        expectedQueryParams.forEach { (k, v) ->
            assertEquals(v, httpUrlWithOrderedQueryParams.queryParameter(k))
        }
    }

    @Test
    fun test_empty_query_params() {
        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .build()
        val request: Request = Request.Builder()
            .url(httpUrl)
            .build()
        val httpUrlWithOrderedQueryParams = requestQueryParameterOrderer.orderedQueryParameters(request)
        val expectedQueryParams = emptyMap<String, String>()
        assertEquals(expectedQueryParams.keys, httpUrlWithOrderedQueryParams.queryParameterNames)
    }
}
