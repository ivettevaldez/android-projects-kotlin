package com.ivettevaldez.unittesting.unittestingfundamentals.exercise1

import org.junit.Assert.assertEquals
import org.junit.Test

class PositiveNumberValidatorTest {

    private val sut: PositiveNumberValidator = PositiveNumberValidator()

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