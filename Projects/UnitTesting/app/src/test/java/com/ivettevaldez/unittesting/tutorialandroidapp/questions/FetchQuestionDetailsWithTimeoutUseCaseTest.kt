package com.ivettevaldez.unittesting.tutorialandroidapp.questions

import com.ivettevaldez.unittesting.tutorialandroidapp.common.time.TimeProvider
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.FetchQuestionDetailsEndpoint
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.QuestionDetailsSchema
import com.ivettevaldez.unittesting.tutorialandroidapp.testsdata.QuestionsTestData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer

@RunWith(MockitoJUnitRunner::class)
class FetchQuestionDetailsWithTimeoutUseCaseTest {

    private lateinit var sut: FetchQuestionDetailsUseCase

    @Mock
    private lateinit var fetchQuestionDetailsEndpointMock: FetchQuestionDetailsEndpoint

    @Mock
    private lateinit var timeProviderMock: TimeProvider

    private val listener1: ListenerTd = ListenerTd()
    private val listener2: ListenerTd = ListenerTd()
    private val questionDetails1: QuestionDetails = QuestionsTestData.getQuestionDetails1()
    private val questionDetails2: QuestionDetails = QuestionsTestData.getQuestionDetails2()

    private var endpointCallsCount: Int = 0

    companion object {

        private const val CACHE_TIMEOUT_MS: Long = 60000L
    }

    @Before
    fun setUp() {
        sut = FetchQuestionDetailsUseCase(fetchQuestionDetailsEndpointMock, timeProviderMock)

        sut.registerListener(listener1)
        sut.registerListener(listener2)
    }

    @Test
    fun fetchAndNotify_success_listenersNotifiedWithCorrectData() {
        // Arrange
        success()
        // Act
        sut.fetchAndNotify(questionDetails1.id)
        // Assert
        listener1.assertSuccessfulCalls(1)
        assertEquals(listener1.getLastData(), questionDetails1)
        listener2.assertSuccessfulCalls(1)
        assertEquals(listener2.getLastData(), questionDetails1)
    }

    @Test
    fun fetchAndNotify_failure_listenersNotifiedOfFailure() {
        // Arrange
        failure()
        // Act
        sut.fetchAndNotify(questionDetails1.id)
        // Assert
        listener1.assertOneFailingCall()
        listener2.assertOneFailingCall()
    }

    @Test
    fun fetchAndNotify_secondTimeImmediatelyAfterSuccess_listenersNotifiedWithDataFromCache() {
        // Arrange
        success()
        // Act
        sut.fetchAndNotify(questionDetails1.id)
        sut.fetchAndNotify(questionDetails1.id)
        // Assert
        listener1.assertSuccessfulCalls(2)
        assertEquals(listener1.getLastData(), questionDetails1)
        listener2.assertSuccessfulCalls(2)
        assertEquals(listener2.getLastData(), questionDetails1)
        assertEquals(endpointCallsCount, 1)
    }

