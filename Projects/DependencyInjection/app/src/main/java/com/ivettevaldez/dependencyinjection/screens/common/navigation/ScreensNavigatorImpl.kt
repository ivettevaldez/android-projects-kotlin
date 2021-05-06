package com.ivettevaldez.dependencyinjection.screens.common.navigation

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsActivity
import javax.inject.Inject

interface IScreensNavigator {

    fun navigateBack()
    fun toQuestionDetails(questionId: String)
}

class ScreensNavigatorImpl @Inject constructor(
    private val activity: AppCompatActivity
) : IScreensNavigator {

    override fun navigateBack() {
        activity.onBackPressed()
    }

    override fun toQuestionDetails(questionId: String) {
        QuestionDetailsActivity.start(activity, questionId)
    }
}