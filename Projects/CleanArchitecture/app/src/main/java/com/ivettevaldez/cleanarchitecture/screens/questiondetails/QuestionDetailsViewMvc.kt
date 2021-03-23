package com.ivettevaldez.cleanarchitecture.screens.questiondetails

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.views.BaseViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.views.IViewMvc
import kotlinx.android.synthetic.main.activity_question_details.view.*

interface IQuestionDetailsViewMvc : IViewMvc {

    fun bindQuestion(question: Question)
    fun showProgressIndicator(show: Boolean)
}

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(),
    IQuestionDetailsViewMvc {

    private val textTitle: TextView
    private val textDescription: TextView
    private val viewProgress: ProgressBar

    private val uiHandler = Handler()

    init {

        setRootView(
            inflater.inflate(R.layout.activity_question_details, parent, false)
        )

        textTitle = getRootView().question_details_text_title
        textDescription = getRootView().question_details_text_description
        viewProgress = getRootView().question_details_progress
    }

    override fun bindQuestion(question: Question) {
        uiHandler.post {
            textTitle.text = question.title
            textDescription.text = question.body
        }
    }

    override fun showProgressIndicator(show: Boolean) {
        uiHandler.post {
            if (show) {
                viewProgress.visibility = View.VISIBLE
            } else {
                viewProgress.visibility = View.GONE
            }
        }
    }
}