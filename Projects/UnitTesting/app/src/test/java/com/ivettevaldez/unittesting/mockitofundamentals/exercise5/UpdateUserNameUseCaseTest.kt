package com.ivettevaldez.unittesting.mockitofundamentals.exercise5

import com.ivettevaldez.unittesting.mockitofundamentals.example7.eventbus.EventBusPoster
import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.NetworkErrorException
import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.UpdateUserNameUseCase.UseCaseResult
import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent
import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.networking.UpdateUserNameHttpEndpoint
import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.networking.UpdateUserNameHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.networking.UpdateUserNameHttpEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.users.User
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.users.UsersCache
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer

class UpdateUserNameUseCaseTest {

    private lateinit var sut: UpdateUserNameUseCase

    private lateinit var updateUserNameHttpEndpointMock: UpdateUserNameHttpEndpoint
    private lateinit var usersCacheMock: UsersCache
    private lateinit var eventBusPosterMock: EventBusPoster

    private val stringCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
    private val userCaptor: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
    private val objectCaptor: ArgumentCaptor<Any> = ArgumentCaptor.forClass(Any::class.java)

    companion object {

        private const val USER_ID = "userId"
        private const val USER_NAME = "user"
        private const val DEFAULT_VALUE: String = ""
    }

    @Before
    fun setUp() {
        updateUserNameHttpEndpointMock = mock(UpdateUserNameHttpEndpoint::class.java)
        usersCacheMock = mock(UsersCache::class.java)
        eventBusPosterMock = mock(EventBusPoster::class.java)

        sut = UpdateUserNameUseCase(
            updateUserNameHttpEndpointMock,
            usersCacheMock,
            eventBusPosterMock
        )

        success()
    }

    @Test
    fun updateUserName_success_userIdAndUserNamePassedToEndpoint() {
        sut.updateUserName(USER_ID, USER_NAME)
        verify(updateUserNameHttpEndpointMock).updateUserName(
            capture(stringCaptor),
            capture(stringCaptor)
        )
        assertEquals(stringCaptor.allValues[0], USER_ID)
        assertEquals(stringCaptor.allValues[1], USER_NAME)
    }

    @Test
    fun updateUserName_success_userCached() {
        sut.updateUserName(USER_ID, USER_NAME)
        verify(usersCacheMock).cacheUser(capture(userCaptor))
        assertEquals(userCaptor.value.id, USER_ID)
        assertEquals(userCaptor.value.fullName, USER_NAME)
    }

    @Test
    fun updateUserName_generalError_notInteractedWithUsersCache() {
        generalError()
        sut.updateUserName(USER_ID, USER_NAME)
        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    fun updateUserName_authError_notInteractedWithUsersCache() {
        authError()
        sut.updateUserName(USER_ID, USER_NAME)
        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    fun updateUserName_serverError_notInteractedWithUsersCache() {
        serverError()
        sut.updateUserName(USER_ID, USER_NAME)
        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    fun updateUserName_success_userDetailsChangedEventPosted() {
        sut.updateUserName(USER_ID, USER_NAME)
        verify(eventBusPosterMock).postEvent(capture(objectCaptor))
        assert(objectCaptor.value is UserDetailsChangedEvent)
    }

    @Test
    fun updateUserName_generalError_notInteractedWithEventPoster() {
        generalError()
        sut.updateUserName(USER_ID, USER_NAME)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun updateUserName_authError_notInteractedWithEventPoster() {
        authError()
        sut.updateUserName(USER_ID, USER_NAME)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun updateUserName_serverError_notInteractedWithEventPoster() {
        serverError()
        sut.updateUserName(USER_ID, USER_NAME)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun updateUserName_success_successReturned() {
        val result = sut.updateUserName(USER_ID, USER_NAME)
        assertEquals(result, UseCaseResult.SUCCESS)
    }

    @Test
    fun updateUserName_generalError_failureReturned() {
        generalError()
        val result = sut.updateUserName(USER_ID, USER_NAME)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun updateUserName_authError_failureReturned() {
        authError()
        val result = sut.updateUserName(USER_ID, USER_NAME)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun updateUserName_serverError_failureReturned() {
        serverError()
        val result = sut.updateUserName(USER_ID, USER_NAME)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun updateUserName_networkError_networkReturned() {
        networkError()
        val result = sut.updateUserName(USER_ID, USER_NAME)
        assertEquals(result, UseCaseResult.NETWORK_ERROR)
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun success() {
        `when`(
            updateUserNameHttpEndpointMock.updateUserName(any(), any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USER_NAME)
        )
    }

    private fun generalError() {
        `when`(
            updateUserNameHttpEndpointMock.updateUserName(any(), any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.GENERAL_ERROR, DEFAULT_VALUE, DEFAULT_VALUE)
        )
    }

    private fun authError() {
        `when`(
            updateUserNameHttpEndpointMock.updateUserName(any(), any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.AUTH_ERROR, DEFAULT_VALUE, DEFAULT_VALUE)
        )
    }

    private fun serverError() {
        `when`(
            updateUserNameHttpEndpointMock.updateUserName(any(), any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.SERVER_ERROR, DEFAULT_VALUE, DEFAULT_VALUE)
        )
    }

    private fun networkError() {
        `when`(
            updateUserNameHttpEndpointMock.updateUserName(any(), any())
        ).doAnswer {
            throw NetworkErrorException()
        }
    }
}