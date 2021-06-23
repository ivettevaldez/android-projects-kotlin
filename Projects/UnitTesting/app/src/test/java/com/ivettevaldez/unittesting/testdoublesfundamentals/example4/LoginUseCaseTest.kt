package com.ivettevaldez.unittesting.testdoublesfundamentals.example4

import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.LoginUseCase.UseCaseResult
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.authtokencache.AuthTokenCache
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.eventbus.EventBusPoster
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.eventbus.LoggedInEvent
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.loginhttpendpoint.LoginHttpEndpoint
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.loginhttpendpoint.LoginHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.loginhttpendpoint.LoginHttpEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.loginhttpendpoint.NetworkErrorException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer

class LoginUseCaseTest {

    private lateinit var sut: LoginUseCase

    private lateinit var loginHttpEndpointMock: LoginHttpEndpoint
    private lateinit var authTokenCacheMock: AuthTokenCache
    private lateinit var eventBusPosterMock: EventBusPoster

    private val stringCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
    private val objectCaptor: ArgumentCaptor<Any> = ArgumentCaptor.forClass(Any::class.java)

    companion object {

        private const val USER_NAME = "user"
        private const val PASSWORD = "password"

        private const val AUTH_TOKEN = "authToken"
        private const val DEFAULT_AUTH_TOKEN = ""
    }

    @Before
    fun setUp() {
        loginHttpEndpointMock = mock(LoginHttpEndpoint::class.java)
        authTokenCacheMock = mock(AuthTokenCache::class.java)
        eventBusPosterMock = mock(EventBusPoster::class.java)

        sut = LoginUseCase(
            loginHttpEndpointMock,
            authTokenCacheMock,
            eventBusPosterMock
        )

        success()
    }

    @Test
    fun login_success_userAndPasswordPassedToEndpoint() {
        sut.login(USER_NAME, PASSWORD)
        verify(loginHttpEndpointMock).login(capture(stringCaptor), capture(stringCaptor))
        assertEquals(stringCaptor.allValues[0], USER_NAME)
        assertEquals(stringCaptor.allValues[1], PASSWORD)
    }

    @Test
    fun login_success_authTokenCached() {
        sut.login(USER_NAME, PASSWORD)
        verify(authTokenCacheMock).cacheAuthToken(capture(stringCaptor))
        assertEquals(stringCaptor.value, AUTH_TOKEN)
    }

    @Test
    fun login_generalError_notInteractedWithAuthTokenCache() {
        generalError()
        sut.login(USER_NAME, PASSWORD)
        verifyNoMoreInteractions(authTokenCacheMock)
    }

    @Test
    fun login_authError_notInteractedWithAuthTokenCache() {
        authError()
        sut.login(USER_NAME, PASSWORD)
        verifyNoMoreInteractions(authTokenCacheMock)
    }

    @Test
    fun login_serverError_notInteractedWithAuthTokenCache() {
        serverError()
        sut.login(USER_NAME, PASSWORD)
        verifyNoMoreInteractions(authTokenCacheMock)
    }

    @Test
    fun login_success_loggedInEventPosted() {
        sut.login(USER_NAME, PASSWORD)
        verify(eventBusPosterMock).postEvent(capture(objectCaptor))
        assert(objectCaptor.value is LoggedInEvent)
    }

    @Test
    fun login_generalError_notInteractedWithEventBusPoster() {
        generalError()
        sut.login(USER_NAME, PASSWORD)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun login_authError_notInteractedWithEventBusPoster() {
        authError()
        sut.login(USER_NAME, PASSWORD)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun login_serverError_notInteractedWithEventBusPoster() {
        serverError()
        sut.login(USER_NAME, PASSWORD)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun login_success_successReturned() {
        val result = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.SUCCESS)
    }

    @Test
    fun login_generalError_failureReturned() {
        generalError()
        val result = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun login_authError_failureReturned() {
        authError()
        val result = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.FAILURE)

    }

    @Test
    fun login_serverError_failureReturned() {
        serverError()
        val result = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun login_networkError_networkErrorReturned() {
        networkError()
        val result = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.NETWORK_ERROR)
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun success() {
        `when`(
            loginHttpEndpointMock.login(any(), any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN)
        )
    }

    private fun generalError() {
        `when`(
            loginHttpEndpointMock.login(any(), any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.GENERAL_ERROR, DEFAULT_AUTH_TOKEN)
        )
    }

    private fun authError() {
        `when`(
            loginHttpEndpointMock.login(any(), any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.AUTH_ERROR, DEFAULT_AUTH_TOKEN)
        )
    }

    private fun serverError() {
        `when`(
            loginHttpEndpointMock.login(any(), any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.SERVER_ERROR, DEFAULT_AUTH_TOKEN)
        )
    }

    private fun networkError() {
        `when`(
            loginHttpEndpointMock.login(any(), any())
        ).doAnswer {
            throw NetworkErrorException()
        }
    }
}