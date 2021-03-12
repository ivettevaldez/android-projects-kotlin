package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.Question
import kotlinx.android.synthetic.main.activity_questions_list.view.*

interface IQuestionsListViewMvc {

    interface Listener {

        fun onQuestionClicked(question: Question?)
    }

    fun getRootView(): View
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener)
    fun bindQuestions(questions: List<Question>)
}

class QuestionsListViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : IQuestionsListViewMvc,
    QuestionsListAdapter.Listener {

    private var rootView: View
    private var listQuestions: ListView
    private var questionsListAdapter: QuestionsListAdapter

    private val listeners: MutableList<IQuestionsListViewMvc.Listener> = mutableListOf()

    init {

        rootView = inflater.inflate(
            R.layout.activity_questions_list, parent, false
        )

        questionsListAdapter = QuestionsListAdapter(getContext(), this)
        listQuestions = rootView.questions_list_items
        listQuestions.adapter = questionsListAdapter
    }

    private fun getContext(): Context {
        return rootView.context
    }

    override fun getRootView(): View {
        return rootView
    }

    override fun registerListener(listener: IQuestionsListViewMvc.Listener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: IQuestionsListViewMvc.Listener) {
        listeners.remove(listener)
    }

    override fun bindQuestions(questions: List<Question>) {
        questionsListAdapter.clear()
        questionsListAdapter.addAll(questions)
        questionsListAdapter.notifyDataSetChanged()
    }

    override fun onQuestionClicked(question: Question?) {
        for (listener in listeners) {
            listener.onQuestionClicked(question)
        }
    }
}