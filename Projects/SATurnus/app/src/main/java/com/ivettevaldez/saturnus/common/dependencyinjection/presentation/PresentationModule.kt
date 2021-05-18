package com.ivettevaldez.saturnus.common.dependencyinjection.presentation

import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    companion object {

        @Provides
        fun uiHandler(): Handler = Handler(Looper.getMainLooper())
    }
}