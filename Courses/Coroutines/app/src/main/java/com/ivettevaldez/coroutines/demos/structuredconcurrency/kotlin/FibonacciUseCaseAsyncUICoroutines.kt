package com.ivettevaldez.coroutines.demos.structuredconcurrency.kotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.coroutines.CoroutineContext

class FibonacciUseCaseAsyncUICoroutines(private val bgDispatcher: CoroutineContext) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun interface Callback {
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