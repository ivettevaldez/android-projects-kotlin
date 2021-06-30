package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchLastActiveQuestionsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts.ToastsHelper

class QuestionsListController(
    private val fetchLastActiveQuestionsUseCase: FetchLastActiveQuestionsUseCase,
    private val screensNavigator: ScreensNavigator,
    private val toastsHelper: ToastsHelper
) : QuestionsListViewMvc.Listener,
    FetchLastActiveQuestionsUseCase.Listener {

    private lateinit var viewMvc: QuestionsListViewMvc

    private var questions: MutableList<Question>? = null

    fun bindView(viewMvc: QuestionsListViewMvc) {
        this.viewMvc = viewMvc
    }

    fun onStart() {
        viewMvc.registerListener(this)
        fetchLastActiveQuestionsUseCase.registerListener(this)

        if (questions != null) {
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

        viewMvc.hideProgressIndicator()
        viewMvc.bindQuestions(questions)
    }

    override fun onFetchLastActiveQuestionsFailed() {
        viewMvc.hideProgressIndicator()
        toastsHelper.showUseCaseError()
    }
}