package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails

import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.QuestionDetails
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts.ToastsHelper

open class QuestionDetailsController(
    private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase,
    private val screensNavigator: ScreensNavigator,
    private val toastHelper: ToastsHelper
) : QuestionDetailsViewMvc.Listener,
    FetchQuestionDetailsUseCase.Listener {

    private lateinit var viewMvc: QuestionDetailsViewMvc
    private lateinit var questionId: String

    fun bindQuestionId(questionId: String) {
        this.questionId = questionId
    }

    fun bindView(viewMvc: QuestionDetailsViewMvc) {
        this.viewMvc = viewMvc
    }

    fun onStart() {
        fetchQuestionDetailsUseCase.registerListener(this)
        viewMvc.registerListener(this)

        viewMvc.showProgressIndicator()
        fetchQuestionDetailsUseCase.fetchAndNotify(questionId)
    }

    fun onStop() {
        fetchQuestionDetailsUseCase.unregisterListener(this)
        viewMvc.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onQuestionDetailsFetched(details: QuestionDetails) {
        viewMvc.hideProgressIndicator()
        viewMvc.bindQuestionDetails(details)
    }

    override fun onFetchQuestionDetailsFailed() {
        viewMvc.hideProgressIndicator()
        toastHelper.showUseCaseError()
    }
}