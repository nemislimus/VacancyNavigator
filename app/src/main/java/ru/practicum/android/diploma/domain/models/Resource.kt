package ru.practicum.android.diploma.domain.models

sealed interface Resource<T> {
    data class Success<T>(val data: T) : Resource<T>
    data class ConnectionError<T>(val message: String) : Resource<T>
    data class NotFoundError<T>(val message: String) : Resource<T>
    data class ServerError<T>(val message: String) : Resource<T>
}
