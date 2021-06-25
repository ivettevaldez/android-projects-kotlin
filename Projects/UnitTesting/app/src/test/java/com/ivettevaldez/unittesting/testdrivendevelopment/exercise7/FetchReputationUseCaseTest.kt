package com.ivettevaldez.unittesting.testdrivendevelopment.exercise7

import com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.FetchReputationUseCase.UseCaseResult
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.FetchReputationUseCase.UseCaseStatus
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.networking.IFetchReputationHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.networking.IFetchReputationHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.networking.IFetchReputationHttpEndpoint.EndpointStatus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchReputationUseCaseTest {

    private lateinit var sut: FetchReputationUseCase

    @Mock
    private lateinit var fetchReputationHttpEndpointMock: IFetchReputationHttpEndpoint

    companion object {

        private const val REPUTATION = 5
        private const val DEFAULT_REPUTATION: Int = 0
    }

    @Before
    fun setUp() {
        sut = FetchReputationUseCase(fetchReputationHttpEndpointMock)
        success()
    }

    @Test
    fun fetchReputation_success_successReturned() {
        // Arrange
        // Act
        val result: UseCaseResult = sut.fetchReputation()
        // Assert
        assertEquals(result.status, UseCaseStatus.SUCCESS)
    }

    @Test
    fun fetchReputation_success_correctReputationReturned() {
        // Arrange
        // Act
        val result: UseCaseResult = sut.fetchReputation()
        // Assert
        assertEquals(result.reputation, REPUTATION)
    }

    @Test
    fun fetchReputation_generalError_failureReturned() {
        // Arrange
        generalError()
        // Act
        val result: UseCaseResult = sut.fetchReputation()
        // Assert
        assertEquals(result.status, UseCaseStatus.FAILURE)
    }

    @Test
    fun fetchReputation_generalError_defaultReputationReturned() {
        // Arrange
        generalError()
        // Act
        val result: UseCaseResult = sut.fetchReputation()
        // Assert
        assertEquals(result.reputation, DEFAULT_REPUTATION)
    }

    @Test
    fun fetchReputation_networkError_failureReturned() {
        // Arrange
        networkError()
        // Act
        val result: UseCaseResult = sut.fetchReputation()
        // Assert
        assertEquals(result.status, UseCaseStatus.FAILURE)
    }

    @Test
    fun fetchReputation_networkError_defaultReputationReturned() {
        // Arrange
        networkError()
        // Act
        val result: UseCaseResult = sut.fetchReputation()
        // Assert
        assertEquals(result.reputation, DEFAULT_REPUTATION)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun success() {
        `when`(
            fetchReputationHttpEndpointMock.fetchReputation()
        ).thenReturn(
            EndpointResult(EndpointStatus.SUCCESS, REPUTATION)
        )
    }

    private fun generalError() {
        `when`(
            fetchReputationHttpEndpointMock.fetchReputation()
        ).thenReturn(
            EndpointResult(EndpointStatus.GENERAL_ERROR, DEFAULT_REPUTATION)
        )
    }

    private fun networkError() {
        `when`(
            fetchReputationHttpEndpointMock.fetchReputation()
        ).thenReturn(
            EndpointResult(EndpointStatus.NETWORK_ERROR, DEFAULT_REPUTATION)
        )
    }
}