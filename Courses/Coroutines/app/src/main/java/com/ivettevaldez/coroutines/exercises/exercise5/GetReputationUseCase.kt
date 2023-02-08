package com.ivettevaldez.coroutines.exercises.exercise5

import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import com.ivettevaldez.coroutines.exercises.exercise1.GetReputationEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetReputationUseCase(private val getReputationEndpoint: GetReputationEndpoint) {

    suspend fun getReputationForUser(userId: String): Int = withContext(Dispatchers.Default) {
        ThreadInfoLogger.logThreadInfo("getReputationForUser()")
        getReputationEndpoint.getReputation(userId)
    }
}