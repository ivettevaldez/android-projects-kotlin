package com.ivettevaldez.dependencyinjection.questions

import com.ivettevaldez.dependencyinjection.common.Constants
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FetchQuestionsUseCase {

    sealed class Result {

        class Success(val questions: List<Question>) : Result()
        object Failure : Result()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val stackOverflowApi = retrofit.create(StackOverflowApi::class.java)

    suspend fun execute(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val response = stackOverflowApi.lastActiveQuestions(20)
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Result.Success(response.body()!!.questions)
                } else {
                    return@withContext Result.Failure
                }
            } catch (t: Throwable) {
                if (t is CancellationException) {
                    return@withContext Result.Failure
                } else {
                    throw t
                }
            }
        }
    }
}