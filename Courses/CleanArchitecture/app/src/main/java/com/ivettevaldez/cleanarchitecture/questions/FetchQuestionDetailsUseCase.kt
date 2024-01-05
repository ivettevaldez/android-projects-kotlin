package com.ivettevaldez.cleanarchitecture.questions

import com.ivettevaldez.cleanarchitecture.common.BaseObservable
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionDetailsResponseSchema
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionSchema
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchQuestionDetailsUseCase(
    private val stackOverflowApi: StackOverflowApi
) : BaseObservable<FetchQuestionDetailsUseCase.Listener>() {

    interface Listener {

        fun onQuestionDetailsFetched(question: Question)
        fun onQuestionDetailsFetchFailed()
    }

    fun executeAndNotify(questionId: String) {
        stackOverflowApi.fetchQuestionDetails(questionId)!!
            .enqueue(
                object : Callback<QuestionDetailsResponseSchema?> {
                    override fun onResponse(
                        call: Call<QuestionDetailsResponseSchema?>,
                        response: Response<QuestionDetailsResponseSchema?>
                    ) {
                        val question = response.body()?.getQuestion()
                        if (response.isSuccessful && question != null) {
                            notifySuccess(question)
                        } else {
                            notifyFailure()
                        }
                    }

                    override fun onFailure(
                        call: Call<QuestionDetailsResponseSchema?>,
                        throwable: Throwable
                    ) {
                        notifyFailure()
                    }
                }
            )
    }

    private fun notifySuccess(questionSchema: QuestionSchema) {
        for (listener in getListeners()) {
            listener.onQuestionDetailsFetched(
                Question(
                    questionSchema.id,
                    questionSchema.title,
                    questionSchema.body
                )
            )
        }
    }

    private fun notifyFailure() {
        for (listener in getListeners()) {
            listener.onQuestionDetailsFetchFailed()
        }
    }
}