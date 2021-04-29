package com.ivettevaldez.dependencyinjection.common.composition

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator

class ActivityCompositionRoot(
    private val activity: AppCompatActivity,
    private val appCompositionRoot: AppCompositionRoot
) {

    val screensNavigator by lazy {
        ScreensNavigator(activity)
    }

    private val fragmentManager get() = activity.supportFragmentManager

    val dialogsNavigator get() = DialogsNavigator(fragmentManager)

    private val stackOverflowApi get() = appCompositionRoot.stackOverflowApi

    val fetchQuestionsUseCase get() = FetchQuestionsUseCase(stackOverflowApi)

    val fetchQuestionDetailsUseCase get() = FetchQuestionDetailsUseCase(stackOverflowApi)
}