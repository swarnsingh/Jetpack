package com.swarn.jetpack.data.remote

import com.google.gson.Gson
import com.swarn.jetpack.data.model.Photos
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun getPhotos(): Photos {
        val response = client.get(ApiRoutes.GET_PHOTOS)
        return Gson().fromJson(response.bodyAsText(), Photos::class.java)
    }
}
