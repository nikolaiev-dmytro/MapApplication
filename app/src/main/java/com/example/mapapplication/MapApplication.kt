package com.example.mapapplication

import android.app.Application
import com.example.mapapplication.di.appModule
import com.google.android.gms.maps.MapsInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MapApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MapApplication)
            modules(listOf(appModule))
        }
    }
}