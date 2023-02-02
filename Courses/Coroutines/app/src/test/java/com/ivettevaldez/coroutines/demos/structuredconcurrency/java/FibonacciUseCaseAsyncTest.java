package com.ivettevaldez.coroutines.demos.structuredconcurrency.java;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class FibonacciUseCaseAsyncTest {

    private FibonacciUseCaseAsync SUT;
    private FibonacciUseCaseAsync.Callback callback;

    private BigInteger lastResult = null;

    @Before
    public void setUp() {
        SUT = new FibonacciUseCaseAsync();
        callback = result -> lastResult = result;
    }

    @Test
    public void computeFibonacci_0_returns0() throws InterruptedException {
        // Arrange
        // Act
        SUT.computeFibonacci(0, callback);
        Thread.sleep(10);
        // Assert
        assertEquals(new BigInteger("0"), lastResult);
    }

    @Test
    public void computeFibonacci_1_returns1() throws InterruptedException {
        // Arrange
        // Act
        SUT.computeFibonacci(1, callback);
        Thread.sleep(10);
        // Assert
        assertEquals(new BigInteger("1"), lastResult);
    }

    @Test
    public void computeFibonacci_10_returnsCorrectValue() throws InterruptedException {
        // Arrange
        // Act
        SUT.computeFibonacci(10, callback);
        Thread.sleep(50);
        // Assert
        assertEquals(new BigInteger("55"), lastResult);
    }

    @Test
    public void computeFibonacci_30_returnsCorrectValue() throws InterruptedException {
        // Arrange
        // Act
        SUT.computeFibonacci(30, callback);
        Thread.sleep(100);
        // Assert
        assertEquals(new BigInteger("832040"), lastResult);
    }
}