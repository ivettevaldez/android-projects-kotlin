package com.ivettevaldez.coroutines.exercises

import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import kotlin.random.Random

class GetReputationEndpoint {

    private val classTag: String = this::class.java.simpleName

    fun getReputation(userId: String): Int {
        ThreadInfoLogger.logThreadInfo("$classTag#getReputation(): called")
        Thread.sleep(3000)
        ThreadInfoLogger.logThreadInfo("$classTag#getReputation(): return data")
        return Random.nextInt(0, 100)
    }
}