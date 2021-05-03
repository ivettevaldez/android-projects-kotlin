package com.ivettevaldez.dependencyinjection.common.dependencyinjection

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.viewsmvc.ViewMvcFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val activityCompositionRoot: ActivityCompositionRoot) {

    @Provides
    fun layoutInflater(): LayoutInflater = activityCompositionRoot.layoutInflater

    @Provides
    fun fragmentManager(): FragmentManager = activityCompositionRoot.fragmentManager

    @Provides
    fun stackOverflowApi(): StackOverflowApi = activityCompositionRoot.stackOverflowApi

    @Provides
    fun application(): Application = activityCompositionRoot.application

    @Provides
    fun activity(): Activity = activityCompositionRoot.activity

    @Provides
    fun screensNavigator(): ScreensNavigator = activityCompositionRoot.screensNavigator

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