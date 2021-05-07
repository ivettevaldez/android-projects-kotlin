package com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation

import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsActivity
import com.ivettevaldez.dependencyinjection.screens.questionslist.QuestionsListFragment
import com.ivettevaldez.dependencyinjection.screens.viewmodel.ViewModelActivity
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [ViewModelsModule::class])
interface PresentationComponent {

    fun inject(fragment: QuestionsListFragment)
    fun inject(activity: QuestionDetailsActivity)
    fun inject(activity: ViewModelActivity)
}