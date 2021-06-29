package com.ivettevaldez.unittesting.tutorialandroidapp.common.dependencyinjection

import com.ivettevaldez.unittesting.tutorialandroidapp.common.Constants
import com.ivettevaldez.unittesting.tutorialandroidapp.common.time.TimeProvider
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.StackOverflowApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompositionRoot {

    private var retrofit: Retrofit? = null

    fun getTimeProvider(): TimeProvider = TimeProvider()

    fun getStackoverflowApi(): StackOverflowApi {
        return getRetrofit().create(StackOverflowApi::class.java)
    }

    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}