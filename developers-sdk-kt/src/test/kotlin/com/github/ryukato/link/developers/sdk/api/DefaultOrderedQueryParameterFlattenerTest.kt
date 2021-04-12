package com.github.ryukato.link.developers.sdk.api

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultOrderedQueryParameterFlattenerTest {
    private lateinit var queryParameterFlattener: QueryParameterFlattener

    @BeforeEach
    fun setUp() {
        queryParameterFlattener = DefaultOrderedQueryParameterFlattener()
    }
    @Test
    fun `flatten request body with all paging query-parameters`() {
        val requestParams =  mapOf<String, List<String?>>(
            "limit" to  listOf("10"),
            "page" to   listOf("1"),
            "orderBy" to   listOf("desc"),
            "msgType" to  listOf("collection/MsgTransferNFTFrom"),
            "after" to  listOf("1614563991000"),
            "before" to  listOf("1617155991000")
        )
        val flatten = queryParameterFlattener.flatten(requestParams)
        val expectedFlatten = "after=1614563991000&before=1617155991000&limit=10&msgType=collection/MsgTransferNFTFrom&orderBy=desc&page=1"

        Assertions.assertEquals(expectedFlatten, flatten)
    }
}
