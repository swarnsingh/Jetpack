package com.swarn.jetpack.data.remote.repository

import com.swarn.jetpack.data.Result
import com.swarn.jetpack.data.model.Photos
import com.swarn.jetpack.data.remote.ApiService
import com.swarn.jetpack.data.remote.BaseRepository
import kotlinx.coroutines.flow.Flow

class PhotosRepository(
    private val apiService: ApiService,
) : BaseRepository() {

    suspend fun getPhotos(): Flow<Result<List<Photos.Photo>>?> {
        return getResultStatusFlow {
            val photos = apiService.getPhotos().photos
            photos
        }
    }
}
