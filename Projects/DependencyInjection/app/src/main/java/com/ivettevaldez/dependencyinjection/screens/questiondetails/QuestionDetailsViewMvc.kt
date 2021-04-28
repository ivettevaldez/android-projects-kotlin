package com.ivettevaldez.dependencyinjection.screens.questiondetails

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ivettevaldez.dependencyinjection.R
import com.ivettevaldez.dependencyinjection.questions.QuestionWithBody
import com.ivettevaldez.dependencyinjection.screens.common.toolbar.MyToolbar
import com.ivettevaldez.dependencyinjection.screens.common.viewsmvc.BaseViewMvc

interface IQuestionDetailsViewMvc {

    interface Listener {

        fun onBackClicked()
    }

    fun bindQuestion(question: QuestionWithBody)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc<IQuestionDetailsViewMvc.Listener>(
    inflater,
    parent,
    R.layout.activity_question_details
), IQuestionDetailsViewMvc {

    private val toolbar: MyToolbar = findViewById(R.id.question_details_toolbar)
    private val textBody: TextView = findViewById(R.id.question_details_text_body)
    private val swipeRefresh: SwipeRefreshLayout = findViewById(R.id.question_details_swipe_refresh)

    init {

        swipeRefresh.isEnabled = false

        setListenerEvents()
    }

    private fun setListenerEvents() {
        toolbar.setNavigateUpListener {
            for (listener in listeners) {
                listener.onBackClicked()
            }
        }
    }

    override fun bindQuestion(question: QuestionWithBody) {
        textBody.text = Html.fromHtml(question.body, Html.FROM_HTML_MODE_LEGACY)
    }

    override fun showProgressIndicator() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgressIndicator() {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
    }
}