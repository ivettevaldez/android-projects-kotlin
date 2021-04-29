package com.ivettevaldez.dependencyinjection.screens.questionslist

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.controllers.BaseActivity
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import kotlinx.coroutines.*

class QuestionsListActivity : BaseActivity(),
    IQuestionsListViewMvc.Listener {

    private val classTag: String = this::class.java.simpleName
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var isDataLoaded: Boolean = false

    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var dialogsNavigator: DialogsNavigator
    private lateinit var fetchQuestionsUseCase: FetchQuestionsUseCase
    private lateinit var viewMvc: QuestionsListViewMvcImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screensNavigator = compositionRoot.screensNavigator
        dialogsNavigator = compositionRoot.dialogsNavigator
        fetchQuestionsUseCase = compositionRoot.fetchQuestionsUseCase

        viewMvc = QuestionsListViewMvcImpl(
            LayoutInflater.from(this), null
        )
        setContentView(viewMvc.rootView)
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