package com.ivettevaldez.dependencyinjection.screens.common.navigation

import android.app.Activity
import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsActivity

class ScreensNavigator(private val activity: Activity) {

    fun navigateBack() {
        activity.onBackPressed()
    }

    fun toQuestionDetails(questionId: String) {
        QuestionDetailsActivity.start(activity, questionId)
    }
}