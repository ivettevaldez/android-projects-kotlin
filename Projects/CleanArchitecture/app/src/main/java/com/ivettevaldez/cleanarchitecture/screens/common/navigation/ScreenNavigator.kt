package com.ivettevaldez.cleanarchitecture.screens.common.navigation

import android.app.Activity
import android.content.Intent
import com.ivettevaldez.cleanarchitecture.screens.questiondetails.QuestionDetailsActivity

class ScreenNavigator(private val activity: Activity) {

    companion object {

        const val EXTRA_QUESTION_ID = "com.ivettevaldez.cleanarchitecture.EXTRA_QUESTION_ID"
    }

    fun toQuestionDetails(questionId: String) {
        val intent = Intent(activity, QuestionDetailsActivity::class.java)
        intent.putExtra(EXTRA_QUESTION_ID, questionId)
        activity.startActivity(intent)
    }
}