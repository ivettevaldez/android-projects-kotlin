package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails

import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts.ToastsHelper
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuestionDetailsControllerTest {

    private lateinit var sut: QuestionDetailsController
    private lateinit var useCaseTd: UseCaseTd

    @Mock
    private lateinit var screensNavigator: ScreensNavigator

    @Mock
    private lateinit var toastsHelper: ToastsHelper

    @Before
    fun setUp() {
        useCaseTd = UseCaseTd()
        sut = QuestionDetailsController(useCaseTd, screensNavigator, toastsHelper)
    }

    // onStart: Progress indicator shown
    // onStart: Success - Progress indicator hidden
    // onStart: Success - Question bound to the view
    // onStart: Failure - Progress indicator hidden
    // onStart: Failure - Error toast shown
    // onStart: Listeners registered
    // onStop: Listeners unregistered

    private class UseCaseTd : FetchQuestionDetailsUseCase(null) {

        override fun fetchAndNotify(questionId: String) {
            TODO("Not yet implemented")
        }
    }
}