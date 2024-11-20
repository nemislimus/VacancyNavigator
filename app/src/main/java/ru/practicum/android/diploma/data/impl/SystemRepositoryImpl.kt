package ru.practicum.android.diploma.data.impl

import android.content.Context
import android.widget.Toast
import ru.practicum.android.diploma.domain.repository.SystemRepository

class SystemRepositoryImpl(private val context: Context) : SystemRepository {
    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
