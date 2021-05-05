package com.ivettevaldez.dependencyinjection.screens.common.navigation

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsActivity

class ScreensNavigator(private val activity: AppCompatActivity) {

    fun navigateBack() {
        activity.onBackPressed()
    }

    fun toQuestionDetails(questionId: String) {
        QuestionDetailsActivity.start(activity, questionId)
    }
}