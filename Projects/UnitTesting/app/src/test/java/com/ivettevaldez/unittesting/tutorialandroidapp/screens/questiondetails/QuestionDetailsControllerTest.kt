package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails

import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts.ToastsHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.testsdata.QuestionsTestData
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class QuestionDetailsControllerTest {

    private lateinit var sut: QuestionDetailsController
    private lateinit var useCaseTd: UseCaseTd

    @Mock
    private lateinit var viewMvcMock: QuestionDetailsViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var toastsHelperMock: ToastsHelper

    private val expectedQuestionDetails = QuestionsTestData.getQuestionDetails()

    @Before
    fun setUp() {
        useCaseTd = UseCaseTd()

        sut = QuestionDetailsController(useCaseTd, screensNavigatorMock, toastsHelperMock)
        sut.bindView(viewMvcMock)
        sut.bindQuestionId(expectedQuestionDetails.id)
    }

    @Test
    fun onStart_success_progressIndicatorShown() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).showProgressIndicator()
    }

    @Test
    fun onStart_success_progressIndicatorHidden() {
        // Arrange
        success()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).hideProgressIndicator()
    }

    @Test
    fun onStart_success_questionBoundToView() {
        // Arrange
        success()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).bindQuestionDetails(expectedQuestionDetails)
    }

    @Test
    fun onStart_failure_progressIndicatorHidden() {
        // Arrange
        failure()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).hideProgressIndicator()
    }

    @Test
    fun onStart_failure_errorToastShown() {
        // Arrange
        failure()
        // Act
        sut.onStart()
        // Assert
        verify(toastsHelperMock).showUseCaseError()
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).registerListener(sut)
        useCaseTd.verifyRegisteredListener(sut)
    }

    @Test
    fun onStop_listenersUnregistered() {
        // Arrange
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
        useCaseTd.verifyNotRegisteredListener(sut)
    }

    @Test
    fun onNavigateUpClicked_navigateUp() {
        // Arrange
        // Act
        sut.onNavigateUpClicked()
        // Assert
        verify(screensNavigatorMock).navigateUp()
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun success() {
        // Nothing to do here by now.
    }

    private fun failure() {
        useCaseTd.failure = true
    }

    // -----------------------------------------------------------------------------------------
    // HELPER CLASSES
    // -----------------------------------------------------------------------------------------

    private class UseCaseTd : FetchQuestionDetailsUseCase(null) {

        private val expectedQuestionDetails = QuestionsTestData.getQuestionDetails()

        var failure: Boolean = false

        override fun fetchAndNotify(questionId: String) {
            for (listener in listeners) {
                if (failure) {
                    listener.onFetchQuestionDetailsFailed()
                } else {
                    listener.onQuestionDetailsFetched(expectedQuestionDetails)
                }
            }
        }

        fun verifyRegisteredListener(candidate: QuestionDetailsController) {
            for (listener in listeners) {
                if (listener == candidate) {
                    return
                }
            }
            throw RuntimeException("Listener is not registered")
        }

        fun verifyNotRegisteredListener(candidate: QuestionDetailsController) {
            for (listener in listeners) {
                if (listener == candidate) {
                    throw RuntimeException("Listener is not registered")
                }
            }
        }
    }
}