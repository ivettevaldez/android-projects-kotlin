package com.ivettevaldez.cleanarchitecture.common.dependencyinjection

import com.ivettevaldez.cleanarchitecture.common.Constants
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompositionRoot {

    private var retrofit: Retrofit? = null

    fun getStackOverflowApi(): StackOverflowApi {
        return getRetrofit().create(StackOverflowApi::class.java)
    }

    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}