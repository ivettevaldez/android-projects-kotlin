package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import android.app.Activity
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun activity(): Activity
    fun screensNavigator(): ScreensNavigator
    fun layoutInflater(): LayoutInflater
    fun fragmentManager(): FragmentManager
    fun stackOverflowApi(): StackOverflowApi
}