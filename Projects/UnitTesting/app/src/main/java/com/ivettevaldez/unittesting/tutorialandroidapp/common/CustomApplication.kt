package com.ivettevaldez.unittesting.tutorialandroidapp.common

import android.app.Application
import com.ivettevaldez.unittesting.tutorialandroidapp.common.dependencyinjection.CompositionRoot

class CustomApplication : Application() {

    lateinit var compositionRoot: CompositionRoot

    override fun onCreate() {
        super.onCreate()
        compositionRoot = CompositionRoot()
    }
}