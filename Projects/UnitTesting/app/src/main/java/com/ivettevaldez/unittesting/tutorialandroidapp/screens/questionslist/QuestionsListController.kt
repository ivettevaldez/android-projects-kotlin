package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import com.ivettevaldez.unittesting.tutorialandroidapp.common.time.TimeProvider
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchLastActiveQuestionsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts.ToastsHelper

class QuestionsListController(
    private val fetchLastActiveQuestionsUseCase: FetchLastActiveQuestionsUseCase,
    private val screensNavigator: ScreensNavigator,
    private val toastsHelper: ToastsHelper,
    private val timeProvider: TimeProvider
) : QuestionsListViewMvc.Listener,
    FetchLastActiveQuestionsUseCase.Listener {

    private lateinit var viewMvc: QuestionsListViewMvc

    private var questions: MutableList<Question>? = null
    private var lastCachedTimestamp: Long = 0L

    companion object {

        private const val CACHE_TIMEOUT_MS = 10000L
    }

    fun bindView(viewMvc: QuestionsListViewMvc) {
        this.viewMvc = viewMvc
    }

    fun onStart() {
        viewMvc.registerListener(this)
        fetchLastActiveQuestionsUseCase.registerListener(this)

        if (isCacheDataValid()) {
            viewMvc.bindQuestions(questions!!)
        } else {
            viewMvc.showProgressIndicator()
            fetchLastActiveQuestionsUseCase.fetchAndNotify()
        }
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
        fetchLastActiveQuestionsUseCase.unregisterListener(this)
    }

    override fun onQuestionClicked(question: Question) {
        screensNavigator.toQuestionDetails(question.id)
    }

    override fun onLastActiveQuestionsFetched(questions: List<Question>) {
        this.questions = mutableListOf()
        this.questions!!.addAll(questions)
        lastCachedTimestamp = timeProvider.getCurrentTimestamp()

        viewMvc.hideProgressIndicator()
        viewMvc.bindQuestions(questions)
    }

    override fun onFetchLastActiveQuestionsFailed() {
        viewMvc.hideProgressIndicator()
//        toastsHelper.showUseCaseError()
    }

    private fun isCacheDataValid(): Boolean {
        return questions != null &&
                timeProvider.getCurrentTimestamp() < lastCachedTimestamp + CACHE_TIMEOUT_MS
    }
}