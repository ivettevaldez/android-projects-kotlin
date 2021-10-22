package com.ivettevaldez.coroutines.demos.structuredconcurrency.java;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class FibonacciUseCaseAsyncTest {

    private FibonacciUseCaseAsync sut;
    private FibonacciUseCaseAsync.Callback callback;

    private BigInteger lastResult = null;

    @Before
    public void setUp() {
        callback = result -> lastResult = result;
        sut = new FibonacciUseCaseAsync();
    }

    @Test
    public void computeFibonacci_indexIs0_returns0() throws InterruptedException {
        // Arrange
        // Act
        sut.computeFibonacci(0, callback);
        Thread.sleep(100);
        // Assert
        assertThat(lastResult, is(new BigInteger("0")));
    }

    @Test
    public void computeFibonacci_indexIs1_returns1() throws InterruptedException {
        // Arrange
        // Act
        sut.computeFibonacci(1, callback);
        Thread.sleep(100);
        // Assert
        assertThat(lastResult, is(new BigInteger("1")));
    }

    @Test
    public void computeFibonacci_indexIs10_returnsCorrectValue() throws InterruptedException {
        // Arrange
        // Act
        sut.computeFibonacci(10, callback);
        Thread.sleep(100);
        // Assert
        assertThat(lastResult, is(new BigInteger("55")));
    }

    @Test
    public void computeFibonacci_indexIs30_returnsCorrectValue() throws InterruptedException {
        // Arrange
        // Act
        sut.computeFibonacci(30, callback);
        Thread.sleep(150);
        // Assert
        assertThat(lastResult, is(new BigInteger("832040")));
    }
}