package com.ivettevaldez.unittesting.testdrivendevelopment.example9

import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.NetworkErrorException
import com.ivettevaldez.unittesting.testdrivendevelopment.example9.AddToCartUseCase.UseCaseResult
import com.ivettevaldez.unittesting.testdrivendevelopment.example9.networking.AddToCartEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.example9.networking.AddToCartEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdrivendevelopment.example9.networking.CartItemSchema
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class AddToCartUseCaseTest {

    private lateinit var sut: AddToCartUseCase

    @Mock
    private lateinit var addToCartEndpointMock: AddToCartEndpoint

    private val cartItemCaptor: ArgumentCaptor<CartItemSchema> =
        ArgumentCaptor.forClass(CartItemSchema::class.java)

    companion object {

        private const val OFFER_ID: Int = 1
        private const val AMOUNT: Int = 4
    }

    @Before
    fun setUp() {
        sut = AddToCartUseCase(addToCartEndpointMock)
        success()
    }

    @Test
    fun addToCart_success_correctParametersPassedToEndpoint() {
        // Act
        sut.addToCart(OFFER_ID, AMOUNT)
        // Assert
        verify(addToCartEndpointMock).addToCart(capture(cartItemCaptor))
        assertEquals(cartItemCaptor.value.offerId, OFFER_ID)
        assertEquals(cartItemCaptor.value.amount, AMOUNT)
    }

    @Test
    fun addToCart_success_successReturned() {
        // Act
        val result = sut.addToCart(OFFER_ID, AMOUNT)
        // Assert
        assertEquals(result, UseCaseResult.SUCCESS)
    }

    @Test
    fun addToCart_generalError_failureReturned() {
        // Arrange
        generalError()
        // Act
        val result = sut.addToCart(OFFER_ID, AMOUNT)
        // Assert
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun addToCart_authError_failureReturned() {
        // Arrange
        authError()
        // Act
        val result = sut.addToCart(OFFER_ID, AMOUNT)
        // Assert
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun addToCart_networkError_networkErrorReturned() {
        // Arrange
        networkError()
        // Act
        val result = sut.addToCart(OFFER_ID, AMOUNT)
        // Assert
        assertEquals(result, UseCaseResult.NETWORK_ERROR)
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun success() {
        `when`(
            addToCartEndpointMock.addToCart(any())
        ).thenReturn(
            EndpointResultStatus.SUCCESS
        )
    }

    private fun generalError() {
        `when`(
            addToCartEndpointMock.addToCart(any())
        ).thenReturn(
            EndpointResultStatus.GENERAL_ERROR
        )
    }

    private fun authError() {
        `when`(
            addToCartEndpointMock.addToCart(any())
        ).thenReturn(
            EndpointResultStatus.AUTH_ERROR
        )
    }

    private fun networkError() {
        `when`(
            addToCartEndpointMock.addToCart(any())
        ).doAnswer {
            throw NetworkErrorException()
        }
    }
}