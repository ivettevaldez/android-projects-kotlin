package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.screens.common.navigation.IScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModule {

    @ActivityScoped
    @Binds
    abstract fun screensNavigator(screensNavigator: ScreensNavigatorImpl): IScreensNavigator

    companion object {

        @Provides
        fun appCompatActivity(activity: Activity): AppCompatActivity = activity as AppCompatActivity

        @Provides
        fun layoutInflater(application: Application): LayoutInflater =
            LayoutInflater.from(application)

        @Provides
        fun fragmentManager(appCompatActivity: AppCompatActivity): FragmentManager =
            appCompatActivity.supportFragmentManager
    }
}