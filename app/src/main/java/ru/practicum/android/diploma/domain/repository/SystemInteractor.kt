package ru.practicum.android.diploma.domain.repository

interface SystemInteractor {
    fun getString(id: Int): String

    fun showToast(message: String)
}
