package com.ivettevaldez.coroutines.demos.structuredconcurrency.java;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class FibonacciUseCaseTest {

    private FibonacciUseCase SUT;

    @Before
    public void setUp() {
        SUT = new FibonacciUseCase();
    }

    @Test
    public void computeFibonacci_0_returns0() {
        // Arrange
        // Act
        BigInteger result = SUT.computeFibonacci(0);
        // Assert
        assertEquals(new BigInteger("0"), result);
    }

    @Test
    public void computeFibonacci_1_returns1() {
        // Arrange
        // Act
        BigInteger result = SUT.computeFibonacci(1);
        // Assert
        assertEquals(new BigInteger("1"), result);
    }

    @Test
    public void computeFibonacci_10_returnsCorrectValue() {
        // Arrange
        // Act
        BigInteger result = SUT.computeFibonacci(10);
        // Assert
        assertEquals(new BigInteger("55"), result);
    }

    @Test
    public void computeFibonacci_30_returnsCorrectValue() {
        // Arrange
        // Act
        BigInteger result = SUT.computeFibonacci(30);
        // Assert
        assertEquals(new BigInteger("832040"), result);
    }
}