package com.ivettevaldez.coroutines.demos.structuredconcurrency.kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

@OptIn(ExperimentalCoroutinesApi::class)
class FibonacciUseCaseAsyncUICoroutinesTest {

    private lateinit var sut: FibonacciUseCaseAsyncUICoroutines
    private lateinit var callback: FibonacciUseCaseAsyncUICoroutines.Callback

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private var lastResult: BigInteger? = null

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        sut = FibonacciUseCaseAsyncUICoroutines(testCoroutineDispatcher)
        callback = FibonacciUseCaseAsyncUICoroutines.Callback { result -> lastResult = result }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun computeFibonacci_0_returns0() = testCoroutineDispatcher.runBlockingTest {
        // Arrange
        // Act
        sut.computeFibonacci(0, callback)
        // Assert
        assertEquals(BigInteger("0"), lastResult)
    }

    @Test
    fun computeFibonacci_1_returns1() = testCoroutineDispatcher.runBlockingTest {
        // Arrange
        // Act
        sut.computeFibonacci(1, callback)
        // Assert
        assertEquals(BigInteger("1"), lastResult)
    }

    @Test
    fun computeFibonacci_10_returnsCorrectValue() = testCoroutineDispatcher.runBlockingTest {
        // Arrange
        // Act
        sut.computeFibonacci(10, callback)
        // Assert
        assertEquals(BigInteger("55"), lastResult)
    }

    @Test
    fun computeFibonacci_30_returnsCorrectValue() = testCoroutineDispatcher.runBlockingTest {
        // Arrange
        // Act
        sut.computeFibonacci(30, callback)
        // Assert
        assertEquals(BigInteger("832040"), lastResult)
    }
}