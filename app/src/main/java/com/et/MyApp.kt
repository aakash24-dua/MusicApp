package com.et

import android.app.Application
import com.et.music.di.preferencesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(preferencesModule))
        }
    }
}