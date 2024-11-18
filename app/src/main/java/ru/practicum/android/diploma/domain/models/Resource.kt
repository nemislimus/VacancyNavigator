package ru.practicum.android.diploma.domain.models

sealed interface Resource<T> {
    data class Success<T>(val data: T) : Resource<T>
    data class ConnectionError<T>(val message: String) : Resource<T>
    data class NotFoundError<T>(val message: String) : Resource<T>
    data class ServerError<T>(val message: String) : Resource<T>
    companion object {
        const val CHECK_CONNECTION = "check connection"
        const val SERVER_ERROR = "error code"
        const val NOT_FOUND = "404 - not founded"
    }
}
