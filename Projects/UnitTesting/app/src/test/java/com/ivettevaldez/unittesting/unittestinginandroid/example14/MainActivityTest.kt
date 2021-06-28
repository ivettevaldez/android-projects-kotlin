package com.ivettevaldez.unittesting.unittestinginandroid.example14

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainActivityTest {

    private lateinit var sut: MainActivity

    @Before
    fun setUp() {
        sut = MainActivity()
    }

    @Test
    fun onStart_incrementsCountByOne() {
        // Arrange
        // Act
        sut.start()
        // Assert
        assertEquals(sut.count, 1)
    }
}