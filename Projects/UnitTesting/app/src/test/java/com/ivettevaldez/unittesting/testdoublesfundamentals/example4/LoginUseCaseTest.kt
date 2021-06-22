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

class LoginUseCaseTest {

    private lateinit var sut: LoginUseCase
    
    private lateinit var loginHttpEndpoint: LoginHttpEndpointTd
    private lateinit var authTokenCache: AuthTokenCacheTd
    private lateinit var eventBusPoster: EventBusPosterTd

    companion object {

        private const val USER_NAME = "user"
        private const val PASSWORD = "password"

        private const val AUTH_TOKEN = "authToken"
        private const val DEFAULT_AUTH_TOKEN = ""
    }

    @Before
    fun setUp() {
        loginHttpEndpoint = LoginHttpEndpointTd()
        authTokenCache = AuthTokenCacheTd()
        eventBusPoster = EventBusPosterTd()

        sut = LoginUseCase(
            loginHttpEndpoint,
            authTokenCache,
            eventBusPoster
        )
    }

    @Test
    fun login_success_userAndPasswordPassedToEndpoint() {
        sut.login(USER_NAME, PASSWORD)
        assertEquals(loginHttpEndpoint.userName, USER_NAME)
        assertEquals(loginHttpEndpoint.password, PASSWORD)
    }

    @Test
    fun login_success_authTokenCached() {
        sut.login(USER_NAME, PASSWORD)
        assertEquals(authTokenCache.authToken, AUTH_TOKEN)
    }

    @Test
    fun login_generalError_authTokenNotCached() {
        loginHttpEndpoint.isGeneralError = true
        sut.login(USER_NAME, PASSWORD)
        assertEquals(authTokenCache.authToken, DEFAULT_AUTH_TOKEN)
    }

    @Test
    fun login_authError_authTokenNotCached() {
        loginHttpEndpoint.isAuthError = true
        sut.login(USER_NAME, PASSWORD)
        assertEquals(authTokenCache.authToken, DEFAULT_AUTH_TOKEN)
    }

    @Test
    fun login_serverError_authTokenNotCached() {
        loginHttpEndpoint.isServerError = true
        sut.login(USER_NAME, PASSWORD)
        assertEquals(authTokenCache.authToken, DEFAULT_AUTH_TOKEN)
    }

    @Test
    fun login_success_loggedInEventPosted() {
        sut.login(USER_NAME, PASSWORD)
        assert(eventBusPoster.event is LoggedInEvent)
    }

    @Test
    fun login_generalError_notInteractedWithEventBusPoster() {
        loginHttpEndpoint.isGeneralError = true
        sut.login(USER_NAME, PASSWORD)
        assertEquals(eventBusPoster.interactionsCount, 0)
    }

    @Test
    fun login_authError_notInteractedWithEventBusPoster() {
        loginHttpEndpoint.isAuthError = true
        sut.login(USER_NAME, PASSWORD)
        assertEquals(eventBusPoster.interactionsCount, 0)
    }

    @Test
    fun login_serverError_notInteractedWithEventBusPoster() {
        loginHttpEndpoint.isServerError = true
        sut.login(USER_NAME, PASSWORD)
        assertEquals(eventBusPoster.interactionsCount, 0)
    }

    @Test
    fun login_success_successReturned() {
        val result: UseCaseResult = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.SUCCESS)
    }

    @Test
    fun login_generalError_failureReturned() {
        loginHttpEndpoint.isGeneralError = true
        val result: UseCaseResult = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun login_authError_failureReturned() {
        loginHttpEndpoint.isAuthError = true
        val result: UseCaseResult = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun login_serverError_failureReturned() {
        loginHttpEndpoint.isServerError = true
        val result: UseCaseResult = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun login_networkError_failureReturned() {
        loginHttpEndpoint.isNetworkError = true
        val result: UseCaseResult = sut.login(USER_NAME, PASSWORD)
        assertEquals(result, UseCaseResult.NETWORK_ERROR)
    }

    // --------------------------------------------------------------------------------------------
    // HELPER CLASSES
    // --------------------------------------------------------------------------------------------

    private class LoginHttpEndpointTd : LoginHttpEndpoint {

        lateinit var userName: String
        lateinit var password: String

        var isGeneralError: Boolean = false
        var isAuthError: Boolean = false
        var isServerError: Boolean = false
        var isNetworkError: Boolean = false

        override fun loginSync(
            userName: String,
            password: String
        ): EndpointResult {

            this.userName = userName
            this.password = password

            return when {
                isGeneralError -> {
                    EndpointResult(EndpointResultStatus.GENERAL_ERROR, DEFAULT_AUTH_TOKEN)
                }
                isAuthError -> {
                    EndpointResult(EndpointResultStatus.AUTH_ERROR, DEFAULT_AUTH_TOKEN)
                }
                isServerError -> {
                    EndpointResult(EndpointResultStatus.SERVER_ERROR, DEFAULT_AUTH_TOKEN)
                }
                isNetworkError -> {
                    throw NetworkErrorException()
                }
                else -> {
                    EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN)
                }
            }
        }
    }

    private class AuthTokenCacheTd : AuthTokenCache {

        var authToken: String = DEFAULT_AUTH_TOKEN

        override fun cacheAuthToken(authToken: String) {
            this.authToken = authToken
        }
    }

    private class EventBusPosterTd : EventBusPoster {

        lateinit var event: Any

        var interactionsCount: Int = 0

        override fun postEvent(event: Any) {
            this.event = event
            this.interactionsCount++
        }
    }
}