package com.ivettevaldez.dependencyinjection.common.dependencyinjection.application

import android.app.Application
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import dagger.Component
import retrofit2.Retrofit

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun application(): Application
    fun retrofit(): Retrofit
    fun stackOverflowApi(): StackOverflowApi
}