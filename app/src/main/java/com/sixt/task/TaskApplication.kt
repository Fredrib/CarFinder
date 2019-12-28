package com.sixt.task

import android.app.Application
import com.sixt.task.network.di.NetworkModule
import com.sixt.task.viewmodel.di.CarModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TaskApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TaskApplication)
            modules(
                listOf(
                    NetworkModule.instance,
                    CarModule.instance
                )
            )
        }
    }
}