package com.ivettevaldez.dependencyinjection.common.dependencyinjection

import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.viewsmvc.ViewMvcFactory
import dagger.Component

@Component(modules = [PresentationModule::class])
interface PresentationComponent {

    fun screensNavigator(): ScreensNavigator
    fun dialogsNavigator(): DialogsNavigator
    fun viewMvcFactory(): ViewMvcFactory
    fun fetchQuestionsUseCase(): FetchQuestionsUseCase
    fun fetchQuestionDetailsUseCase(): FetchQuestionDetailsUseCase
}