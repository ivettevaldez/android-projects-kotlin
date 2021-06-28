package com.ivettevaldez.unittesting.unittestinginandroid.example12

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class StringRetrieverTest {

    private lateinit var sut: StringRetriever

    @Mock
    private lateinit var contextMock: Context

    companion object {

        private const val ID = 10
        private const val STRING = "string"
    }

    @Before
    fun setUp() {
        sut = StringRetriever(contextMock)
        success()
    }

    @Test
    fun getString_correctParametersPassedToEndpoint() {
        // Arrange
        // Act
        sut.getString(ID)
        // Assert
        verify(contextMock).getString(ID)
    }

    @Test
    fun getString_correctStringReturned() {
        // Arrange
        // Act
        val result = sut.getString(ID)
        // Assert
        verify(contextMock).getString(ID)
        assertEquals(result, STRING)
    }

    private fun success() {
        `when`(contextMock.getString(ID)).thenReturn(STRING)
    }
}