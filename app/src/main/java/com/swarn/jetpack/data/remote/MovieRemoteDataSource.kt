package com.swarn.jetpack.data.remote

import com.swarn.jetpack.model.MovieDesc
import com.swarn.jetpack.model.Result
import com.swarn.jetpack.model.TrendingMovieResponse
import com.swarn.jetpack.network.services.MovieApi
import com.swarn.jetpack.util.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit

/**
 * fetches data from remote source
 */
class MovieRemoteDataSource constructor(
    private val retrofit: Retrofit,
    private val movieService: MovieApi
) {

    suspend fun fetchTrendingMovies(): Result<TrendingMovieResponse> {
        return getResponse(
            request = { movieService.getPopularMovies() },
            defaultErrorMessage = "Error fetching Movie list"
        )
    }

    suspend fun fetchMovie(id: Int): Result<MovieDesc> {
        return getResponse(
            request = { movieService.getMovie(id) },
            defaultErrorMessage = "Error fetching Movie Description"
        )
    }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                Result.Success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.Error(errorResponse?.status_message ?: defaultErrorMessage)
            }
        } catch (e: Throwable) {
            Result.Error("Unknown Error")
        }
    }
}