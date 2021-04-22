package com.ivettevaldez.cleanarchitecture.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.infodialog.InfoViewMvcImpl
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog.PromptViewMvcImpl
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.NavDrawerViewMvcImpl
import com.ivettevaldez.cleanarchitecture.screens.common.toolbar.ToolbarViewMvcImpl
import com.ivettevaldez.cleanarchitecture.screens.questiondetails.IQuestionDetailsViewMvc
import com.ivettevaldez.cleanarchitecture.screens.questiondetails.QuestionDetailsViewMvcImpl
import com.ivettevaldez.cleanarchitecture.screens.questionslist.IQuestionsListItemViewMvc
import com.ivettevaldez.cleanarchitecture.screens.questionslist.IQuestionsListViewMvc
import com.ivettevaldez.cleanarchitecture.screens.questionslist.QuestionsListItemViewMvcImpl
import com.ivettevaldez.cleanarchitecture.screens.questionslist.QuestionsListViewMvcImpl

class ViewMvcFactory(
    private val inflater: LayoutInflater,
    private val navDrawerHelper: INavDrawerHelper
) {

    fun getToolbarViewMvc(parent: ViewGroup?): ToolbarViewMvcImpl {
        return ToolbarViewMvcImpl(inflater, parent)
    }

    fun getNavDrawerViewMvc(parent: ViewGroup?): NavDrawerViewMvcImpl {
        return NavDrawerViewMvcImpl(inflater, parent)
    }

    fun getQuestionsListViewMvc(parent: ViewGroup?): IQuestionsListViewMvc {
        return QuestionsListViewMvcImpl(inflater, parent, this, navDrawerHelper)
    }

    fun getQuestionsListItemViewMvc(parent: ViewGroup?): IQuestionsListItemViewMvc {
        return QuestionsListItemViewMvcImpl(inflater, parent)
    }

    fun getQuestionDetailsViewMvc(parent: ViewGroup?): IQuestionDetailsViewMvc {
        return QuestionDetailsViewMvcImpl(inflater, parent, this)
    }

    fun getInfoViewMvc(parent: ViewGroup?): InfoViewMvcImpl {
        return InfoViewMvcImpl(inflater, parent)
    }

    fun getPromptViewMvc(parent: ViewGroup?): PromptViewMvcImpl {
        return PromptViewMvcImpl(inflater, parent)
    }
}