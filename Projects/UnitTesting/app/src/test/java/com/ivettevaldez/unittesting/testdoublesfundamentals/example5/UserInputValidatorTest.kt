package com.ivettevaldez.unittesting.testdoublesfundamentals.example5

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserInputValidatorTest {

    private lateinit var sut: UserInputValidator

    companion object {

        private const val VALID_USER_NAME: String = "validUserName"
        private const val VALID_FULL_NAME: String = "validFullName"
        private const val INVALID_VALUE: String = ""
    }

    @Before
    fun setUp() {
        sut = UserInputValidator()
    }

    @Test
    fun isValidFullName_validFullName_trueReturned() {
        val result = sut.isValidFullName(VALID_FULL_NAME)
        assertTrue(result)
    }

    @Test
    fun isValidFullName_invalidFullName_falseReturned() {
        val result = sut.isValidFullName(INVALID_VALUE)
        assertFalse(result)
    }

    @Test
    fun isValidUserName_validUserName_trueReturned() {
        val result = sut.isValidUserName(VALID_USER_NAME)
        assertTrue(result)
    }

    @Test
    fun isValidUserName_invalidUserName_falseReturned() {
        val result = sut.isValidUserName(INVALID_VALUE)
        assertFalse(result)
    }
}