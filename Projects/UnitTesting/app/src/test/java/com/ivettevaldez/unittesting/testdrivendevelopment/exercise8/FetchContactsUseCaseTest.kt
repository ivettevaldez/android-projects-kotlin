package com.ivettevaldez.unittesting.testdrivendevelopment.exercise8

import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.FetchContactsUseCase.Listener
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.contacts.Contact
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.networking.ContactSchema
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.networking.FetchContactsHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.networking.FetchContactsHttpEndpoint.Callback
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.networking.FetchContactsHttpEndpoint.FailReason
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.verifyNoMoreInteractions

@RunWith(MockitoJUnitRunner::class)
class FetchContactsUseCaseTest {

    private lateinit var sut: FetchContactsUseCase

    @Mock
    private lateinit var fetchContactsHttpEndpointMock: FetchContactsHttpEndpoint

    @Mock
    private lateinit var listenerMock1: Listener

    @Mock
    private lateinit var listenerMock2: Listener

    @Captor
    private lateinit var contactsCaptor: ArgumentCaptor<List<Contact>>

    private val stringCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

    companion object {

        private const val FILTER_TERM: String = "filterTerm"

        private const val ID: String = "id"
        private const val FULL_NAME: String = "fullName"
        private const val FULL_PHONE_NUMBER: String = "fullPhoneNumber"
        private const val PHOTO_URL: String = "photoUrl"
        private const val AGE: Double = 30.0
    }

    @Before
    fun setUp() {
        sut = FetchContactsUseCase(fetchContactsHttpEndpointMock)
        success()
    }

    @Test
    fun fetchContacts_correctFilterTermPassedToEndpoint() {
        // Arrange
        // Act
        sut.fetchContactsAndNotify(FILTER_TERM)
        // Assert
        verify(fetchContactsHttpEndpointMock).fetchContacts(capture(stringCaptor), any())
        assertEquals(stringCaptor.value, FILTER_TERM)
    }

    @Test
    fun fetchContacts_success_observersNotifiedWithCorrectData() {
        // Arrange
        // Act
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        sut.fetchContactsAndNotify(FILTER_TERM)
        // Assert
        verify(listenerMock1).onContactsFetched(capture(contactsCaptor))
        verify(listenerMock2).onContactsFetched(capture(contactsCaptor))
        val capture1 = contactsCaptor.allValues[0]
        val capture2 = contactsCaptor.allValues[1]
        assertEquals(capture1, getContacts())
        assertEquals(capture2, getContacts())
    }

    @Test
    fun fetchContacts_success_unsubscribedObserversNotNotified() {
        // Arrange
        // Act
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        sut.unregisterListener(listenerMock2)
        sut.fetchContactsAndNotify(FILTER_TERM)
        // Assert
        verify(listenerMock1).onContactsFetched(capture(contactsCaptor))
        verifyNoMoreInteractions(listenerMock2)
    }

    @Test
    fun fetchContacts_generalError_observersNotifiedOfFailure() {
        // Arrange
        generalError()
        // Act
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        sut.fetchContactsAndNotify(FILTER_TERM)
        // Assert
        verify(listenerMock1).onFetchContactsFailed()
        verify(listenerMock2).onFetchContactsFailed()
    }

    @Test
    fun fetchContacts_networkError_observersNotifiedOfFailure() {
        // Arrange
        networkError()
        // Act
        sut.registerListener(listenerMock1)
        sut.registerListener(listenerMock2)
        sut.fetchContactsAndNotify(FILTER_TERM)
        // Assert
        verify(listenerMock1).onFetchContactsFailed()
        verify(listenerMock2).onFetchContactsFailed()
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun getContactSchemas(): List<ContactSchema> = listOf(
        ContactSchema(
            id = ID,
            fullName = FULL_NAME,
            fullPhoneNumber = FULL_PHONE_NUMBER,
            photoUrl = PHOTO_URL,
            age = AGE
        )
    )

    private fun getContacts(): List<Contact> = listOf(
        Contact(
            id = ID,
            fullName = FULL_NAME,
            fullPhoneNumber = FULL_PHONE_NUMBER,
            photoUrl = PHOTO_URL,
            age = AGE
        )
    )

    private fun success() {
        `when`(
            fetchContactsHttpEndpointMock.fetchContacts(any(), any())
        ).doAnswer {
            val callback = it.arguments[1] as Callback
            callback.onFetchContactsSucceeded(getContactSchemas())
            null
        }
    }

    private fun generalError() {
        `when`(
            fetchContactsHttpEndpointMock.fetchContacts(any(), any())
        ).doAnswer {
            val callback = it.arguments[1] as Callback
            callback.onFetchContactsFailed(FailReason.GENERAL_ERROR)
            null
        }
    }

    private fun networkError() {
        `when`(
            fetchContactsHttpEndpointMock.fetchContacts(any(), any())
        ).doAnswer {
            val callback = it.arguments[1] as Callback
            callback.onFetchContactsFailed(FailReason.NETWORK_ERROR)
            null
        }
    }
}