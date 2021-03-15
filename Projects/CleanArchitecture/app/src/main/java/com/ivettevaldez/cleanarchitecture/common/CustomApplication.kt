package com.ivettevaldez.cleanarchitecture.common

import android.app.Application
import com.ivettevaldez.cleanarchitecture.common.dependencyinjection.CompositionRoot

class CustomApplication : Application() {

    private lateinit var compositionRoot: CompositionRoot

    override fun onCreate() {
        super.onCreate()
        compositionRoot = CompositionRoot()
    }

    fun getCompositionRoot(): CompositionRoot = compositionRoot
}