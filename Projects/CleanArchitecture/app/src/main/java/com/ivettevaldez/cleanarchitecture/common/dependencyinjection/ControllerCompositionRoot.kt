package com.ivettevaldez.cleanarchitecture.common.dependencyinjection

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.screens.common.ViewMvcFactory

class ControllerCompositionRoot(
    private val activity: AppCompatActivity,
    private val compositionRoot: CompositionRoot
) {

    fun getStackOverflowApi(): StackOverflowApi {
        return compositionRoot.getStackOverflowApi()
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(
            LayoutInflater.from(activity)
        )
    }
}