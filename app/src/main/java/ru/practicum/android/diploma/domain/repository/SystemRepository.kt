package ru.practicum.android.diploma.domain.repository

interface SystemRepository {
    fun getString(id: Int): String

    fun showToast(message: String)
}
