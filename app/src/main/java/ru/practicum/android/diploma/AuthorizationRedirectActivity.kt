package ru.practicum.android.diploma

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AuthorizationRedirectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_authorization_redirect)

        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data

            uri?.let {
                findViewById<TextView>(R.id.tvKey).text = it.getQueryParameter("key")
            }
        }
    }
}
