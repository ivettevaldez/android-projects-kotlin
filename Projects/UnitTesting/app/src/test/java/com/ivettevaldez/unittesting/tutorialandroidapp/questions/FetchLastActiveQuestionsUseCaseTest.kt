package com.ivettevaldez.unittesting.tutorialandroidapp.questions

import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.QuestionSchema
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.list.FetchLastActiveQuestionsEndpoint
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

    companion object {

        private const val ID_1 = "id1"
        private const val ID_2 = "id2"
        private const val TITLE_1 = "title1"
        private const val TITLE_2 = "title2"
        private const val BODY_1 = "body1"
        private const val BODY_2 = "body2"
    }

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
        assertEquals(capture1, getExpectedQuestions())
        assertEquals(capture2, getExpectedQuestions())
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

    private fun getExpectedQuestions(): List<Question> {
        return listOf(
            Question(ID_1, TITLE_1),
            Question(ID_2, TITLE_2)
        )
    }

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

        var failure: Boolean = false

        override fun fetchLastActiveQuestions(listener: Listener) {
            if (failure) {
                listener.onQuestionsFetchFailed()
            } else {
                listener.onQuestionsFetched(
                    listOf(
                        QuestionSchema(ID_1, TITLE_1, BODY_1),
                        QuestionSchema(ID_2, TITLE_2, BODY_2)
                    )
                )
            }
        }
    }
}