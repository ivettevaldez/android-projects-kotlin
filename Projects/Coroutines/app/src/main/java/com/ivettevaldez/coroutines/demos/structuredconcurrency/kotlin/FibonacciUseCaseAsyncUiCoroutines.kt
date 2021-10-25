package com.ivettevaldez.coroutines.demos.structuredconcurrency.kotlin

import kotlinx.coroutines.*
import java.math.BigInteger

class FibonacciUseCaseAsyncUiCoroutines(private val bgDispatcher: CoroutineDispatcher) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    interface Callback {

        fun onFibonacciComputed(result: BigInteger?)
    }

    fun computeFibonacci(index: Int, callback: Callback) {
        coroutineScope.launch {
            val result = computeFibonacciBg(index)
            callback.onFibonacciComputed(result)
        }
    }

    private suspend fun computeFibonacciBg(index: Int): BigInteger =
        withContext(bgDispatcher) {
            when (index) {
                0 -> {
                    BigInteger("0")
                }
                1 -> {
                    BigInteger("1")
                }
                else -> {
                    computeFibonacciBg(index - 1).add(computeFibonacciBg(index - 2))
                }
            }
        }
}