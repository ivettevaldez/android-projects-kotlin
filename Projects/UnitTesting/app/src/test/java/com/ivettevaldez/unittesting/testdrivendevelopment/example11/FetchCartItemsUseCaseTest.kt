package com.ivettevaldez.unittesting.testdrivendevelopment.example11

import com.ivettevaldez.unittesting.testdrivendevelopment.example11.cartitems.CartItem
import com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking.CartItemSchema
import com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking.IFetchCartItemsHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking.IFetchCartItemsHttpEndpoint.Callback
import com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking.IFetchCartItemsHttpEndpoint.FailReason
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@RunWith(MockitoJUnitRunner::class)
class FetchCartItemsUseCaseTest {

    private lateinit var sut: FetchCartItemsUseCase

    @Mock
    private lateinit var fetchCartItemsHttpEndpointMock: IFetchCartItemsHttpEndpoint

    @Mock
    private lateinit var listenerMock1: FetchCartItemsUseCase.Listener

    @Mock
    private lateinit var listenerMock2: FetchCartItemsUseCase.Listener

    @Captor
    private lateinit var cartItemsCaptor: ArgumentCaptor<List<CartItem>>

    @Captor
    private lateinit var intCaptor: ArgumentCaptor<Int>

    companion object {

        private const val LIMIT: Int = 10

        private const val ID: String = "id"
        private const val TITLE: String = "title"
        private const val DESCRIPTION: String = "description"
        private const val PRICE: Int = 5
    }

    @Before
    fun setUp() {
        sut = FetchCartItemsUseCase(fetchCartItemsHttpEndpointMock)
        success()
    }

    @Test
    fun fetchCartItems_correctLimitPassedToEndpoint() {
        // Arrange
        // Act
        sut.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(fetchCartItemsHttpEndpointMock).fetchCartItems(capture(intCaptor), any())
        assertEquals(intCaptor.value, LIMIT)
    }

    @Test
    fun fetchCartItems_success_observersNotifiedWithCorrectData() {
        // Arrange
        // Act
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        sut.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(listenerMock1).onCartItemsFetched(capture(cartItemsCaptor))
        verify(listenerMock2).onCartItemsFetched(capture(cartItemsCaptor))
        val capture1 = cartItemsCaptor.allValues[0]
        val capture2 = cartItemsCaptor.allValues[1]
        assertEquals(capture1, getCartItems())
        assertEquals(capture2, getCartItems())
    }

    @Test
    fun fetchCartItems_success_unsubscribedObserversNotNotified() {
        // Arrange
        // Act
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        sut.unregisterListener(listenerMock2)
        sut.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(listenerMock1).onCartItemsFetched(any())
        verifyNoMoreInteractions(listenerMock2)
    }

    @Test
    fun fetchCartItems_generalError_observersNotifiedOfFailure() {
        // Arrange
        generalError()
        // Act
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        sut.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(listenerMock1).onFetchCartItemsFailed()
        verify(listenerMock2).onFetchCartItemsFailed()
    }

    @Test
    fun fetchCartItems_networkError_observersNotifiedOfFailure() {
        // Arrange
        networkError()
        // Act
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        sut.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(listenerMock1).onFetchCartItemsFailed()
        verify(listenerMock2).onFetchCartItemsFailed()
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun getCartItemSchemas(): List<CartItemSchema> =
        listOf(CartItemSchema(ID, TITLE, DESCRIPTION, PRICE))

    private fun getCartItems(): List<CartItem> =
        listOf(CartItem(ID, TITLE, DESCRIPTION, PRICE))

    private fun success() {
        `when`(
            fetchCartItemsHttpEndpointMock.fetchCartItems(any(), any())
        ).doAnswer {
            val callback = it.arguments[1] as Callback
            callback.onFetchCartItemsSucceeded(getCartItemSchemas())
            null
        }
    }

    private fun generalError() {
        `when`(
            fetchCartItemsHttpEndpointMock.fetchCartItems(any(), any())
        ).doAnswer {
            val callback = it.arguments[1] as Callback
            callback.onFetchCartItemsFailed(FailReason.GENERAL_ERROR)
            null
        }
    }

    private fun networkError() {
        `when`(
            fetchCartItemsHttpEndpointMock.fetchCartItems(any(), any())
        ).doAnswer {
            val callback = it.arguments[1] as Callback
            callback.onFetchCartItemsFailed(FailReason.NETWORK_ERROR)
            null
        }
    }
}