package com.ivettevaldez.cleanarchitecture.screens.common.navigation

import androidx.fragment.app.FragmentManager
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IFragmentFrameWrapper
import com.ivettevaldez.cleanarchitecture.screens.questiondetails.QuestionDetailsFragment
import com.ivettevaldez.cleanarchitecture.screens.questionslist.QuestionsListFragment

class ScreenNavigator(
    private val fragmentManager: FragmentManager,
    private val fragmentFrameWrapper: IFragmentFrameWrapper
) {

    fun toQuestionDetails(questionId: String) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        val fragment = QuestionDetailsFragment.newInstance(questionId)
        fragmentTransaction.replace(fragmentFrameWrapper.getFragmentFrame().id, fragment).commit()
    }

    fun toQuestionsList() {
        // Clear back stack.
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = QuestionsListFragment.newInstance()
        fragmentTransaction.replace(fragmentFrameWrapper.getFragmentFrame().id, fragment).commit()
    }
}