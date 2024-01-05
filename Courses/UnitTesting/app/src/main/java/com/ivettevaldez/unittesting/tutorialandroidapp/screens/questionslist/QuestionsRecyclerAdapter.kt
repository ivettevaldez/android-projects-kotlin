package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.ViewMvcFactory

class QuestionsRecyclerAdapter(
    private val listener: QuestionsListItemViewMvc.Listener,
    private val viewMvcFactory: ViewMvcFactory
) : RecyclerView.Adapter<QuestionsRecyclerAdapter.ViewHolder>(),
    QuestionsListItemViewMvc.Listener {

    private var questions: MutableList<Question> = mutableListOf()

    inner class ViewHolder(val viewMvc: QuestionsListItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.getRootView()!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewMvc = viewMvcFactory.getQuestionsListItemViewMvc(parent)
        viewMvc.registerListener(this)
        return ViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewMvc.bindQuestion(
            questions[position]
        )
    }

    override fun getItemCount(): Int = questions.size

    override fun onQuestionClicked(question: Question) {
        listener.onQuestionClicked(question)
    }

    fun bindData(questions: List<Question>) {
        this.questions.clear()
        this.questions.addAll(questions)
        notifyDataSetChanged()
    }
}