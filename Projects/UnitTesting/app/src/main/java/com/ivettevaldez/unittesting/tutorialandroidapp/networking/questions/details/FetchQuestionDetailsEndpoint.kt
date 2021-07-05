package com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details

import com.ivettevaldez.unittesting.tutorialandroidapp.networking.StackOverflowApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class FetchQuestionDetailsEndpoint(private val stackOverflowApi: StackOverflowApi?) {

    interface Listener {

        fun onQuestionDetailsFetched(questionDetailsSchema: QuestionDetailsSchema?)
        fun onQuestionDetailsFetchFailed()
    }

    open fun fetchQuestionDetails(questionId: String, listener: Listener) {
        stackOverflowApi!!.fetchQuestionDetails(questionId)!!
            .enqueue(object : Callback<QuestionDetailsResponseSchema?> {
                override fun onResponse(
                    call: Call<QuestionDetailsResponseSchema?>?,
                    response: Response<QuestionDetailsResponseSchema?>
                ) {
                    if (response.isSuccessful) {
                        listener.onQuestionDetailsFetched(response.body()!!.items[0])
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