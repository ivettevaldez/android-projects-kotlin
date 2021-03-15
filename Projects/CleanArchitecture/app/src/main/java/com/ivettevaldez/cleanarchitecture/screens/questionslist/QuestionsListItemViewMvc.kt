package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.IObservableViewMvc
import kotlinx.android.synthetic.main.questions_list_item.view.*

interface IQuestionsListItemViewMvc : IObservableViewMvc<IQuestionsListItemViewMvc.Listener> {

    interface Listener {

        fun onQuestionClicked(question: Question?)
    }

    fun bindQuestion(question: Question?)
}

class QuestionsListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : IQuestionsListItemViewMvc {

    private var rootView: View
    private var textTitle: TextView

    private val listeners: MutableList<IQuestionsListItemViewMvc.Listener> = mutableListOf()

    private var question: Question? = null

    init {

        rootView = inflater.inflate(
            R.layout.questions_list_item,
            parent,
            false
        )

        textTitle = rootView.questions_list_item_text_title

        setListenerEvents()
    }

    override fun getRootView(): View {
        return rootView
    }

    override fun registerListener(listener: IQuestionsListItemViewMvc.Listener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: IQuestionsListItemViewMvc.Listener) {
        listeners.remove(listener)
    }

    override fun bindQuestion(question: Question?) {
        this.question = question
        textTitle.text = question?.title ?: ""
    }

    private fun setListenerEvents() {
        rootView.setOnClickListener {
            for (listener in listeners) {
                listener.onQuestionClicked(question)
            }
        }
    }
}