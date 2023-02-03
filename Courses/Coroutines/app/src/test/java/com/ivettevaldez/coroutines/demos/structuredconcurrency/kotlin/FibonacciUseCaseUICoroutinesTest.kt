package com.ivettevaldez.coroutines.demos.structuredconcurrency.kotlin

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

class FibonacciUseCaseUICoroutinesTest {

    private lateinit var sut: FibonacciUseCaseUICoroutines

    @Before
    fun setUp() {
        sut = FibonacciUseCaseUICoroutines()
    }

    @Test
    fun computeFibonacci_0_returns0() = runBlocking {
        // Arrange
        // Act
        val result = sut.computeFibonacci(0)
        // Assert
        assertEquals(BigInteger("0"), result)
    }

    @Test
    fun computeFibonacci_1_returns1() = runBlocking {
        // Arrange
        // Act
        val result = sut.computeFibonacci(1)
        // Assert
        assertEquals(BigInteger("1"), result)
    }

    @Test
    fun computeFibonacci_10_returnsCorrectValue() = runBlocking {
        // Arrange
        // Act
        val result = sut.computeFibonacci(10)
        // Assert
        assertEquals(BigInteger("55"), result)
    }

    @Test
    fun computeFibonacci_30_returnsCorrectValue() = runBlocking {
        // Arrange
        // Act
        val result = sut.computeFibonacci(30)
        // Assert
        assertEquals(BigInteger("832040"), result)
    }
}