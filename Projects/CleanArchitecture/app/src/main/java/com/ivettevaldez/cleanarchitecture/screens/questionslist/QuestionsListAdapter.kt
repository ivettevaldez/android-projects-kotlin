package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ivettevaldez.cleanarchitecture.questions.Question

class QuestionsListAdapter(
    context: Context,
    private val listener: Listener
) : ArrayAdapter<Question>(context, 0),
    IQuestionsListItemViewMvc.Listener {

    interface Listener {

        fun onQuestionClicked(question: Question?)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView == null) {
            val viewMvc = QuestionsListItemViewMvcImpl(
                LayoutInflater.from(parent.context),
                parent
            )
            viewMvc.registerListener(this)

            view = viewMvc.getRootView()
            view.tag = viewMvc
        } else {
            view = convertView
        }

        val question = getItem(position)
        val viewMvc = view.tag as QuestionsListItemViewMvcImpl
        viewMvc.bindQuestion(question)

        view.setOnClickListener { onQuestionClicked(question) }

        return view
    }

    override fun onQuestionClicked(question: Question?) {
        listener.onQuestionClicked(question)
    }
}