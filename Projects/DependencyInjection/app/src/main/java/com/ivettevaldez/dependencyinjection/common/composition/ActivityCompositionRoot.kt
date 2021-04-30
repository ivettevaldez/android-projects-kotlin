package com.ivettevaldez.dependencyinjection.common.composition

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator

class ActivityCompositionRoot(
    private val activity: AppCompatActivity,
    private val appCompositionRoot: AppCompositionRoot
) {

    val screensNavigator by lazy {
        ScreensNavigator(activity)
    }

    val layoutInflater: LayoutInflater get() = LayoutInflater.from(activity)

    val fragmentManager: FragmentManager get() = activity.supportFragmentManager

    val stackOverflowApi: StackOverflowApi get() = appCompositionRoot.stackOverflowApi
}