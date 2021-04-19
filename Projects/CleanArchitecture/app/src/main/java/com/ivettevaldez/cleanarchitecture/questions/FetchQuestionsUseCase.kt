package com.ivettevaldez.cleanarchitecture.questions

import com.ivettevaldez.cleanarchitecture.common.BaseObservable
import com.ivettevaldez.cleanarchitecture.common.Constants
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionSchema
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionsListResponseSchema
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchQuestionsUseCase(
    private val stackOverflowApi: StackOverflowApi
) : BaseObservable<FetchQuestionsUseCase.Listener>() {

    interface Listener {

        fun onQuestionsFetched(questions: List<Question>)
        fun onQuestionsFetchFailed()
    }

    fun executeAndNotify() {
        stackOverflowApi.fetchLastActiveQuestions(Constants.QUESTIONS_LIST_PAGE_SIZE)!!
            .enqueue(
                object : Callback<QuestionsListResponseSchema?> {
                    override fun onResponse(
                        call: Call<QuestionsListResponseSchema?>,
                        response: Response<QuestionsListResponseSchema?>
                    ) {
                        val questions = response.body()?.questions
                        if (response.isSuccessful && questions != null) {
                            notifySuccess(questions)
                        } else {
                            notifyFailure()
                        }
                    }

                    override fun onFailure(
                        call: Call<QuestionsListResponseSchema?>,
                        throwable: Throwable
                    ) {
                        notifyFailure()
                    }
                }
            )
    }

    fun notifySuccess(questionsSchemas: List<QuestionSchema>) {
        val questions = mutableListOf<Question>()
        for (questionSchema in questionsSchemas) {
            questions.add(
                Question(
                    questionSchema.id,
                    questionSchema.title,
                    questionSchema.body
                )
            )
        }

        for (listener in getListeners()) {
            listener.onQuestionsFetched(questions)
        }
    }

    fun notifyFailure() {
        for (listener in getListeners()) {
            listener.onQuestionsFetchFailed()
        }
    }
}