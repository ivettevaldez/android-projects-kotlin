package com.ivettevaldez.coroutines.exercises.exercise4

import com.ivettevaldez.coroutines.exercises.exercise4.FactorialUseCase.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

class FactorialUseCaseTest {

    private lateinit var sut: FactorialUseCase

    @Before
    fun setUp() {
        sut = FactorialUseCase()
    }

    @Test
    fun computeFactorial_0_returns1() = runBlocking {
        // Arrange
        // Act
        val result = sut.computeFactorial(0, 10)
        // Assert
        assertEquals(BigInteger("1"), (result as Result.Success).result)
    }

    @Test
    fun computeFactorial_1_returns1() = runBlocking {
        // Arrange
        // Act
        val result = sut.computeFactorial(1, 10)
        // Assert
        assertEquals(BigInteger("1"), (result as Result.Success).result)
    }

    @Test
    fun computeFactorial_10_returnsCorrectAnswer() = runBlocking {
        // Arrange
        // Act
        val result = sut.computeFactorial(10, 100)
        // Assert
        assertEquals(BigInteger("3628800"), (result as Result.Success).result)
    }

    @Test
    fun computeFactorial_30_returnsCorrectAnswer() = runBlocking {
        // Arrange
        // Act
        val result = sut.computeFactorial(30, 100)
        // Assert
        assertEquals(
            BigInteger("265252859812191058636308480000000"),
            (result as Result.Success).result
        )
    }
}