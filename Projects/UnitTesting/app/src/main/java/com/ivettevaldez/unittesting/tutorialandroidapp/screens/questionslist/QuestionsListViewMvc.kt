package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.unittesting.R
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.ViewMvcFactory
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toolbar.ToolbarViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.ObservableViewMvc

interface QuestionsListViewMvc : ObservableViewMvc<QuestionsListViewMvc.Listener> {

    interface Listener {

        fun onQuestionClicked(question: Question)
    }

    fun bindQuestions(questions: List<Question>)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class QuestionsListViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val navDrawerHelper: NavDrawerHelper,
    private val viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<QuestionsListViewMvc.Listener>(),
    QuestionsListViewMvc,
    QuestionsListItemViewMvc.Listener {

    private val progressBar: ProgressBar
    private val recyclerView: RecyclerView
    private val toolbarContainer: Toolbar

    private lateinit var toolbarViewMvc: ToolbarViewMvc
    private lateinit var questionsRecyclerAdapter: QuestionsRecyclerAdapter

    init {

        setRootView(
            inflater.inflate(R.layout.layout_questions_list, parent, false)
        )

        progressBar = getRootView()!!.findViewById(R.id.questions_list_progress)
        recyclerView = getRootView()!!.findViewById(R.id.questions_list_recycler)
        toolbarContainer = getRootView()!!.findViewById(R.id.toolbar)

        initToolbar()
        initRecycler()
    }

    override fun bindQuestions(questions: List<Question>) {
        questionsRecyclerAdapter.bindData(questions)
    }

    override fun showProgressIndicator() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        progressBar.visibility = View.GONE
    }

    override fun onQuestionClicked(question: Question) {
        for (listener in getListeners()) {
            listener.onQuestionClicked(question)
        }
    }

    private fun initToolbar() {
        toolbarViewMvc = viewMvcFactory.getToolbarViewMvc(toolbarContainer)
        toolbarViewMvc.setTitle(getContext().getString(R.string.last_active_questions))
        toolbarViewMvc.enableMenuAndListen(object : ToolbarViewMvc.MenuListener {
            override fun onMenuClicked() {
                navDrawerHelper.openDrawer()
            }
        })

        toolbarContainer.addView(toolbarViewMvc.getRootView())
    }

    private fun initRecycler() {
        questionsRecyclerAdapter = QuestionsRecyclerAdapter(this, viewMvcFactory)

        recyclerView.layoutManager = LinearLayoutManager(getContext())
        recyclerView.adapter = questionsRecyclerAdapter
    }
}