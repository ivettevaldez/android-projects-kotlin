package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import android.app.Application
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.screens.common.navigation.IScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ActivityModule {

    @ActivityScope
    @Binds
    abstract fun screensNavigator(screensNavigator: ScreensNavigatorImpl): IScreensNavigator

    companion object {

        @Provides
        fun layoutInflater(application: Application): LayoutInflater =
            LayoutInflater.from(application)

        @Provides
        fun fragmentManager(activity: AppCompatActivity): FragmentManager =
            activity.supportFragmentManager
    }
}