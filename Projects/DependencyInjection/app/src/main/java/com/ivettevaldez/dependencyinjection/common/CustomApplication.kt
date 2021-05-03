package com.ivettevaldez.dependencyinjection.common

import android.app.Application
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.AppCompositionRoot

class CustomApplication : Application() {

    lateinit var appCompositionRoot: AppCompositionRoot

    override fun onCreate() {
        appCompositionRoot = AppCompositionRoot(this)
        super.onCreate()
    }
}