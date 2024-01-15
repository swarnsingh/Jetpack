package com.swarn.jetpack.data.remote

import com.swarn.jetpack.data.model.Photos
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

interface ApiService {

    suspend fun getPhotos(): Photos

    companion object {
        fun create(): ApiService {
            return ApiServiceImpl(
                client = HttpClient(Android) {
                    install(ContentNegotiation) {
                        gson()
                    }
                }
            )
        }
    }
}
