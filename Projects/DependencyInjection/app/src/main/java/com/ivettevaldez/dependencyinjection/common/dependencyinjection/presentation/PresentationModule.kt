package com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.ActivityComponent
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.viewsmvc.ViewMvcFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val activityComponent: ActivityComponent) {

    @Provides
    fun layoutInflater(): LayoutInflater = activityComponent.layoutInflater()

    @Provides
    fun fragmentManager(): FragmentManager = activityComponent.fragmentManager()

    @Provides
    fun stackOverflowApi(): StackOverflowApi = activityComponent.stackOverflowApi()

    @Provides
    fun application(): Application = activityComponent.application()

    @Provides
    fun activity(): Activity = activityComponent.activity()

    @Provides
    fun screensNavigator(): ScreensNavigator = activityComponent.screensNavigator()

    @Provides
    fun dialogsNavigator(fragmentManager: FragmentManager) = DialogsNavigator(fragmentManager)

    @Provides
    fun viewMvcFactory(layoutInflater: LayoutInflater) = ViewMvcFactory(layoutInflater)

    @Provides
    fun fetchQuestionsUseCase(stackOverflowApi: StackOverflowApi) =
        FetchQuestionsUseCase(stackOverflowApi)

    @Provides
    fun fetchQuestionDetailsUseCase(stackOverflowApi: StackOverflowApi) =
        FetchQuestionDetailsUseCase(stackOverflowApi)
}