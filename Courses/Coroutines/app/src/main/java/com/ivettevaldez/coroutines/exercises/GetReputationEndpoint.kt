package com.ivettevaldez.coroutines.exercises

import kotlin.random.Random
import com.ivettevaldez.coroutines.common.ThreadInfoLogger as logger

class GetReputationEndpoint {

    fun getReputation(userId: String): Int {
        logger.logThreadInfo("GetReputationEndpoint#getReputation(): called")
        Thread.sleep(3000L)
        logger.logThreadInfo("GetReputationEndpoint#getReputation(): return data")
        return Random.nextInt(0, 100)
    }
}