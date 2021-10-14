package com.swarn.jetpack.di.modules

import android.app.Application
import androidx.room.Room
import com.swarn.jetpack.BuildConfig
import com.swarn.jetpack.data.MovieRepository
import com.swarn.jetpack.data.local.MovieDao
import com.swarn.jetpack.data.local.TmdbDatabase
import com.swarn.jetpack.data.remote.MovieRemoteDataSource
import com.swarn.jetpack.network.AuthInterceptor
import com.swarn.jetpack.network.Config
import com.swarn.jetpack.network.services.MovieApi
import com.swarn.jetpack.ui.details.DetailTmdbFragment
import com.swarn.jetpack.ui.details.DetailTmdbViewModel
import com.swarn.jetpack.ui.home.HomeFragment
import com.swarn.jetpack.ui.home.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Swarn Singh.
 */

const val TMDB_SCOPE = "tmdb_scope"
val tmdbModules = module {

    fun provideTmdbRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor;
    }

    fun provideTmdbOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(BuildConfig.TMDB_API_KEY))
            .build()
    }

    fun provideTmdbDatabase(application: Application): TmdbDatabase {
        return Room.databaseBuilder(application, TmdbDatabase::class.java, "tmdb_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideMoviesDao(database: TmdbDatabase): MovieDao {
        return database.movieDao()
    }

    fun provideMoviesApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    scope(named(TMDB_SCOPE)) {
        scoped { provideHTTPLoggingInterceptor() }
        scoped { provideTmdbOkHttpClient(get()) }
        scoped { provideTmdbRetrofit(get()) }

        scoped { provideMoviesApi(get()) }
        scoped { MovieRepository(get(), get()) }
        scoped { MovieRemoteDataSource(get(), get()) }

        scoped { provideTmdbDatabase(androidApplication()) }
        scoped { provideMoviesDao(get()) }
    }

    scope<HomeFragment> {
        viewModel { HomeViewModel(get()) }
    }

    scope<DetailTmdbFragment> {
        viewModel { DetailTmdbViewModel(get()) }
    }
}


