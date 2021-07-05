package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerViewMvcImpl
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toolbar.ToolbarViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toolbar.ToolbarViewMvcImpl
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails.QuestionDetailsViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails.QuestionDetailsViewMvcImpl
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist.QuestionsListItemViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist.QuestionsListItemViewMvcImpl
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist.QuestionsListViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist.QuestionsListViewMvcImpl

class ViewMvcFactory(
    private val inflater: LayoutInflater,
    private val navDrawerHelper: NavDrawerHelper
) {

    fun getNavDrawerViewMvc(parent: ViewGroup?): NavDrawerViewMvc {
        return NavDrawerViewMvcImpl(inflater, parent)
    }

    fun getToolbarViewMvc(parent: ViewGroup?): ToolbarViewMvc {
        return ToolbarViewMvcImpl(inflater, parent)
    }

    fun getQuestionsListViewMvc(parent: ViewGroup?): QuestionsListViewMvc {
        return QuestionsListViewMvcImpl(inflater, parent, navDrawerHelper, this)
    }

    fun getQuestionsListItemViewMvc(parent: ViewGroup?): QuestionsListItemViewMvc {
        return QuestionsListItemViewMvcImpl(inflater, parent)
    }

    fun getQuestionDetailsViewMvc(parent: ViewGroup?): QuestionDetailsViewMvc {
        return QuestionDetailsViewMvcImpl(inflater, parent, this)
    }
}