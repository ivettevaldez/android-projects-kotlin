package com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details

import com.ivettevaldez.unittesting.tutorialandroidapp.networking.StackOverflowApi
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.QuestionSchema
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchQuestionDetailsEndpoint(private val stackOverflowApi: StackOverflowApi) {

    interface Listener {

        fun onQuestionDetailsFetched(question: QuestionSchema?)
        fun onQuestionDetailsFetchFailed()
    }

    fun fetchQuestionDetails(questionId: String?, listener: Listener) {
        stackOverflowApi.fetchQuestionDetails(questionId)!!
            .enqueue(object : Callback<QuestionDetailsResponseSchema?> {
                override fun onResponse(
                    call: Call<QuestionDetailsResponseSchema?>?,
                    response: Response<QuestionDetailsResponseSchema?>
                ) {
                    if (response.isSuccessful) {
                        listener.onQuestionDetailsFetched(response.body()!!.getQuestion())
                    } else {
                        listener.onQuestionDetailsFetchFailed()
                    }
                }

                override fun onFailure(call: Call<QuestionDetailsResponseSchema?>?, t: Throwable?) {
                    listener.onQuestionDetailsFetchFailed()
                }
            })
    }
}