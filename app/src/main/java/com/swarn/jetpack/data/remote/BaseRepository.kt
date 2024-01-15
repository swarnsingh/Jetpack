package com.swarn.jetpack.data.remote

import com.swarn.jetpack.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository(val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend inline fun <reified T> getResultStatusFlow(
        crossinline function: suspend () -> T,
    ): Flow<Result<T>> {
        return flow {
            emit(Result.Loading())
            val response = function()
            emit(Result.Success(response))
        }.catch { throwable ->
            emit(Result.Error(null, throwable))
        }.flowOn(defaultDispatcher)
    }
}