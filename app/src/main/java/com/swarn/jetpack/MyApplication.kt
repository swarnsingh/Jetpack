package com.swarn.jetpack

import android.app.Application
import com.swarn.jetpack.di.modules.tmdbModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author Swarn Singh.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                tmdbModules
            )
        }
    }
}