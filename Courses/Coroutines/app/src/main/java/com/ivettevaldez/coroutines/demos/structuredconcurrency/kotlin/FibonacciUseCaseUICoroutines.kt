package com.ivettevaldez.coroutines.demos.structuredconcurrency.kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class FibonacciUseCaseUICoroutines {

    suspend fun computeFibonacci(index: Int): BigInteger = withContext(Dispatchers.Default) {
        when (index) {
            0 -> BigInteger("0")
            1 -> BigInteger("1")
            else -> computeFibonacci(index - 1).add(computeFibonacci(index - 2))
        }
    }
}