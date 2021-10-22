package com.ivettevaldez.coroutines.demos.structuredconcurrency.java;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class FibonacciUseCaseTest {

    private FibonacciUseCase sut;

    @Before
    public void setUp() {
        sut = new FibonacciUseCase();
    }

    @Test
    public void computeFibonacci_indexIs0_returns0() {
        // Arrange
        // Act
        BigInteger result = sut.computeFibonacci(0);
        // Assert
        assertThat(result, is(new BigInteger("0")));
    }

    @Test
    public void computeFibonacci_indexIs1_returns1() {
        // Arrange
        // Act
        BigInteger result = sut.computeFibonacci(1);
        // Assert
        assertThat(result, is(new BigInteger("1")));
    }

    @Test
    public void computeFibonacci_indexIs10_returnsCorrectValue() {
        // Arrange
        // Act
        BigInteger result = sut.computeFibonacci(10);
        // Assert
        assertThat(result, is(new BigInteger("55")));
    }

    @Test
    public void computeFibonacci_indexIs30_returnsCorrectValue() {
        // Arrange
        // Act
        BigInteger result = sut.computeFibonacci(30);
        // Assert
        assertThat(result, is(new BigInteger("832040")));
    }
}