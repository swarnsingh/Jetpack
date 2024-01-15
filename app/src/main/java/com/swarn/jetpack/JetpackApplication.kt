package com.swarn.jetpack

import android.app.Application
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class JetpackApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            loadKoinModules(jetpackModules)
        }
    }
}