package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.unittesting.R
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.ObservableViewMvc

interface QuestionsListViewMvc : ObservableViewMvc<QuestionsListViewMvc.Listener> {

    interface Listener {

        fun onQuestionClicked(question: Question)
    }

    fun bindQuestions(questions: List<Question>)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class QuestionsListViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<QuestionsListViewMvc.Listener>(),
    QuestionsListViewMvc {

    init {

        setRootView(
            inflater.inflate(R.layout.layout_questions_list, parent, false)
        )
    }

    override fun bindQuestions(questions: List<Question>) {
        TODO("Not yet implemented")
    }

    override fun showProgressIndicator() {
        TODO("Not yet implemented")
    }

    override fun hideProgressIndicator() {
        TODO("Not yet implemented")
    }
}