package com.ivettevaldez.cleanarchitecture.screens.questiondetails

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.ViewMvcFactory
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.BaseNavDrawerViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.DrawerItems
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.views.IObservableViewMvc
import kotlinx.android.synthetic.main.activity_question_details.view.*
import kotlinx.android.synthetic.main.element_toolbar.view.*

interface IQuestionDetailsViewMvc : IObservableViewMvc<IQuestionDetailsViewMvc.Listener>,
    INavDrawerViewMvc {

    interface Listener {

        fun onNavigateUpClicked()
        fun onQuestionsListClicked()
    }

    fun bindQuestion(question: Question)
    fun showProgressIndicator(show: Boolean)
}

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseNavDrawerViewMvc<IQuestionDetailsViewMvc.Listener>(inflater, parent),
    IQuestionDetailsViewMvc {

    private val textTitle: TextView
    private val textDescription: TextView
    private val viewProgress: ProgressBar
    private val toolbar: Toolbar

    private val toolbarViewMvc: IToolbarViewMvc
    private val uiHandler = Handler()

    init {

        setRootView(
            inflater.inflate(R.layout.activity_question_details, parent, false)
        )

        textTitle = getRootView().question_details_text_title
        textDescription = getRootView().question_details_text_description
        viewProgress = getRootView().question_details_progress
        toolbar = getRootView().toolbar
        toolbarViewMvc = viewMvcFactory.getToolbarViewMvc(toolbar)

        initToolbar()
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

    override fun onDrawerItemClicked(item: DrawerItems) {
        for (listener in getListeners()) {
            when (item) {
                DrawerItems.QUESTIONS_LIST -> listener.onQuestionsListClicked()
            }
        }
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(
            getContext().getString(R.string.question_details_title)
        )
        toolbarViewMvc.enableUpNavigationAndListen(
            object : IToolbarViewMvc.NavigateUpClickListener {
                override fun onNavigateUpClicked() {
                    for (listener in getListeners()) {
                        listener.onNavigateUpClicked()
                    }
                }
            }
        )

        toolbar.addView(toolbarViewMvc.getRootView())
    }
}