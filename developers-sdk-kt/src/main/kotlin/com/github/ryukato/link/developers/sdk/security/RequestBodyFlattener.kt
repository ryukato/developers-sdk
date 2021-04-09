package com.github.ryukato.link.developers.sdk.security

import org.apache.commons.lang3.StringUtils
import java.util.TreeMap

interface RequestBodyFlattener {
    fun flatten(body: Map<String, Any?>): String
}

class DefaultRequestBodyFlattener: RequestBodyFlattener {
    override fun flatten(body: Map<String, Any?>): String {
        val bodyTreeMap = TreeMap<String, Any?>()
        bodyTreeMap.putAll(body)

        @Suppress("UNCHECKED_CAST")
        return if (bodyTreeMap.isEmpty()) {
            ""
        } else {
            bodyTreeMap.filterValues { it != null }.map { (k, v) ->
                when (v) {
                    is String -> "$k=$v"
                    is List<*> -> {
                        val listTreeMap = TreeMap<String, String?>()
                        v as List<Map<String, String>>
                        v.forEachIndexed { index, map ->
                            map.keys.union(listTreeMap.keys).forEach { key ->
                                val value = map[key] ?: StringUtils.EMPTY
                                if (listTreeMap[key] == null) {
                                    listTreeMap[key] = "${",".repeat(index)}$value"
                                } else {
                                    listTreeMap[key] = "${listTreeMap[key]},$value"
                                }
                            }
                        }
                        listTreeMap.map { (lk, kv) ->
                            "$k.$lk=$kv"
                        }.joinToString("&")
                    }
                    else -> throw IllegalArgumentException()
                }
            }.joinToString("&")
        }
    }
}
