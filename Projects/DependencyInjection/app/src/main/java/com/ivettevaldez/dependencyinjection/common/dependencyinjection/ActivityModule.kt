package com.ivettevaldez.dependencyinjection.common.dependencyinjection

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(
    private val activity: AppCompatActivity,
    private val appCompositionRoot: AppCompositionRoot
) {

    @Provides
    fun activity(): Activity = activity

    @Provides
    fun screensNavigator(activity: Activity) = ScreensNavigator(activity)

    @Provides
    fun application(): Application = appCompositionRoot.application

    @Provides
    fun layoutInflater(application: Application): LayoutInflater = LayoutInflater.from(application)

    @Provides
    fun fragmentManager(): FragmentManager = activity.supportFragmentManager

    @Provides
    fun stackOverflowApi(): StackOverflowApi = appCompositionRoot.stackOverflowApi
}