package com.piashcse.utils

import io.ktor.http.*

data class Response(
    val isSuccess: Boolean,
    val statusCode: HttpStatusCode? = null,
    val data: Any? = null,
    val message: Any? = null,
    val pageResult: PageResult? = null
)

object ApiResponse {
    fun <T> success(data: T, statsCode: HttpStatusCode?) =
        Response(true, data = data, statusCode = statsCode)

    fun <T> success(data: T, statsCode: HttpStatusCode?, message: Any?) =
        Response(true, data = data, statusCode = statsCode, message = message)

    fun <T> success(data: T, statsCode: HttpStatusCode?, pageResult: PageResult?) =
        Response(true, data = data, statusCode = statsCode, pageResult = pageResult)

    fun <T> failure(error: T, statsCode: HttpStatusCode?) = Response(false, message = error, statusCode = statsCode)
}

data class PageResult(
    val totalRecord: Int?,
    val totalPage: Int?,
    val currentPage: Int?,
)
