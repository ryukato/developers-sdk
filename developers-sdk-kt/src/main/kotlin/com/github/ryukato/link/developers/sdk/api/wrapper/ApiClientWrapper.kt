package com.github.ryukato.link.developers.sdk.api.wrapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ryukato.link.developers.sdk.model.response.ErrorResponse
import com.github.ryukato.link.developers.sdk.model.response.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

open class ApiClientWrapper(private val objectMapper: ObjectMapper) {
    suspend fun <T> invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when(throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        ResultWrapper.GenericError(code, errorResponse)
                    }
                    else -> ResultWrapper.GenericError(null, null)
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val typeObject = object : TypeReference<ErrorResponse>() {}
                objectMapper.readValue(it.inputStream(), typeObject)
            }
        } catch (exception: Exception) {
            null
        }
    }
}
