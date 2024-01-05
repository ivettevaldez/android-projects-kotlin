package com.ivettevaldez.unittesting.tutorialandroidapp.questions

import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.QuestionSchema
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.list.FetchLastActiveQuestionsEndpoint
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
class FetchLastActiveQuestionsUseCaseTest {

    private lateinit var sut: FetchLastActiveQuestionsUseCase

    private lateinit var endpointTd: EndpointTd

    @Mock
    private lateinit var listenerMock1: FetchLastActiveQuestionsUseCase.Listener

    @Mock
    private lateinit var listenerMock2: FetchLastActiveQuestionsUseCase.Listener

    @Captor
    private lateinit var questionsCaptor: ArgumentCaptor<List<Question>>

    private val expectedQuestions: List<Question> = QuestionsTestData.getQuestions()

    @Before
    fun setUp() {
        endpointTd = EndpointTd()
        sut = FetchLastActiveQuestionsUseCase(endpointTd)
    }

    @Test
    fun fetchAndNotify_success_observersNotifiedWithCorrectData() {
        // Arrange
        success()
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        // Act
        sut.fetchAndNotify()
        // Assert
        verify(listenerMock1).onLastActiveQuestionsFetched(capture(questionsCaptor))
        verify(listenerMock2).onLastActiveQuestionsFetched(capture(questionsCaptor))
        val capture1 = questionsCaptor.allValues[0]
        val capture2 = questionsCaptor.allValues[1]
        assertEquals(capture1, expectedQuestions)
        assertEquals(capture2, expectedQuestions)
    }

    @Test
    fun fetchAndNotify_failure_observersNotifiedOfFailure() {
        // Arrange
        failure()
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        // Act
        sut.fetchAndNotify()
        // Assert
        verify(listenerMock1).onFetchLastActiveQuestionsFailed()
        verify(listenerMock2).onFetchLastActiveQuestionsFailed()
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

    // -----------------------------------------------------------------------------------------
    // HELPER CLASSES
    // -----------------------------------------------------------------------------------------

    private class EndpointTd : FetchLastActiveQuestionsEndpoint(null) {

        private val questionDetails1: QuestionDetails = QuestionsTestData.getQuestionDetails1()
        private val questionDetails2: QuestionDetails = QuestionsTestData.getQuestionDetails2()

        var failure: Boolean = false

        override fun fetchLastActiveQuestions(listener: Listener) {
            if (failure) {
                listener.onQuestionsFetchFailed()
            } else {
                listener.onQuestionsFetched(
                    listOf(
                        QuestionSchema(
                            questionDetails1.id,
                            questionDetails1.title,
                            questionDetails1.body
                        ),
                        QuestionSchema(
                            questionDetails2.id,
                            questionDetails2.title,
                            questionDetails2.body
                        )
                    )
                )
            }
        }
    }
}