package com.github.ryukato.link.developers.sdk.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

open class ApiClientFactory {
    fun build(
        baseUrl: String,
        requestHeadersAppender: RequestHeadersAppender,
        enableLogging: Boolean,
        jacksonObjectMapper: ObjectMapper = jacksonObjectMapper(),
    ): ApiClient {
        val okHttp3Client: OkHttpClient =
            httpClient(requestHeadersAppender, enableLogging)
        val retrofit = retrofit(baseUrl, okHttp3Client, jacksonObjectMapper)
        return retrofit.create(ApiClient::class.java)
    }

    open fun retrofit(
        baseUrl: String,
        okHttp3Client: OkHttpClient,
        jacksonObjectMapper: ObjectMapper = jacksonObjectMapper(),
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper))
            .client(okHttp3Client)
            .build()
    }

    open fun httpClient(
        requestHeadersAppender: RequestHeadersAppender,
        enableLogging: Boolean,
    ): OkHttpClient {
        val logLevel = if (!enableLogging) HttpLoggingInterceptor.Level.NONE else HttpLoggingInterceptor.Level.BODY
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(logLevel)

        return OkHttpClient.Builder()
            .addInterceptor(requestHeadersAppender)
            .addInterceptor(loggingInterceptor).build()
    }
}
