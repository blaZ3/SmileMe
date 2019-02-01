package me.tellvivk.smileme

import android.app.Application
import org.koin.android.ext.android.startKoin

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, AppModule.modules)
    }

}