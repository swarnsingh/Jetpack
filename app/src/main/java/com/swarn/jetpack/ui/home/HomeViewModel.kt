package com.swarn.jetpack.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swarn.jetpack.data.Result.Error
import com.swarn.jetpack.data.Result.Loading
import com.swarn.jetpack.data.Result.Success
import com.swarn.jetpack.data.collectUntilSuccessOrError
import com.swarn.jetpack.data.model.Photos
import com.swarn.jetpack.data.remote.repository.PhotosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val photosRepository: PhotosRepository
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photos.Photo>>(emptyList())
    val photos
        get() = _photos.asStateFlow()

    private var job: Job? = null

    fun start() {
        cancelJob()
        job = viewModelScope.launch {
            photosRepository.getPhotos().collectUntilSuccessOrError { response ->
                when (response) {
                    is Loading -> {

                    }

                    is Success -> {
                        response.data?.let {
                            _photos.value = it
                        }
                    }

                    is Error -> {
                        println("Error : ${response.throwable.message}")
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    private fun cancelJob() {
        job?.cancel()
        job = null
    }
}