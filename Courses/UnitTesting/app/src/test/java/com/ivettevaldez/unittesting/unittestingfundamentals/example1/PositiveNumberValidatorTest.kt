package com.ivettevaldez.unittesting.unittestingfundamentals.example1

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PositiveNumberValidatorTest {

    private lateinit var sut: PositiveNumberValidator

    @Before
    fun setUp() {
        sut = PositiveNumberValidator()
    }

    @Test
    fun test1() {
        val result = sut.isPositive(-1)
        assertEquals(result, false)
    }

    @Test
    fun test2() {
        val result = sut.isPositive(0)
        assertEquals(result, false)
    }

    @Test
    fun test3() {
        val result = sut.isPositive(1)
        assertEquals(result, true)
    }
}