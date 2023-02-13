package com.ivettevaldez.coroutines.demos.coroutinescancellationcooperative2

import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class CancellableBenchmark2UseCase {

    companion object {

        const val ONE_SECOND_NANO: Long = 1_000_000_000L
    }

    suspend fun executeBenchmark(durationInSeconds: Int): Long {
        ThreadInfoLogger.logThreadInfo("benchmark started")
        val stopTimeNano = System.nanoTime() + durationInSeconds * ONE_SECOND_NANO

        var iterationsCount: Long = 0
        while (System.nanoTime() < stopTimeNano) {
            coroutineContext.ensureActive()
            iterationsCount++
        }
        ThreadInfoLogger.logThreadInfo("benchmark completed")

        return iterationsCount
    }
}