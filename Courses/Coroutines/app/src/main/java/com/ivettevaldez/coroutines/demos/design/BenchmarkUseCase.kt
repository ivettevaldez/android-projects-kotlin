package com.ivettevaldez.coroutines.demos.design

import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BenchmarkUseCase {

    companion object {

        const val ONE_SECOND_NANO: Long = 1_000_000_000L
    }

    suspend fun executeBenchmark(durationInSeconds: Int): Long = withContext(Dispatchers.Default) {
        ThreadInfoLogger.logThreadInfo("benchmark started")
        val stopTimeNano = System.nanoTime() + durationInSeconds * ONE_SECOND_NANO

        var iterationsCount: Long = 0
        while (System.nanoTime() < stopTimeNano) {
            iterationsCount++
        }
        ThreadInfoLogger.logThreadInfo("benchmark completed")

        iterationsCount
    }
}