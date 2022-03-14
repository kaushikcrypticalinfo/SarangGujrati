package com.example.saranggujrati

import android.app.Application
import android.content.Context
import com.github.muhrifqii.parserss.ParseRSS
import org.xmlpull.v1.XmlPullParserFactory

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
        ParseRSS.init(XmlPullParserFactory.newInstance())
        appContext = applicationContext
        if (BuildConfig.DEBUG) {
            timber.log.Timber.plant(timber.log.Timber.DebugTree())
        }
    }
}