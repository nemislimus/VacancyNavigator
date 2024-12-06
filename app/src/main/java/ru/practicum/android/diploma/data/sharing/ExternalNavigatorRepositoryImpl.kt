package ru.practicum.android.diploma.data.sharing

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.sharing.api.ExternalNavigatorRepository

class ExternalNavigatorRepositoryImpl(private val context: Context) : ExternalNavigatorRepository {
    override fun shareMessageOrLink(messageOrLink: String, shareDialogTitle: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, messageOrLink)
        }

        val chooser = Intent.createChooser(shareIntent, shareDialogTitle)

        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            startActivity(
                context,
                chooser,
                null
            )
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(
                context,
                context.getString(R.string.no_apps_for_text_sending),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
