package com.ivettevaldez.unittesting.unittestingfundamentals.exercise2

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StringDuplicatorTest {

    private lateinit var sut: StringDuplicator

    @Before
    fun setUp() {
        sut = StringDuplicator()
    }

    @Test
    fun duplicate_emptyString_emptyStringReturned() {
        val result = sut.duplicate("")
        assertEquals(result, "")
    }

    @Test
    fun duplicate_singleCharString_duplicatedStringReturned() {
        val result = sut.duplicate("a")
        assertEquals(result, "aa")
    }

    @Test
    fun duplicate_longString_duplicatedStringReturned() {
        val result = sut.duplicate("Ivette Valdez")
        assertEquals(result, "Ivette ValdezIvette Valdez")
    }
}