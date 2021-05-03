package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import dagger.Component

@Component(modules = [ActivityModule::class])
interface ActivityComponent {

    fun activity(): Activity
    fun screensNavigator(): ScreensNavigator
    fun application(): Application
    fun layoutInflater(): LayoutInflater
    fun fragmentManager(): FragmentManager
    fun stackOverflowApi(): StackOverflowApi
}