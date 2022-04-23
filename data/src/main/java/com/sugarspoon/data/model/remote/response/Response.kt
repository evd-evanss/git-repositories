package com.sugarspoon.data.model.remote.response

sealed class Response {
    data class Success<T>(val value: T): Response()
    data class Error<T>(val value: T): Response()
    object Completed: Response()
}