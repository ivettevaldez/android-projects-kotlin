package com.ivettevaldez.saturnus.common.dependencyinjection.application

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun application(): Application = application
}