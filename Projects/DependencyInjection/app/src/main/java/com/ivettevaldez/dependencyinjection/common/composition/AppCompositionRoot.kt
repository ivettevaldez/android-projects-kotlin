package com.ivettevaldez.dependencyinjection.common.composition

import androidx.annotation.UiThread
import com.ivettevaldez.dependencyinjection.common.Constants
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@UiThread
class AppCompositionRoot {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val stackOverflowApi: StackOverflowApi by lazy {
        retrofit.create(StackOverflowApi::class.java)
    }
}