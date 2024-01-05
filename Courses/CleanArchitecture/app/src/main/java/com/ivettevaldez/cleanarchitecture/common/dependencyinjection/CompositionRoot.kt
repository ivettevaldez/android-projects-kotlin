package com.ivettevaldez.cleanarchitecture.common.dependencyinjection

import com.ivettevaldez.cleanarchitecture.common.Constants
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsEventBus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompositionRoot {

    private var retrofit: Retrofit? = null
    private var dialogsEventBus: DialogsEventBus? = null

    fun getStackOverflowApi(): StackOverflowApi {
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

    fun getDialogsEventBus(): DialogsEventBus {
        if (dialogsEventBus == null) {
            dialogsEventBus = DialogsEventBus()
        }
        return dialogsEventBus!!
    }
}