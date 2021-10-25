package com.ivettevaldez.coroutines.demos.structuredconcurrency.kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

@ExperimentalCoroutinesApi
class FibonacciUseCaseAsyncUiCoroutinesTest {

    private lateinit var sut: FibonacciUseCaseAsyncUiCoroutines
    private lateinit var callback: FibonacciUseCaseAsyncUiCoroutines.Callback

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private var lastResult: BigInteger? = null

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        callback = object : FibonacciUseCaseAsyncUiCoroutines.Callback {
            override fun onFibonacciComputed(result: BigInteger?) {
                lastResult = result
            }
        }
        sut = FibonacciUseCaseAsyncUiCoroutines(testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun computeFibonacci_indexIs0_returns0() {
        testCoroutineDispatcher.runBlockingTest {
            // Arrange
            // Act
            sut.computeFibonacci(0, callback)
            // Assert
            assertThat(lastResult, `is`(BigInteger("0")))
        }
    }

    @Test
    fun computeFibonacci_indexIs1_returns1() {
        testCoroutineDispatcher.runBlockingTest {
            // Arrange
            // Act
            sut.computeFibonacci(1, callback)
            // Assert
            assertThat(lastResult, `is`(BigInteger("1")))
        }
    }

    @Test
    fun computeFibonacci_indexIs10_returnsCorrectValue() {
        testCoroutineDispatcher.runBlockingTest {
            // Arrange
            // Act
            sut.computeFibonacci(10, callback)
            // Assert
            assertThat(lastResult, `is`(BigInteger("55")))
        }
    }

    @Test
    fun computeFibonacci_indexIs30_returnsCorrectValue() {
        testCoroutineDispatcher.runBlockingTest {
            // Arrange
            // Act
            sut.computeFibonacci(30, callback)
            // Assert
            assertThat(lastResult, `is`(BigInteger("832040")))
        }
    }
}