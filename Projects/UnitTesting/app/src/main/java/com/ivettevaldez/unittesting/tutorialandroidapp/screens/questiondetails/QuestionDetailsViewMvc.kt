package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.unittesting.R
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.QuestionDetails
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.ObservableViewMvc

interface QuestionDetailsViewMvc : ObservableViewMvc<QuestionDetailsViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
    }

    fun showProgressIndicator()
    fun hideProgressIndicator()
    fun bindQuestionDetails(questionDetails: QuestionDetails)
}

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<QuestionDetailsViewMvc.Listener>(),
    QuestionDetailsViewMvc {

    init {

        setRootView(
            inflater.inflate(R.layout.layout_question_details, parent, false)
        )
    }

    override fun showProgressIndicator() {
        TODO("Not yet implemented")
    }

    override fun hideProgressIndicator() {
        TODO("Not yet implemented")
    }

    override fun bindQuestionDetails(questionDetails: QuestionDetails) {
        TODO("Not yet implemented")
    }
}