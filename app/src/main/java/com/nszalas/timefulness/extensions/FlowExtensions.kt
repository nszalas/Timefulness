package com.nszalas.timefulness.extensions

import kotlinx.coroutines.flow.MutableStateFlow

inline fun <T: Any> MutableStateFlow<T>.mutate(block: T.() -> T) {
    value = value.block()
}