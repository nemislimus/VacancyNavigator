package com.example.playlistmaker.di

import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<FirebaseAnalytics> {
        FirebaseAnalytics.getInstance(androidContext())
    }
}
