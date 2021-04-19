package com.ivettevaldez.cleanarchitecture.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.cleanarchitecture.screens.questiondetails.IQuestionDetailsViewMvc
import com.ivettevaldez.cleanarchitecture.screens.questiondetails.QuestionDetailsViewMvcImpl
import com.ivettevaldez.cleanarchitecture.screens.questionslist.IQuestionsListItemViewMvc
import com.ivettevaldez.cleanarchitecture.screens.questionslist.IQuestionsListViewMvc
import com.ivettevaldez.cleanarchitecture.screens.questionslist.QuestionsListItemViewMvcImpl
import com.ivettevaldez.cleanarchitecture.screens.questionslist.QuestionsListViewMvcImpl

class ViewMvcFactory(private val inflater: LayoutInflater) {

    fun getQuestionsListViewMvc(parent: ViewGroup?): IQuestionsListViewMvc {
        return QuestionsListViewMvcImpl(inflater, parent, this)
    }

    fun getQuestionsListItemViewMvc(parent: ViewGroup?): IQuestionsListItemViewMvc {
        return QuestionsListItemViewMvcImpl(inflater, parent)
    }

    fun getQuestionDetailsViewMvc(parent: ViewGroup?): IQuestionDetailsViewMvc {
        return QuestionDetailsViewMvcImpl(inflater, parent)
    }

    fun getToolbarViewMvc(parent: ViewGroup?): ToolbarViewMvc {
        return ToolbarViewMvc(inflater, parent)
    }
}