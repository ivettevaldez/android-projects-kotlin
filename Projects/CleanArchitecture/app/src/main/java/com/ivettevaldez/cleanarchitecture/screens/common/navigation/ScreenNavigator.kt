package com.ivettevaldez.cleanarchitecture.screens.common.navigation

import com.ivettevaldez.cleanarchitecture.screens.common.fragmentframehelper.FragmentFrameHelper
import com.ivettevaldez.cleanarchitecture.screens.questiondetails.QuestionDetailsFragment
import com.ivettevaldez.cleanarchitecture.screens.questionslist.QuestionsListFragment

class ScreenNavigator(
    private val fragmentFrameHelper: FragmentFrameHelper
) {

    fun toQuestionDetails(questionId: String) {
        val fragment = QuestionDetailsFragment.newInstance(questionId)
        fragmentFrameHelper.replaceFragment(fragment)
    }

    fun toQuestionsList() {
        val fragment = QuestionsListFragment.newInstance()
        fragmentFrameHelper.replaceFragmentAndClearBackstack(fragment)
    }

    fun navigateUp() {
        fragmentFrameHelper.navigateUp()
    }
}