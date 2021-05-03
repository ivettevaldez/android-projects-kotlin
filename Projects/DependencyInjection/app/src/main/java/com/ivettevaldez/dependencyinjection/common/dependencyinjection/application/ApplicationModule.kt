package com.ivettevaldez.dependencyinjection.common.dependencyinjection.application

import android.app.Application
import com.ivettevaldez.dependencyinjection.common.Constants
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun application(): Application = application

    @Provides
    @ApplicationScope
    fun retrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @ApplicationScope
    fun stackOverflowApi(retrofit: Retrofit): StackOverflowApi =
        retrofit.create(StackOverflowApi::class.java)
}