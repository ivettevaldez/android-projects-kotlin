package com.ivettevaldez.dependencyinjection.questions

import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchQuestionsUseCase(private val stackOverflowApi: StackOverflowApi) {

    sealed class Result {

        class Success(val questions: List<Question>) : Result()
        object Failure : Result()
    }

    suspend fun execute(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val response = stackOverflowApi.lastActiveQuestions(20)
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Result.Success(
                        response.body()!!.questions
                    )
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