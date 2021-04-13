package com.github.ryukato.link.developers.sdk.api

interface QueryParameterOrderer {
    fun sort(queryParams: Map<String, List<String?>>): Map<String, List<String?>>
    fun order(queryParams: Map<String, List<String?>>): Map<String, List<String?>> {
        return sort(queryParams)
    }
}
