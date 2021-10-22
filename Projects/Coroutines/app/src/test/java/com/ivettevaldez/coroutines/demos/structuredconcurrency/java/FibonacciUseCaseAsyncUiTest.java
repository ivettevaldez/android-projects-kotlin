package com.ivettevaldez.coroutines.demos.structuredconcurrency.java;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.techyourchance.threadposter.testdoubles.ThreadPostersTestDouble;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class FibonacciUseCaseAsyncUiTest {

    private FibonacciUseCaseAsyncUi sut;
    private FibonacciUseCaseAsyncUi.Callback callback;

    private ThreadPostersTestDouble threadPostersTestDouble;

    private BigInteger lastResult = null;

    @Before
    public void setUp() {
        callback = result -> lastResult = result;
        threadPostersTestDouble = new ThreadPostersTestDouble();

        sut = new FibonacciUseCaseAsyncUi(
                threadPostersTestDouble.getBackgroundTestDouble(),
                threadPostersTestDouble.getUiTestDouble()
        );
    }

    @Test
    public void computeFibonacci_indexIs0_returns0() {
        // Arrange
        // Act
        sut.computeFibonacci(0, callback);
        threadPostersTestDouble.join();
        // Assert
        assertThat(lastResult, is(new BigInteger("0")));
    }

    @Test
    public void computeFibonacci_indexIs1_returns1() {
        // Arrange
        // Act
        sut.computeFibonacci(1, callback);
        threadPostersTestDouble.join();
        // Assert
        assertThat(lastResult, is(new BigInteger("1")));
    }

    @Test
    public void computeFibonacci_indexIs10_returnsCorrectValue() {
        // Arrange
        // Act
        sut.computeFibonacci(10, callback);
        threadPostersTestDouble.join();
        // Assert
        assertThat(lastResult, is(new BigInteger("55")));
    }

    @Test
    public void computeFibonacci_indexIs30_returnsCorrectValue() {
        // Arrange
        // Act
        sut.computeFibonacci(30, callback);
        threadPostersTestDouble.join();
        // Assert
        assertThat(lastResult, is(new BigInteger("832040")));
    }
}