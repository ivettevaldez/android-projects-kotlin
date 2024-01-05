package com.ivettevaldez.unittesting.testdrivendevelopment.example10

import com.ivettevaldez.unittesting.testdrivendevelopment.example10.PingServerUseCase.UseCaseResult
import com.ivettevaldez.unittesting.testdrivendevelopment.example10.networking.IPingServerHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.example10.networking.IPingServerHttpEndpoint.EndpointResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PingServerUseCaseTest {

    private lateinit var sut: PingServerUseCase

    @Mock
    private lateinit var pingServerHttpEndpointMock: IPingServerHttpEndpoint

    @Before
    fun setUp() {
        sut = PingServerUseCase(pingServerHttpEndpointMock)
        success()
    }

    @Test
    fun pingServer_success_successReturned() {
        // Arrange
        // Act
        val result: UseCaseResult = sut.pingServer()
        // Assert
        assertEquals(result, UseCaseResult.SUCCESS)
    }

    @Test
    fun pingServer_generalError_failureReturned() {
        // Arrange
        generalError()
        // Act
        val result: UseCaseResult = sut.pingServer()
        // Assert
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun pingServer_networkError_failureReturned() {
        // Arrange
        networkError()
        // Act
        val result: UseCaseResult = sut.pingServer()
        // Assert
        assertEquals(result, UseCaseResult.FAILURE)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun success() {
        `when`(
            pingServerHttpEndpointMock.pingServer()
        ).thenReturn(
            EndpointResult.SUCCESS
        )
    }

    private fun generalError() {
        `when`(
            pingServerHttpEndpointMock.pingServer()
        ).thenReturn(
            EndpointResult.GENERAL_ERROR
        )
    }

    private fun networkError() {
        `when`(
            pingServerHttpEndpointMock.pingServer()
        ).thenReturn(
            EndpointResult.NETWORK_ERROR
        )
    }
}