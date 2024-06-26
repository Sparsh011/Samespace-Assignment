package com.sparshchadha.samespaceassignment

sealed class Resource<T>(val data: T? = null, val error: Throwable? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(data: T? = null, error: Throwable) : Resource<T>(data, error)
}