package com.swarn.jetpack.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swarn.jetpack.data.MovieRepository
import com.swarn.jetpack.model.MovieDesc
import com.swarn.jetpack.model.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailTmdbViewModel constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private val _movieDesc = MutableLiveData<Result<MovieDesc>>()
    private val movieDesc: LiveData<Result<MovieDesc>>
        get() = _movieDesc

    fun getMovieDetail(id: Int): LiveData<Result<MovieDesc>> {
        viewModelScope.launch {
            movieRepository.fetchMovie(id).collect {
                _movieDesc.value = it
            }
        }
        _movieDesc.value = Result.Loading()
        return movieDesc
    }
}