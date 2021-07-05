package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.ivettevaldez.unittesting.R
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.QuestionDetails
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.ViewMvcFactory
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toolbar.ToolbarViewMvc
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
    parent: ViewGroup?,
    private val viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<QuestionDetailsViewMvc.Listener>(),
    QuestionDetailsViewMvc {

    private val progressBar: ProgressBar
    private val textTitle: TextView
    private val textBody: TextView
    private val toolbarContainer: Toolbar

    private lateinit var toolbarViewMvc: ToolbarViewMvc

    init {

        setRootView(
            inflater.inflate(R.layout.layout_question_details, parent, false)
        )

        progressBar = getRootView()!!.findViewById(R.id.question_details_progress)
        textTitle = getRootView()!!.findViewById(R.id.question_details_text_title)
        textBody = getRootView()!!.findViewById(R.id.question_details_text_body)
        toolbarContainer = getRootView()!!.findViewById(R.id.toolbar)

        initToolbar()
    }

    override fun showProgressIndicator() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        progressBar.visibility = View.GONE
    }

    override fun bindQuestionDetails(questionDetails: QuestionDetails) {
        textTitle.text = questionDetails.title
        textBody.text = questionDetails.body
    }

    private fun initToolbar() {
        toolbarViewMvc = viewMvcFactory.getToolbarViewMvc(toolbarContainer)
        toolbarViewMvc.setTitle(getContext().getString(R.string.question_details))
        toolbarViewMvc.enableNavigateUpAndListen(object : ToolbarViewMvc.NavigateUpListener {
            override fun onNavigateUpClicked() {
                for (listener in getListeners()) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        toolbarContainer.addView(toolbarViewMvc.getRootView())
    }
}