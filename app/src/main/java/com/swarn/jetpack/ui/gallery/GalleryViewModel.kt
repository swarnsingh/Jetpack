package com.swarn.jetpack.ui.gallery

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GalleryViewModel : ViewModel() {

    init {
        println()
    }
    private val _counter = MutableStateFlow(0)
    val counter
        get() = _counter.asStateFlow()

    fun incrementCounter() {
        _counter.value = _counter.value.plus(1)
    }

    fun decrementCounter() {
        _counter.value = _counter.value.minus(1)
    }
}