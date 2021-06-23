package com.ivettevaldez.unittesting.unittestingfundamentals.example2

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StringReverserTest {

    private lateinit var sut: StringReverser

    @Before
    fun setUp() {
        sut = StringReverser()
    }

    @Test
    fun reverse_emptyString_emptyStringReturned() {
        val result = sut.reverse("")
        assertEquals(result, "")
    }

    @Test
    fun reverse_singleCharacter_sameStringReturned() {
        val result = sut.reverse("a")
        assertEquals(result, "a")
    }

    @Test
    fun reverse_longString_reversedStringReturned() {
        val result = sut.reverse("Ivette Valdez")
        assertEquals(result, "zedlaV ettevI")
    }
}