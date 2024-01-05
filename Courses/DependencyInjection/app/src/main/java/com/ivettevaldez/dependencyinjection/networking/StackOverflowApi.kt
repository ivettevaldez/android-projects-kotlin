package com.ivettevaldez.dependencyinjection.networking

import com.ivettevaldez.dependencyinjection.common.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackOverflowApi {

    @GET("/questions?key=" + Constants.STACK_OVERFLOW_API_KEY + "&order=desc&sort=activity&site=stackoverflow")
    suspend fun lastActiveQuestions(@Query("pagesize") pageSize: Int?): Response<QuestionsListResponseSchema>

    @GET("/questions/{questionId}?key=" + Constants.STACK_OVERFLOW_API_KEY + "&site=stackoverflow&filter=withbody")
    suspend fun questionDetails(@Path("questionId") questionId: String?): Response<SingleQuestionResponseSchema>
}