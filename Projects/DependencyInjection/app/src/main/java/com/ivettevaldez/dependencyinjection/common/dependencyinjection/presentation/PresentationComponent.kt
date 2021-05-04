package com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation

import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsActivity
import com.ivettevaldez.dependencyinjection.screens.questionslist.QuestionsListFragment
import dagger.Component

@Component(modules = [PresentationModule::class])
interface PresentationComponent {

    fun inject(fragment: QuestionsListFragment)
    fun inject(activity: QuestionDetailsActivity)
}