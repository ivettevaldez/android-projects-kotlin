package com.ivettevaldez.dependencyinjection.common

import android.app.Application
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationModule
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.DaggerApplicationComponent

class CustomApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        super.onCreate()
    }
}