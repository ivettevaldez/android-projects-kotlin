package com.ivettevaldez.coroutines.demos.structuredconcurrency.kotlin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

@ExperimentalCoroutinesApi
class FibonacciUseCaseCoroutinesTest {

    private lateinit var sut: FibonacciUseCaseCoroutines

    @Before
    fun setUp() {
        sut = FibonacciUseCaseCoroutines()
    }

    @Test
    fun computeFibonacci_indexIs0_returns0() {
        runBlocking {
            // Arrange
            // Act
            val result = sut.computeFibonacci(0)
            // Assert
            assertThat(result, `is`(BigInteger("0")))
        }
    }

    @Test
    fun computeFibonacci_indexIs1_returns1() {
        runBlocking {
            // Arrange
            // Act
            val result = sut.computeFibonacci(1)
            // Assert
            assertThat(result, `is`(BigInteger("1")))
        }
    }

    @Test
    fun computeFibonacci_indexIs10_returnsCorrectValue() {
        runBlocking {
            // Arrange
            // Act
            val result = sut.computeFibonacci(10)
            // Assert
            assertThat(result, `is`(BigInteger("55")))
        }
    }

    @Test
    fun computeFibonacci_indexIs30_returnsCorrectValue() {
        runBlocking {
            // Arrange
            // Act
            val result = sut.computeFibonacci(30)
            // Assert
            assertThat(result, `is`(BigInteger("832040")))
        }
    }
}