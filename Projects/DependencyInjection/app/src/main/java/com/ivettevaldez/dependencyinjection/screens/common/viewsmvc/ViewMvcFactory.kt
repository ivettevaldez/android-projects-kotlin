package com.ivettevaldez.dependencyinjection.screens.common.viewsmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsViewMvcImpl
import com.ivettevaldez.dependencyinjection.screens.questionslist.QuestionsListViewMvcImpl
import javax.inject.Inject

class ViewMvcFactory @Inject constructor(private val inflater: LayoutInflater) {

    fun newQuestionsListViewMvc(parent: ViewGroup?): QuestionsListViewMvcImpl {
        return QuestionsListViewMvcImpl(inflater, parent)
    }

    fun newQuestionDetailsViewMvc(parent: ViewGroup?): QuestionDetailsViewMvcImpl {
        return QuestionDetailsViewMvcImpl(inflater, parent)
    }
}