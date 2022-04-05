package com.jasonsb.simplebible

import android.app.Application
import timber.log.Timber


class BibleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}