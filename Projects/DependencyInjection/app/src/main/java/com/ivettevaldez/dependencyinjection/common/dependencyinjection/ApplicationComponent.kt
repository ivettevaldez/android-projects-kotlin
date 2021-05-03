package com.ivettevaldez.dependencyinjection.common.dependencyinjection

import android.app.Application
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import dagger.Component
import retrofit2.Retrofit

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun application(): Application
    fun retrofit(): Retrofit
    fun stackOverflowApi(): StackOverflowApi
}