package com.jasonsb.simplebible.network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

suspend fun <T : BaseResponse> Call<T>.syncRequest(): HttpRequestResult<T> {
    return try {
        suspendCancellableCoroutine { continuation: Continuation<HttpRequestResult<T>> ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    Timber.e("Request execution failed: ${call.request()} ${t.message}")
                    continuation.resume(ConnectionError(t))
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    continuation.resume(response.unwrap())
                }
            })
        }
    } catch (e: CancellationException) {
        Timber.d("Request has been cancelled")
        cancel()
        throw e
    }
}

private fun <T : BaseResponse> Response<T>.unwrap(): HttpRequestResult<T> {
    val body = body()
    return if (isSuccessful && body != null) {
        Success(body)
    } else {
        Error(errorBody())
    }
}

sealed class HttpRequestResult<out T : Any>

data class Success<T : Any>(val data: T) : HttpRequestResult<T>()

data class Error<T : Any>(val body: ResponseBody?) : HttpRequestResult<T>()

data class ConnectionError(val throwable: Throwable) : HttpRequestResult<Nothing>()