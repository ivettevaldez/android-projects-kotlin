package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ivettevaldez.unittesting.R
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

    private val textTitle: TextView

    private lateinit var question: Question

    init {

        setRootView(
            inflater.inflate(R.layout.item_question, parent, false)
        )

        textTitle = getRootView()!!.findViewById(R.id.question_text_title)

        setListenerEvents()
    }

    override fun bindQuestion(question: Question) {
        this.question = question
        textTitle.text = question.title
    }

    private fun setListenerEvents() {
        getRootView()!!.setOnClickListener {
            for (listener in getListeners()) {
                listener.onQuestionClicked(question)
            }
        }
    }
}