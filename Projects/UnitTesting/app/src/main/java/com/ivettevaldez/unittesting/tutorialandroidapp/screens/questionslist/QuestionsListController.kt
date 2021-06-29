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

    fun bindView(viewMvc: QuestionsListViewMvc) {
        this.viewMvc = viewMvc
    }

    fun onStart() {
        viewMvc.registerListener(this)
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    override fun onQuestionClicked(question: Question) {
        TODO("Not yet implemented")
    }

    override fun onLastActiveQuestionsFetched(questions: List<Question>) {
        TODO("Not yet implemented")
    }

    override fun onFetchLastActiveQuestionsFailed() {
        TODO("Not yet implemented")
    }
}