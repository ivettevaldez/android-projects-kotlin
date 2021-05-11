package com.ivettevaldez.saturnus.common.dependencyinjection.activity

import android.app.Application
import android.view.LayoutInflater
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    companion object {

        @Provides
        fun layoutInflater(application: Application): LayoutInflater =
            LayoutInflater.from(application)
    }
}