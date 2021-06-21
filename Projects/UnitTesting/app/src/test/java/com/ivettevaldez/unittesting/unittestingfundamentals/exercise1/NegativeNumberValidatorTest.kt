package com.ivettevaldez.unittesting.unittestingfundamentals.exercise1

import org.junit.Assert.assertEquals
import org.junit.Test

class NegativeNumberValidatorTest {

    private val sut: NegativeNumberValidator = NegativeNumberValidator()

    @Test
    fun test1() {
        val result = sut.isNegative(-1)
        assertEquals(result, true)
    }

    @Test
    fun test2() {
        val result = sut.isNegative(0)
        assertEquals(result, true)
    }

    @Test
    fun test3() {
        val result = sut.isNegative(1)
        assertEquals(result, false)
    }
}