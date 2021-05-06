package com.ivettevaldez.dependencyinjection.common

import android.app.Application
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationModule
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.DaggerApplicationComponent

class CustomApplication : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}