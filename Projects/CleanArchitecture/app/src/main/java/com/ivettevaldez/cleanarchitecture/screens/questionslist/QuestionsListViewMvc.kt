package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    QuestionsRecyclerAdapter.Listener {

    private var rootView: View
    private var recyclerQuestions: RecyclerView

    private val listeners: MutableList<IQuestionsListViewMvc.Listener> = mutableListOf()

    private lateinit var questionsRecyclerAdapter: QuestionsRecyclerAdapter

    init {

        rootView = inflater.inflate(
            R.layout.activity_questions_list, parent, false
        )

        recyclerQuestions = rootView.questions_recycler_items

        setList()
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
        questionsRecyclerAdapter.updateData(questions)
    }

    override fun onQuestionClicked(question: Question?) {
        for (listener in listeners) {
            listener.onQuestionClicked(question)
        }
    }

    private fun getContext(): Context {
        return getRootView().context
    }

    private fun setList() {
        questionsRecyclerAdapter = QuestionsRecyclerAdapter(this)

        val linearLayoutManager = LinearLayoutManager(getContext())
        linearLayoutManager.isSmoothScrollbarEnabled = true

        with(recyclerQuestions) {
            layoutManager = linearLayoutManager
            adapter = questionsRecyclerAdapter
            addItemDecoration(
                getDividerItemDecoration(context)
            )
        }
    }

    private fun getDividerItemDecoration(context: Context): DividerItemDecoration {
        val divider = ContextCompat.getDrawable(context, R.drawable.shape_divider)
        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )

        if (divider != null) {
            dividerItemDecoration.setDrawable(divider)
        }

        return dividerItemDecoration
    }
}