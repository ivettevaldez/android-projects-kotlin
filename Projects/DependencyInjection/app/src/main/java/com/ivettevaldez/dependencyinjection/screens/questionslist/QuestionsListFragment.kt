package com.ivettevaldez.dependencyinjection.screens.questionslist

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.Service
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.controllers.BaseFragment
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.viewsmvc.ViewMvcFactory
import kotlinx.coroutines.*

class QuestionsListFragment : BaseFragment(),
    IQuestionsListViewMvc.Listener {

    private val classTag: String = this::class.java.simpleName
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @field: Service
    private lateinit var screensNavigator: ScreensNavigator

    @field: Service
    private lateinit var dialogsNavigator: DialogsNavigator

    @field: Service
    private lateinit var fetchQuestionsUseCase: FetchQuestionsUseCase

    @field: Service
    private lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: QuestionsListViewMvcImpl

    private var isDataLoaded: Boolean = false

    companion object {

        fun newInstance() = QuestionsListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewMvc = viewMvcFactory.newQuestionsListViewMvc(null)
        return viewMvc.rootView
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)

        if (!isDataLoaded) {
            fetchQuestions()
        }
    }

    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)
        coroutineScope.coroutineContext.cancelChildren()
    }

    override fun onRefreshClicked() {
        fetchQuestions()
    }

    override fun onQuestionClicked(questionId: String) {
        screensNavigator.toQuestionDetails(questionId)
    }

    private fun fetchQuestions() {
        coroutineScope.launch {
            viewMvc.showProgressIndicator()

            try {
                when (val result = fetchQuestionsUseCase.execute()) {
                    is FetchQuestionsUseCase.Result.Success -> {
                        viewMvc.bindQuestions(result.questions)
                        isDataLoaded = true
                    }
                    is FetchQuestionsUseCase.Result.Failure -> {
                        onFetchFailed()
                    }
                }
            } catch (ex: Exception) {
                Log.e(classTag, "Attempting to fetch questions", ex)
                onFetchFailed()
            } finally {
                viewMvc.hideProgressIndicator()
            }
        }
    }

    private fun onFetchFailed() {
        dialogsNavigator.showServerErrorDialog()
    }
}