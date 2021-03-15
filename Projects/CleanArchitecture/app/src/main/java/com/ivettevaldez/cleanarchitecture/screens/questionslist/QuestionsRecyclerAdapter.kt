package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.cleanarchitecture.questions.Question

class QuestionsRecyclerAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<QuestionsRecyclerAdapter.ViewHolder>(),
    IQuestionsListItemViewMvc.Listener {

    private val questions: MutableList<Question> = mutableListOf()

    interface Listener {

        fun onQuestionClicked(question: Question?)
    }

    inner class ViewHolder(val viewMvc: IQuestionsListItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.getRootView())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewMvc = QuestionsListItemViewMvcImpl(
            LayoutInflater.from(parent.context),
            parent
        )
        viewMvc.registerListener(this)
        return ViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewMvc.bindQuestion(
            questions[position]
        )
    }

    override fun getItemCount(): Int = questions.size

    override fun onQuestionClicked(question: Question?) {
        listener.onQuestionClicked(question)
    }

    fun updateData(questions: List<Question>) {
        this.questions.clear()
        this.questions.addAll(questions)
        notifyDataSetChanged()
    }
}