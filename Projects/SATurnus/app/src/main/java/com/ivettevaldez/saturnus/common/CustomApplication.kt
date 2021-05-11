package com.ivettevaldez.saturnus.common

import android.app.Application
import com.ivettevaldez.saturnus.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.application.ApplicationModule
import com.ivettevaldez.saturnus.common.dependencyinjection.application.DaggerApplicationComponent

class CustomApplication : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}