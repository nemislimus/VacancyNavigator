package ru.practicum.android.diploma.data.sharing

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.sharing.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareMessageOrLink(messageOrLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, messageOrLink)
        }

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            startActivity(context, shareIntent, Bundle())
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(
                context,
                context.getString(R.string.no_apps_for_text_sending),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
