package com.swarn.jetpack.model

/**
 * Generic class for holding success response, error response and loading status
 */
sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T? = null) : Result<T>(data)
    class Loading<T>(data: T? = null) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
}