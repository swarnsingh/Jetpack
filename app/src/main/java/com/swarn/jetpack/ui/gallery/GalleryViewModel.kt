package com.swarn.jetpack.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swarn.jetpack.data.MovieRepository
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.inject

class GalleryViewModel : ViewModel() {

    private val movieRepository : MovieRepository by inject(MovieRepository::class.java)

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}