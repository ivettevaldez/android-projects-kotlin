package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator

import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.fragmentframehelper.FragmentFrameHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails.QuestionDetailsFragment
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist.QuestionsListFragment

open class ScreensNavigator(private val fragmentFrameHelper: FragmentFrameHelper) {

    fun navigateUp() {
        fragmentFrameHelper.navigateUp()
    }

    fun toQuestionsList() {
        fragmentFrameHelper.replaceFragment(
            QuestionsListFragment.newInstance()
        )
    }

    open fun toQuestionDetails(id: String) {
        fragmentFrameHelper.replaceFragment(
            QuestionDetailsFragment.newInstance(id)
        )
    }
}