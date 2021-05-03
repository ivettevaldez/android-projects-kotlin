package com.ivettevaldez.dependencyinjection.common.dependencyinjection

import android.app.Application
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator

class ActivityCompositionRoot(
    val activity: AppCompatActivity,
    private val appCompositionRoot: AppCompositionRoot
) {

    val screensNavigator by lazy {
        ScreensNavigator(activity)
    }

    val application: Application get() = appCompositionRoot.application

    val layoutInflater: LayoutInflater get() = LayoutInflater.from(application)

    val fragmentManager: FragmentManager get() = activity.supportFragmentManager

    val stackOverflowApi: StackOverflowApi get() = appCompositionRoot.stackOverflowApi
}