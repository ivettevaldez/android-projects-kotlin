package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.ViewMvcFactory
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.BaseNavDrawerViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.DrawerItems
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.views.IObservableViewMvc
import kotlinx.android.synthetic.main.element_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_questions_list.view.*

interface IQuestionsListViewMvc : IObservableViewMvc<IQuestionsListViewMvc.Listener>,
    INavDrawerViewMvc {

    interface Listener {

        fun onQuestionClicked(question: Question?)
        fun onQuestionsListClicked()
    }

    fun bindQuestions(questions: List<Question>)
    fun showProgressIndicator(show: Boolean)
}

class QuestionsListViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val viewMvcFactory: ViewMvcFactory
) : BaseNavDrawerViewMvc<IQuestionsListViewMvc.Listener>(inflater, parent),
    IQuestionsListViewMvc,
    QuestionsRecyclerAdapter.Listener {

    private val uiHandler = Handler()

    private val recyclerQuestions: RecyclerView
    private val viewProgress: ProgressBar
    private val toolbar: Toolbar
    private val toolbarViewMvc: IToolbarViewMvc

    private lateinit var questionsRecyclerAdapter: QuestionsRecyclerAdapter

    init {

        setRootView(
            inflater.inflate(R.layout.fragment_questions_list, parent, false)
        )

        recyclerQuestions = getRootView().questions_recycler_items
        viewProgress = getRootView().questions_list_progress
        toolbar = getRootView().toolbar
        toolbarViewMvc = viewMvcFactory.getToolbarViewMvc(toolbar)

        initToolbar()
        initRecycler()
    }

    override fun bindQuestions(questions: List<Question>) {
        uiHandler.post {
            questionsRecyclerAdapter.updateData(questions)
        }
    }

    override fun showProgressIndicator(show: Boolean) {
        uiHandler.post {
            if (show) {
                viewProgress.visibility = View.VISIBLE
            } else {
                viewProgress.visibility = View.GONE
            }
        }
    }

    override fun onQuestionClicked(question: Question?) {
        for (listener in getListeners()) {
            listener.onQuestionClicked(question)
        }
    }

    override fun onDrawerItemClicked(item: DrawerItems) {
        for (listener in getListeners()) {
            when (item) {
                DrawerItems.QUESTIONS_LIST -> listener.onQuestionsListClicked()
            }
        }
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(
            getContext().getString(R.string.questions_list_title)
        )
        toolbarViewMvc.enableMenuAndListen(
            object : IToolbarViewMvc.MenuClickListener {
                override fun onMenuClicked() {
                    openDrawer()
                }
            }
        )

        toolbar.addView(toolbarViewMvc.getRootView())
    }

    private fun initRecycler() {
        uiHandler.post {
            questionsRecyclerAdapter = QuestionsRecyclerAdapter(
                this,
                viewMvcFactory
            )

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