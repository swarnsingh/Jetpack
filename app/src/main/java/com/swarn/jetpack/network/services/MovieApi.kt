package com.swarn.jetpack.network.services

import com.swarn.jetpack.model.MovieDesc
import com.swarn.jetpack.model.TrendingMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API Service
 */
interface MovieApi {

    @GET("/3/trending/movie/week")
    suspend fun getPopularMovies(): Response<TrendingMovieResponse>

    @GET("/3/movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int): Response<MovieDesc>
}