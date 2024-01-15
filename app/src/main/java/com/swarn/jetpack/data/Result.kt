package com.swarn.jetpack.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.transformWhile

sealed class Result<T>(open val data: T?) {
    class Success<T>(override val data: T?) : Result<T>(data)
    class Error<T>(override val data: T? = null, val throwable: Throwable) : Result<T>(data)
    class Loading<T>(override val data: T? = null) : Result<T>(data)
}

suspend fun <T : Result<*>?> Flow<T>.collectUntilSuccessOrError(collector: FlowCollector<T>? = null) {
    transformWhile { result ->
        emit(result)
        result !is Result.Success<*> && result !is Result.Error<*>
    }.collect { collector?.emit(it) }
}