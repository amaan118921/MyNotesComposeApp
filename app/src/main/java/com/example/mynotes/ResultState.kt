package com.example.mynotes


sealed class ResultState<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int? = null
) {
    class Success<T>(data: T?) : ResultState<T>(data)
    class Loading<T>(data: T? = null) : ResultState<T>(data)
    class Error<T>(data: T?, msg: String, code: Int = 501) : ResultState<T>(data, msg, code)
}
