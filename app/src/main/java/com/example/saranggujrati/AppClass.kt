package com.example.saranggujrati

import android.app.Application
import android.content.Context

class AppClass:Application() {

    companion object {
        lateinit var appContext: Context
        lateinit var instance: AppClass
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        if (BuildConfig.DEBUG) {
            timber.log.Timber.plant(timber.log.Timber.DebugTree())
        }
    }
}