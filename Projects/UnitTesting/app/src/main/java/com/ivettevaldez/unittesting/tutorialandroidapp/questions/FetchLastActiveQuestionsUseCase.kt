package com.ivettevaldez.unittesting.tutorialandroidapp.questions

import com.ivettevaldez.unittesting.tutorialandroidapp.common.BaseObservable
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.QuestionSchema
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.list.FetchLastActiveQuestionsEndpoint

open class FetchLastActiveQuestionsUseCase(
    private val fetchLastActiveQuestionsEndpoint: FetchLastActiveQuestionsEndpoint?
) : BaseObservable<FetchLastActiveQuestionsUseCase.Listener>(),
    FetchLastActiveQuestionsEndpoint.Listener {

    interface Listener {

        fun onLastActiveQuestionsFetched(questions: List<Question>)
        fun onFetchLastActiveQuestionsFailed()
    }

    override fun onQuestionsFetched(questions: List<QuestionSchema?>?) {
        if (questions != null) {
            notifySuccess(questions)
        }
    }

    override fun onQuestionsFetchFailed() {
        notifyFailure()
    }

    open fun fetchAndNotify() {
        fetchLastActiveQuestionsEndpoint!!.fetchLastActiveQuestions(this)
    }

    private fun getQuestionsFromSchemas(schemas: List<QuestionSchema?>): List<Question> {
        val questions: MutableList<Question> = mutableListOf()
        for (schema in schemas) {
            if (schema != null) {
                questions.add(
                    Question(
                        id = schema.id,
                        title = schema.title
                    )
                )
            }
        }
        return questions
    }

    private fun notifySuccess(schemas: List<QuestionSchema?>) {
        for (listener in listeners) {
            listener.onLastActiveQuestionsFetched(
                getQuestionsFromSchemas(schemas)
            )
        }
    }

    private fun notifyFailure() {
        for (listener in listeners) {
            listener.onFetchLastActiveQuestionsFailed()
        }
    }
}