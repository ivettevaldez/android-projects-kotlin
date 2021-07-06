package com.ivettevaldez.unittesting.tutorialandroidapp.questions

import com.ivettevaldez.unittesting.tutorialandroidapp.common.time.TimeProvider
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.FetchQuestionDetailsEndpoint
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.QuestionDetailsSchema
import com.ivettevaldez.unittesting.tutorialandroidapp.testsdata.QuestionsTestData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class FetchQuestionDetailsUseCaseTest {

    private lateinit var sut: FetchQuestionDetailsUseCase

    private lateinit var endpointTd: EndpointTd

    @Mock
    private lateinit var listenerMock1: FetchQuestionDetailsUseCase.Listener

    @Mock
    private lateinit var listenerMock2: FetchQuestionDetailsUseCase.Listener

    @Mock
    private lateinit var timeProviderMock: TimeProvider

    @Captor
    private lateinit var detailsCaptor: ArgumentCaptor<QuestionDetails>

    private val questionDetails: QuestionDetails = QuestionsTestData.getQuestionDetails1()

    @Before
    fun setUp() {
        endpointTd = EndpointTd()
        sut = FetchQuestionDetailsUseCase(endpointTd, timeProviderMock)
    }

    @Test
    fun fetchAndNotify_correctQuestionIdPassedToEndpoint() {
        // Arrange
        // Act
        sut.fetchAndNotify(questionDetails.id)
        // Assert
        assertEquals(endpointTd.questionId, questionDetails.id)
    }

    @Test
    fun fetchAndNotify_success_observersNotifiedWithCorrectData() {
        // Arrange
        success()
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        // Act
        sut.fetchAndNotify(questionDetails.id)
        // Assert
        verify(listenerMock1).onQuestionDetailsFetched(capture(detailsCaptor))
        verify(listenerMock2).onQuestionDetailsFetched(capture(detailsCaptor))
        val capture1 = detailsCaptor.allValues[0]
        val capture2 = detailsCaptor.allValues[1]
        assertEquals(capture1, getExpectedQuestionDetails())
        assertEquals(capture2, getExpectedQuestionDetails())
    }

    @Test
    fun fetchAndNotify_failure_observersNotifiedOfFailure() {
        // Arrange
        failure()
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        // Act
        sut.fetchAndNotify(questionDetails.id)
        // Assert
        verify(listenerMock1).onFetchQuestionDetailsFailed()
        verify(listenerMock2).onFetchQuestionDetailsFailed()
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun success() {
        // Nothing to do here for now.
    }

    private fun failure() {
        endpointTd.failure = true
    }

    private fun getExpectedQuestionDetails(): QuestionDetails = questionDetails

    // -----------------------------------------------------------------------------------------
    // HELPER CLASSES
    // -----------------------------------------------------------------------------------------

    private class EndpointTd : FetchQuestionDetailsEndpoint(null) {

        private val questionDetails: QuestionDetails = QuestionsTestData.getQuestionDetails1()

        var callsCount: Int = 0
        var questionId: String? = null
        var failure: Boolean = false

        override fun fetchQuestionDetails(questionId: String, listener: Listener) {
            callsCount++
            this.questionId = questionId

            if (failure) {
                listener.onQuestionDetailsFetchFailed()
            } else {
                listener.onQuestionDetailsFetched(
                    QuestionDetailsSchema(
                        questionDetails.id,
                        questionDetails.title,
                        questionDetails.body
                    )
                )
            }
        }
    }
}