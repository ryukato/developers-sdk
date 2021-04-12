package com.github.ryukato.link.developers.sdk.api

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultRequestBodyFlattenerTest {

    private lateinit var requestBodyFlattener: RequestBodyFlattener

    @BeforeEach
    fun setUp() {
        requestBodyFlattener = DefaultRequestBodyFlattener()
    }

    @Test
    fun `flatten request body with mint-list parameter value test`() {
        val requestParams = mapOf(
            "ownerAddress" to "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret" to "uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=",
            "toAddress" to "tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp",
            "mintList" to listOf(
                mapOf(
                    "tokenType" to "10000001",
                    "name" to "NewNFT"
                ),
                mapOf(
                    "tokenType" to "10000003",
                    "name" to "NewNFT2",
                    "meta" to "New nft 2 meta information"
                )
            )
        )
        val flatten = requestBodyFlattener.flatten(requestParams)
        val expectedFlatten = "mintList.meta=,New nft 2 meta information&mintList.name=NewNFT,NewNFT2&mintList.tokenType=10000001,10000003&ownerAddress=tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq&ownerSecret=uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=&toAddress=tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp"

        assertEquals(expectedFlatten, flatten)
    }
}
