package com.ivettevaldez.unittesting.tutorialandroidapp.questions

import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.FetchQuestionDetailsEndpoint
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.QuestionDetailsSchema
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

    @Captor
    private lateinit var detailsCaptor: ArgumentCaptor<QuestionDetails>

    companion object {

        private const val QUESTION_ID: String = "questionId"
        private const val ID: String = "questionId"
        private const val TITLE: String = "title"
        private const val BODY: String = "body"
    }

    @Before
    fun setUp() {
        endpointTd = EndpointTd()
        sut = FetchQuestionDetailsUseCase(endpointTd)
    }

    @Test
    fun fetchAndNotify_correctQuestionIdPassedToEndpoint() {
        // Arrange
        // Act
        sut.fetchAndNotify(QUESTION_ID)
        // Assert
        assertEquals(endpointTd.questionId, QUESTION_ID)
    }

    @Test
    fun fetchAndNotify_success_observersNotifiedWithCorrectData() {
        // Arrange
        success()
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        // Act
        sut.fetchAndNotify(QUESTION_ID)
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
        sut.fetchAndNotify(QUESTION_ID)
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

    private fun getExpectedQuestionDetails(): QuestionDetails {
        return QuestionDetails(
            ID, TITLE, BODY
        )
    }

    // -----------------------------------------------------------------------------------------
    // HELPER CLASSES
    // -----------------------------------------------------------------------------------------

    private class EndpointTd : FetchQuestionDetailsEndpoint(null) {

        var questionId: String? = null
        var failure: Boolean = false

        override fun fetchQuestionDetails(questionId: String, listener: Listener) {
            this.questionId = questionId

            if (failure) {
                listener.onQuestionDetailsFetchFailed()
            } else {
                listener.onQuestionDetailsFetched(
                    QuestionDetailsSchema(
                        ID, TITLE, BODY
                    )
                )
            }
        }
    }
}