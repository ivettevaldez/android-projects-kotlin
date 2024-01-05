package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.views.IObservableViewMvc
import kotlinx.android.synthetic.main.item_questions_list.view.*

interface IQuestionsListItemViewMvc : IObservableViewMvc<IQuestionsListItemViewMvc.Listener> {

    interface Listener {

        fun onQuestionClicked(question: Question?)
    }

    fun bindQuestion(question: Question?)
}

class QuestionsListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IQuestionsListItemViewMvc.Listener>(),
    IQuestionsListItemViewMvc {

    private var textTitle: TextView

    private var question: Question? = null

    init {

        setRootView(
            inflater.inflate(R.layout.item_questions_list, parent, false)
        )

        textTitle = getRootView().questions_list_item_text_title

        setListenerEvents()
    }

    override fun bindQuestion(question: Question?) {
        this.question = question
        textTitle.text = question?.title ?: ""
    }

    private fun setListenerEvents() {
        getRootView().setOnClickListener {
            for (listener in getListeners()) {
                listener.onQuestionClicked(question)
            }
        }
    }
}