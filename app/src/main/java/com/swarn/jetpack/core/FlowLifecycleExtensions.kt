package com.swarn.jetpack.core

import android.view.View
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline fun <T> Flow<T>.collectWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.CREATED,
    crossinline body: suspend CoroutineScope.(T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            collect { body(it) }
        }
    }
}

@Composable
fun <T> Flow<T>.CollectAsEffect(
    context: CoroutineContext = EmptyCoroutineContext,
    block: (T) -> Unit
) {
    LaunchedEffect(key1 = this) {
        onEach(block).flowOn(context).launchIn(this)
    }
}

inline fun <T> Flow<T>.collectWithLifecycle(
    fragment: Fragment,
    lifecycleState: Lifecycle.State = Lifecycle.State.CREATED,
    crossinline body: suspend CoroutineScope.(T) -> Unit
) {
    collectWithLifecycle(fragment.viewLifecycleOwner, lifecycleState, body)
}

inline fun <T> Flow<T>.collectWithLifecycle(
    view: View,
    lifecycleState: Lifecycle.State = Lifecycle.State.CREATED,
    crossinline body: suspend CoroutineScope.(T) -> Unit
) {
    view.findViewTreeLifecycleOwner()?.let { collectWithLifecycle(it, lifecycleState, body) }
}

fun TextView.bindText(stateFlow: StateFlow<CharSequence?>?) {
    stateFlow?.collectWithLifecycle(findViewTreeLifecycleOwner()!!) {
        if (text.toString() != it) this@bindText.text = it
    }
}

/**
 * Returns a buffered representation of given flow that buffers its emitted values until it is collected.
 * The resulting flow will emit values only to one collector.
 */
fun <T> SharedFlow<T>.toBufferedFlow(scope: CoroutineScope, capacity: Int = 1000): Flow<T> {
    return buffer(capacity, BufferOverflow.DROP_OLDEST).shareIn(
        scope,
        SharingStarted.Eagerly,
        capacity
    )
}
