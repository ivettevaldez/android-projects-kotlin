package com.ivettevaldez.dependencyinjection.common

import android.app.Application
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.ApplicationModule

class CustomApplication : Application() {

    lateinit var applicationModule: ApplicationModule

    override fun onCreate() {
        applicationModule = ApplicationModule(this)
        super.onCreate()
    }
}