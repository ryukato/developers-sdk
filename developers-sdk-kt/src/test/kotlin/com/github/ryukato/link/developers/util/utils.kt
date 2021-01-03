package com.github.ryukato.link.developers.util

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.random.Random
import kotlin.streams.toList

val random: Random = Random(System.nanoTime())
const val PORT_RANGE_MIN = 1024
const val PORT_RANGE_MAX = 65535


fun loadJsonToString(classpathResource: String, claz: Class<*>): String {
    val bufferedReader =
        claz.classLoader.getResourceAsStream(classpathResource)?.bufferedReader()
    return bufferedReader?.lines()?.toList()?.joinToString(" ") ?: "{}"
}

fun LocalDateTime.toEpochMilli(): Long = this.toInstant(ZoneOffset.UTC).toEpochMilli()