    @Test
    fun fetchAndNotify_secondTimeRightBeforeTimeoutAfterSuccess_listenersNotifiedWithDataFromCache() {
        // Arrange
        success()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        sut.fetchAndNotify(questionDetails1.id)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT_MS - 1)
        sut.fetchAndNotify(questionDetails1.id)
        // Assert
        listener1.assertSuccessfulCalls(2)
        assertEquals(listener1.getLastData(), questionDetails1)
        listener2.assertSuccessfulCalls(2)
        assertEquals(listener2.getLastData(), questionDetails1)
        assertEquals(endpointCallsCount, 1)
    }

    @Test
    fun fetchAndNotify_secondTimeAfterTimeoutAfterSuccess_listenersNotifiedWithDataFromEndpoint() {
        // Arrange
        success()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        sut.fetchAndNotify(questionDetails1.id)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT_MS)
        sut.fetchAndNotify(questionDetails1.id)
        // Assert
        listener1.assertSuccessfulCalls(2)
        assertEquals(listener1.getLastData(), questionDetails1)
        listener2.assertSuccessfulCalls(2)
        assertEquals(listener2.getLastData(), questionDetails1)
        assertEquals(endpointCallsCount, 2)
    }

    @Test
    fun fetchAndNotify_secondTimeWithDifferentIdAfterSuccess_listenersNotifiedWithDataFromEndpoint() {
        // Arrange
        success()
        // Act
        sut.fetchAndNotify(questionDetails1.id)
        sut.fetchAndNotify(questionDetails2.id)
        // Assert
        listener1.assertSuccessfulCalls(2)
        assertEquals(listener1.getLastData(), questionDetails2)
        listener2.assertSuccessfulCalls(2)
        assertEquals(listener2.getLastData(), questionDetails2)
        assertEquals(endpointCallsCount, 2)
    }

    @Test
    fun fetchAndNotify_afterTwoDifferentQuestionsAtDifferentTimesFirstQuestionRightBeforeTimeout_listenersNotifiedWithDataFromCache() {
        // Arrange
        success()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        sut.fetchAndNotify(questionDetails1.id)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT_MS / 2)
        sut.fetchAndNotify(questionDetails2.id)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT_MS - 1)
        sut.fetchAndNotify(questionDetails1.id)
        // Assert
        listener1.assertSuccessfulCalls(3)
        assertEquals(listener1.getLastData(), questionDetails1)
        listener2.assertSuccessfulCalls(3)
        assertEquals(listener2.getLastData(), questionDetails1)
        assertEquals(endpointCallsCount, 2)
    }

    @Test
    fun fetchAndNotify_afterTwoDifferentQuestionsAtDifferentTimesSecondQuestionRightBeforeTimeout_listenersNotifiedWithDataFromCache() {
        // Arrange
        success()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        sut.fetchAndNotify(questionDetails1.id)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT_MS / 2)
        sut.fetchAndNotify(questionDetails2.id)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn((CACHE_TIMEOUT_MS / 2) - 1)
        sut.fetchAndNotify(questionDetails2.id)
        // Assert
        listener1.assertSuccessfulCalls(3)
        assertEquals(listener1.getLastData(), questionDetails2)
        listener2.assertSuccessfulCalls(3)
        assertEquals(listener2.getLastData(), questionDetails2)
        assertEquals(endpointCallsCount, 2)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun success() {
        `when`(
            fetchQuestionDetailsEndpointMock.fetchQuestionDetails(any(), any())
        ).doAnswer {
            endpointCallsCount++

            val questionId = it.arguments[0] as String
            val listener = it.arguments[1] as FetchQuestionDetailsEndpoint.Listener

            when (questionId) {
                questionDetails1.id -> {
                    listener.onQuestionDetailsFetched(
                        QuestionDetailsSchema(
                            questionDetails1.id,
                            questionDetails1.title,
                            questionDetails1.body
                        )
                    )
                }
                questionDetails2.id -> {
                    listener.onQuestionDetailsFetched(
                        QuestionDetailsSchema(
                            questionDetails2.id,
                            questionDetails2.title,
                            questionDetails2.body
                        )
                    )
                }
                else -> {
                    throw RuntimeException("Unhandled question ID: $questionId")
                }
            }
        }
    }

    private fun failure() {
        `when`(
            fetchQuestionDetailsEndpointMock.fetchQuestionDetails(any(), any())
        ).doAnswer {
            endpointCallsCount++

            val listener = it.arguments[1] as FetchQuestionDetailsEndpoint.Listener
            listener.onQuestionDetailsFetchFailed()
        }
    }

    // -----------------------------------------------------------------------------------------
    // HELPER CLASSES
    // -----------------------------------------------------------------------------------------

    private class ListenerTd : FetchQuestionDetailsUseCase.Listener {

        private var callsCount: Int = 0
        private var successCallsCount: Int = 0

        private lateinit var questionDetailsData: QuestionDetails

        override fun onQuestionDetailsFetched(details: QuestionDetails) {
            callsCount++
            successCallsCount++

            this.questionDetailsData = details
        }

        override fun onFetchQuestionDetailsFailed() {
            callsCount++
        }

        fun getLastData(): QuestionDetails = questionDetailsData

        fun assertSuccessfulCalls(count: Int) {
            if (callsCount != count || callsCount != successCallsCount) {
                throw RuntimeException(
                    "$count successful call(s) assertion failed; " +
                            "calls: $callsCount; successes: $successCallsCount"
                )
            }
        }

        fun assertOneFailingCall() {
            if (callsCount != 1 || successCallsCount > 0) {
                throw RuntimeException("One failing call assertion failed")
            }
        }
    }
}