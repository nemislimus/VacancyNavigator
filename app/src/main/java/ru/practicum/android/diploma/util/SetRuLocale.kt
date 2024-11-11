package ru.practicum.android.diploma.util

import android.app.Activity
import android.content.res.Configuration
import android.os.LocaleList
import java.util.Locale

interface SetRuLocale {
    fun setRuLocale(activity: Activity) {
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val config: Configuration = activity.baseContext.resources.configuration
        val list = LocaleList(locale)
        config.setLocales(list)
        activity.createConfigurationContext(config)
    }
}
