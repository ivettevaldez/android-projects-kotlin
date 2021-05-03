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

class PresentationCompositionRoot(private val activityCompositionRoot: ActivityCompositionRoot) {

    private val layoutInflater: LayoutInflater get() = activityCompositionRoot.layoutInflater

    private val fragmentManager: FragmentManager get() = activityCompositionRoot.fragmentManager

    private val stackOverflowApi: StackOverflowApi get() = activityCompositionRoot.stackOverflowApi

    val application: Application get() = activityCompositionRoot.application

    val activity: Activity get() = activityCompositionRoot.activity

    val screensNavigator: ScreensNavigator get() = activityCompositionRoot.screensNavigator

    val dialogsNavigator get() = DialogsNavigator(fragmentManager)

    val viewMvcFactory get() = ViewMvcFactory(layoutInflater)

    val fetchQuestionsUseCase get() = FetchQuestionsUseCase(stackOverflowApi)

    val fetchQuestionDetailsUseCase get() = FetchQuestionDetailsUseCase(stackOverflowApi)
}