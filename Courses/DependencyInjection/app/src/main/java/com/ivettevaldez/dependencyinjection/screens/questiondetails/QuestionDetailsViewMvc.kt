package com.ivettevaldez.dependencyinjection.screens.questiondetails

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ivettevaldez.dependencyinjection.R
import com.ivettevaldez.dependencyinjection.questions.QuestionWithBody
import com.ivettevaldez.dependencyinjection.screens.common.images.ImageLoader
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
    parent: ViewGroup?,
    private val imageLoader: ImageLoader
) : BaseViewMvc<IQuestionDetailsViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_question_details
), IQuestionDetailsViewMvc {

    private val toolbar: MyToolbar = findViewById(R.id.question_details_toolbar)
    private val textBody: TextView = findViewById(R.id.question_details_text_body)
    private val textUserName: TextView = findViewById(R.id.question_details_text_user_name)
    private val imageUserPicture: ImageView = findViewById(R.id.question_details_image_user)
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
        textUserName.text = question.owner.name

        imageLoader.load(question.owner.imageUrl, imageUserPicture)
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