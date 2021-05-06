package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import android.app.Application
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import dagger.Module
import dagger.Provides

@Module
object ActivityModule {
    
    @Provides
    @ActivityScope
    fun screensNavigator(activity: AppCompatActivity) = ScreensNavigator(activity)

    @Provides
    fun layoutInflater(application: Application): LayoutInflater =
        LayoutInflater.from(application)

    @Provides
    fun fragmentManager(activity: AppCompatActivity): FragmentManager =
        activity.supportFragmentManager
}