package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchLastActiveQuestionsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts.ToastsHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.testsdata.QuestionsTestData
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class QuestionsListControllerTest {

    private lateinit var sut: QuestionsListController
    private lateinit var useCaseTd: UseCaseTd

    @Mock
    private lateinit var viewMvc: QuestionsListViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var toastsHelperMock: ToastsHelper

    private val expectedQuestion: Question = QuestionsTestData.getQuestion()
    private val expectedQuestions: List<Question> = QuestionsTestData.getQuestions()

    @Before
    fun setUp() {
        useCaseTd = UseCaseTd()

        sut = QuestionsListController(useCaseTd, screensNavigatorMock, toastsHelperMock)
        sut.bindView(viewMvc)
    }

    @Test
    fun onStart_progressIndicatorShown() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvc).showProgressIndicator()
    }

    @Test
    fun onStart_success_progressIndicatorHidden() {
        // Arrange
        success()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvc).hideProgressIndicator()
    }

    @Test
    fun onStart_success_questionsBoundToView() {
        // Arrange
        success()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvc).bindQuestions(expectedQuestions)
    }

    @Test
    fun onStart_secondTimeSuccess_questionsBoundToViewFromCache() {
        // Arrange
        success()
        // Act
        sut.onStart()
        sut.onStart()
        // Assert
        verify(viewMvc, times(2)).bindQuestions(expectedQuestions)
        Assert.assertEquals(useCaseTd.callsCount, 1)
    }

    @Test
    fun onStart_failure_progressIndicatorHidden() {
        // Arrange
        failure()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvc).hideProgressIndicator()
    }

    @Test
    fun onStart_failure_questionsNotBoundToView() {
        // Arrange
        failure()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvc, never()).bindQuestions(any())
    }

    @Test
    fun onStart_failure_showErrorToast() {
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
        verify(viewMvc).registerListener(sut)
        useCaseTd.verifyRegisteredListener(sut)
    }

    @Test
    fun onStop_listenersUnregistered() {
        // Arrange
        // Act
        sut.onStop()
        // Assert
        verify(viewMvc).unregisterListener(sut)
        useCaseTd.verifyNotRegisteredListener(sut)
    }

    @Test
    fun onQuestionClicked_navigatedToQuestionDetailsScreen() {
        // Arrange
        // Act
        sut.onQuestionClicked(expectedQuestion)
        // Assert
        verify(screensNavigatorMock).toQuestionDetails(expectedQuestion.id)
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

    private class UseCaseTd : FetchLastActiveQuestionsUseCase(null) {

        private val expectedQuestions: List<Question> = QuestionsTestData.getQuestions()

        var failure: Boolean = false
        var callsCount: Int = 0

        override fun fetchAndNotify() {
            callsCount++

            for (listener in listeners) {
                if (failure) {
                    listener.onFetchLastActiveQuestionsFailed()
                } else {
                    listener.onLastActiveQuestionsFetched(expectedQuestions)
                }
            }
        }

        fun verifyRegisteredListener(candidate: QuestionsListController) {
            for (listener in listeners) {
                if (listener == candidate) {
                    return
                } else {
                    throw RuntimeException("Listener is not registered")
                }
            }
        }

        fun verifyNotRegisteredListener(candidate: QuestionsListController) {
            for (listener in listeners) {
                if (listener == candidate) {
                    throw RuntimeException("Listener is registered")
                }
            }
        }
    }
}