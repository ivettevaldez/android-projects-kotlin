package com.ivettevaldez.coroutines.demos.structuredconcurrency.java;

import static org.junit.Assert.assertEquals;

import com.techyourchance.threadposter.testdoubles.ThreadPostersTestDouble;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class FibonacciUseCaseAsyncUIThreadPosterTest {

    private FibonacciUseCaseAsyncUIThreadPoster SUT;
    private FibonacciUseCaseAsyncUIThreadPoster.Callback callback;

    private ThreadPostersTestDouble threadPostersTestDouble;

    private BigInteger lastResult = null;

    @Before
    public void setUp() {
        threadPostersTestDouble = new ThreadPostersTestDouble();
        SUT = new FibonacciUseCaseAsyncUIThreadPoster(
                threadPostersTestDouble.getBackgroundTestDouble(),
                threadPostersTestDouble.getUiTestDouble()
        );
        callback = result -> lastResult = result;
    }

    @Test
    public void computeFibonacci_0_returns0() {
        // Arrange
        // Act
        SUT.computeFibonacci(0, callback);
        threadPostersTestDouble.join();
        // Assert
        assertEquals(new BigInteger("0"), lastResult);
    }

    @Test
    public void computeFibonacci_1_returns1() {
        // Arrange
        // Act
        SUT.computeFibonacci(1, callback);
        threadPostersTestDouble.join();
        // Assert
        assertEquals(new BigInteger("1"), lastResult);
    }

    @Test
    public void computeFibonacci_10_returnsCorrectValue() {
        // Arrange
        // Act
        SUT.computeFibonacci(10, callback);
        threadPostersTestDouble.join();
        // Assert
        assertEquals(new BigInteger("55"), lastResult);
    }

    @Test
    public void computeFibonacci_30_returnsCorrectValue() {
        // Arrange
        // Act
        SUT.computeFibonacci(30, callback);
        threadPostersTestDouble.join();
        // Assert
        assertEquals(new BigInteger("832040"), lastResult);
    }
}