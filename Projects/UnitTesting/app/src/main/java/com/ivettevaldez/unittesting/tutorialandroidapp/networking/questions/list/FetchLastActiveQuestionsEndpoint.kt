package com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.list

import com.ivettevaldez.unittesting.tutorialandroidapp.common.Constants
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.StackOverflowApi
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.QuestionSchema
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchLastActiveQuestionsEndpoint(private val stackOverflowApi: StackOverflowApi) {

    interface Listener {

        fun onQuestionsFetched(questions: List<QuestionSchema?>?)
        fun onQuestionsFetchFailed()
    }

    fun fetchLastActiveQuestions(listener: Listener) {
        stackOverflowApi.fetchLastActiveQuestions(Constants.QUESTIONS_LIST_PAGE_SIZE)!!
            .enqueue(object : Callback<QuestionsListResponseSchema?> {
                override fun onResponse(
                    call: Call<QuestionsListResponseSchema?>?,
                    response: Response<QuestionsListResponseSchema?>
                ) {
                    if (response.isSuccessful) {
                        listener.onQuestionsFetched(response.body()!!.questions)
                    } else {
                        listener.onQuestionsFetchFailed()
                    }
                }

                override fun onFailure(call: Call<QuestionsListResponseSchema?>?, t: Throwable?) {
                    listener.onQuestionsFetchFailed()
                }
            })
    }
}