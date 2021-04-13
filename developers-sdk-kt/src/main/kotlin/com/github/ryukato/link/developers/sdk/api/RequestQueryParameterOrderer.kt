package com.github.ryukato.link.developers.sdk.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.TreeMap

interface RequestQueryParameterOrderer : QueryParameterOrderer, Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = orderedQueryParameters(request)
        return chain.proceed(request.newBuilder().url(httpUrl).build())
    }

    fun orderedQueryParameters(request: Request): HttpUrl
}

class DefaultRequestQueryParameterOrderer : RequestQueryParameterOrderer {
    override fun sort(queryParams: Map<String, List<String?>>): Map<String, List<String?>> {
        return TreeMap<String, List<String?>>(queryParams)
    }

    override fun orderedQueryParameters(request: Request): HttpUrl {
        val queryParams = sort(queryParameters(request))
        return when {
            queryParams.isEmpty() -> {
                request.url
            }
            else -> {
                val newUrlBuilder = request.url.newBuilder()
                newUrlBuilder.clearAllQueryParameters(request.url)
                queryParams
                    .filter { it.value.first()?.isNotBlank() ?: false }
                    .forEach {
                        newUrlBuilder.addQueryParameter(it.key, it.value.first())
                    }
                newUrlBuilder.build()
            }
        }
    }

    private fun queryParameters(request: Request): Map<String, List<String?>> {
        return request.url.queryParameterNames.map {
            it to request.url.queryParameterValues(it)
        }.toMap()
    }
}

fun HttpUrl.Builder.clearAllQueryParameters(httpUrl: HttpUrl) {
    httpUrl.queryParameterNames.forEach {
        this.removeAllQueryParameters(it)
    }
}
