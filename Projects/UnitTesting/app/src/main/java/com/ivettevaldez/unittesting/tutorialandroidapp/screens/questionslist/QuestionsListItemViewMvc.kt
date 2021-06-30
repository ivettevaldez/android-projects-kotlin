package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.ObservableViewMvc

interface QuestionsListItemViewMvc : ObservableViewMvc<QuestionsListItemViewMvc.Listener> {

    interface Listener {

        fun onQuestionClicked(question: Question)
    }

    fun bindQuestion(question: Question)
}

class QuestionsListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<QuestionsListItemViewMvc.Listener>(),
    QuestionsListItemViewMvc {

    init {

    }

    override fun bindQuestion(question: Question) {
        TODO("Not yet implemented")
    }
}