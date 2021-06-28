package com.ivettevaldez.unittesting.unittestinginandroid.example13

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AndroidTestingProblemsTest {

    private lateinit var sut: AndroidTestingProblems

    @Before
    fun setUp() {
        sut = AndroidTestingProblems()
    }

    @Test
    fun testStaticApiCall() {
        sut.callStaticAndroidApi("")
        assertTrue(true)
    }
}