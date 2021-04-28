package com.ivettevaldez.dependencyinjection.screens.questiondetails

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ivettevaldez.dependencyinjection.R
import com.ivettevaldez.dependencyinjection.questions.QuestionWithBody
import com.ivettevaldez.dependencyinjection.screens.common.toolbar.MyToolbar

interface IQuestionDetailsViewMvc {

    interface Listener {

        fun onNavigateUpClicked()
    }

    fun bindQuestion(question: QuestionWithBody)
    fun showProgressIndicator()
    fun hideProgressIndicator()
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener)
}

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : IQuestionDetailsViewMvc {

    val rootView: View = inflater.inflate(
        R.layout.activity_question_details, parent, false
    )

    private val context: Context get() = rootView.context
    private val toolbar: MyToolbar
    private val textBody: TextView
    private val swipeRefresh: SwipeRefreshLayout

    private val listeners: MutableSet<IQuestionDetailsViewMvc.Listener> = HashSet()

    init {

        toolbar = findViewById(R.id.question_details_toolbar)
        textBody = findViewById(R.id.question_details_text_body)

        swipeRefresh = findViewById(R.id.question_details_swipe_refresh)
        swipeRefresh.isEnabled = false

        setListenerEvents()
    }

    private fun <T : View?> findViewById(@IdRes id: Int): T {
        return rootView.findViewById<T>(id)
    }

    private fun setListenerEvents() {
        toolbar.setNavigateUpListener {
            for (listener in listeners) {
                listener.onNavigateUpClicked()
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

    override fun registerListener(listener: IQuestionDetailsViewMvc.Listener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: IQuestionDetailsViewMvc.Listener) {
        listeners.remove(listener)
    }
}