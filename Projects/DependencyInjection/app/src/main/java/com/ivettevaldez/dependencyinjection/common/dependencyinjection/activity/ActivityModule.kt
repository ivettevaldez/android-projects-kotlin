package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun activity(): Activity = activity

    @Provides
    @ActivityScope
    fun screensNavigator(activity: Activity) = ScreensNavigator(activity)

    @Provides
    fun layoutInflater(application: Application): LayoutInflater = LayoutInflater.from(application)

    @Provides
    fun fragmentManager(): FragmentManager = activity.supportFragmentManager
}