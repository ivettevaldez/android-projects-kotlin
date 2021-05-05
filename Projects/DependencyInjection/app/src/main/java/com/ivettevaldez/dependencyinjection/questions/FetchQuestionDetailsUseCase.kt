package com.ivettevaldez.dependencyinjection.questions

import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchQuestionDetailsUseCase @Inject constructor(private val stackOverflowApi: StackOverflowApi) {

    sealed class Result {

        data class Success(val question: QuestionWithBody) : Result()
        object Failure : Result()
    }

    suspend fun execute(questionId: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val response = stackOverflowApi.questionDetails(questionId)
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Result.Success(
                        response.body()!!.question
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