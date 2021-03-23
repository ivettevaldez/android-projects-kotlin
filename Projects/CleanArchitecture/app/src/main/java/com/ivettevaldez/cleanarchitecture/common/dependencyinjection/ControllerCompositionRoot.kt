package com.ivettevaldez.cleanarchitecture.common.dependencyinjection

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.screens.common.ViewMvcFactory
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreenNavigator

class ControllerCompositionRoot(
    private val activity: AppCompatActivity,
    private val compositionRoot: CompositionRoot
) {

    private var screenNavigator: ScreenNavigator? = null

    fun getStackOverflowApi(): StackOverflowApi {
        return compositionRoot.getStackOverflowApi()
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(
            LayoutInflater.from(activity)
        )
    }

    fun getScreenNavigator(): ScreenNavigator {
        if (screenNavigator == null) {
            screenNavigator = ScreenNavigator(activity)
        }
        return screenNavigator!!
    }
}