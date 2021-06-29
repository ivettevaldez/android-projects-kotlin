package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator

import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.fragmentframehelper.FragmentFrameHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist.QuestionsListFragment

class ScreensNavigator(private val fragmentFrameHelper: FragmentFrameHelper) {

    fun navigateUp() {
        fragmentFrameHelper.navigateUp()
    }

    fun toQuestionsList() {
        fragmentFrameHelper.replaceFragment(
            QuestionsListFragment.newInstance()
        )
    }

    fun toQuestionDetails(id: String) {
        // TODO:
    }
}