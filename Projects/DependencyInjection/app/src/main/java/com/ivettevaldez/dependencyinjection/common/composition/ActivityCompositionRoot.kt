package com.ivettevaldez.dependencyinjection.common.composition

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.viewsmvc.ViewMvcFactory

class ActivityCompositionRoot(
    private val activity: AppCompatActivity,
    private val appCompositionRoot: AppCompositionRoot
) {

    val screensNavigator by lazy {
        ScreensNavigator(activity)
    }

    private val layoutInflater get() = LayoutInflater.from(activity)

    val viewMvcFactory: ViewMvcFactory get() = ViewMvcFactory(layoutInflater)

    private val fragmentManager get() = activity.supportFragmentManager

    val dialogsNavigator get() = DialogsNavigator(fragmentManager)

    private val stackOverflowApi get() = appCompositionRoot.stackOverflowApi

    val fetchQuestionsUseCase get() = FetchQuestionsUseCase(stackOverflowApi)

    val fetchQuestionDetailsUseCase get() = FetchQuestionDetailsUseCase(stackOverflowApi)
}