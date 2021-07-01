package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails

import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts.ToastsHelper

open class QuestionDetailsController(
    private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase,
    private val screensNavigator: ScreensNavigator,
    private val toastHelper: ToastsHelper
) {

    private lateinit var viewMvc: QuestionDetailsViewMvc
    private lateinit var questionId: String

    fun bindQuestionId(questionId: String) {
        this.questionId = questionId
    }

    fun bindView(viewMvc: QuestionDetailsViewMvc) {
        this.viewMvc = viewMvc
    }
}