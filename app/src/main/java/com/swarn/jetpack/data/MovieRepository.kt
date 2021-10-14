package com.swarn.jetpack.data

import com.swarn.jetpack.data.local.MovieDao
import com.swarn.jetpack.data.remote.MovieRemoteDataSource
import com.swarn.jetpack.model.MovieDesc
import com.swarn.jetpack.model.Result
import com.swarn.jetpack.model.TrendingMovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Repository which fetches data from Remote or Local data sources
 */
class MovieRepository constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieDao: MovieDao
) {

    suspend fun fetchTrendingMovies(): Flow<Result<TrendingMovieResponse>?> {
        return flow {
            emit(fetchTrendingMoviesCached())
            emit(Result.Loading())
            val result = movieRemoteDataSource.fetchTrendingMovies()

            //Cache to database if response is successful

            if (result is Result.Success) {
                result.data?.results?.let { it ->
                    movieDao.deleteAll(it)
                    movieDao.insertAll(it)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    private fun fetchTrendingMoviesCached(): Result<TrendingMovieResponse>? =
        movieDao.getAll()?.let {
            Result.Success(TrendingMovieResponse(it))
        }

    suspend fun fetchMovie(id: Int): Flow<Result<MovieDesc>> {
        return flow {
            emit(Result.Loading())
            emit(movieRemoteDataSource.fetchMovie(id))
        }.flowOn(Dispatchers.IO)
    }
}