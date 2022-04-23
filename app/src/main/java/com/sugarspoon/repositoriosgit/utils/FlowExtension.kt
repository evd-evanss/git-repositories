package com.sugarspoon.repositoriosgit.utils

import com.google.gson.Gson
import com.sugarspoon.data.model.exception.ErrorResponse
import com.sugarspoon.data.model.exception.RetrofitException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

fun <T> Flow<T>.onResults(
    Success:(suspend (t: T) -> Unit)? = null,
    Error: ((e: Throwable) -> Unit)? = null,
    Loading: ((isLoading: Boolean) -> Unit)? = null,
    coroutineScope: CoroutineScope = CoroutineScope(IO)
) {
    coroutineScope.launch {
        withContext(Main) {
            Loading?.invoke(true)
        }
        try {
            collect { result ->
                Success?.let {
                    withContext(Main) {
                        it(result)
                        Loading?.invoke(false)
                    }
                }
            }
        } catch (e: Throwable) {
            withContext(Main) {
                if (e is HttpException) {
                    val json = e.response()?.errorBody()?.string()
                    try {
                        val message = Gson().fromJson(json, ErrorResponse::class.java)
                        Error?.invoke(RetrofitException.create(message.message, e, RetrofitException.Type.HTTP))
                    } catch (e: Exception) {
                        Error?.invoke(e)
                    }
                } else {
                    Error?.invoke(e)
                }
                Loading?.invoke(false)
            }
        }
    }

}