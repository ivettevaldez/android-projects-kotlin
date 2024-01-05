package com.ivettevaldez.unittesting.testdrivendevelopment.exercise6

import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.NetworkErrorException
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.IFetchUserUseCase.UseCaseResultStatus
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.users.User
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.users.UsersCache
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.never

@RunWith(MockitoJUnitRunner::class)
class FetchUserUseCaseTest {

    private lateinit var sut: FetchUserUseCase

    @Mock
    private lateinit var fetchUserHttpEndpoint: FetchUserHttpEndpoint

    @Mock
    private lateinit var usersCacheMock: UsersCache

    private val user: User = User(USER_ID, USER_NAME)

    private val stringCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
    private val userCaptor: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)

    companion object {

        private const val USER_ID: String = "userId"
        private const val USER_NAME: String = "userName"
        private const val DEFAULT_EMPTY_VALUE: String = ""
    }

    @Before
    fun setUp() {
        sut = FetchUserUseCase(
            fetchUserHttpEndpoint,
            usersCacheMock
        )

        userNotInCache()
        success()
    }

    @Test
    fun fetchUser_notInCache_correctUserIdPassedToEndpoint() {
        // Act
        sut.fetchUser(USER_ID)
        // Assert
        verify(fetchUserHttpEndpoint).fetchUser(capture(stringCaptor))
        assertEquals(stringCaptor.value, USER_ID)
    }

    @Test
    fun fetchUser_notInCacheSuccess_successStatusReturned() {
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.status, UseCaseResultStatus.SUCCESS)
    }

    @Test
    fun fetchUser_notInCacheSuccess_correctUserReturned() {
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.user, user)
    }

    @Test
    fun fetchUser_notInCacheSuccess_userCached() {
        // Act
        sut.fetchUser(USER_ID)
        // Assert
        verify(usersCacheMock).cacheUser(capture(userCaptor))
        assertEquals(userCaptor.value, user)
    }

    @Test
    fun fetchUser_notInCacheGeneralError_failureStatusReturned() {
        // Arrange
        generalError()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.status, UseCaseResultStatus.FAILURE)
    }

    @Test
    fun fetchUser_notInCacheGeneralError_nullUserReturned() {
        // Arrange
        generalError()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.user, null)
    }

    @Test
    fun fetchUser_notInCacheGeneralError_nothingCached() {
        // Arrange
        generalError()
        // Act
        sut.fetchUser(USER_ID)
        // Assert
        verify(usersCacheMock, never()).cacheUser(capture(userCaptor))
    }

    @Test
    fun fetchUser_notInCacheAuthError_failureStatusReturned() {
        // Arrange
        authError()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.status, UseCaseResultStatus.FAILURE)
    }

    @Test
    fun fetchUser_notInCacheAuthError_nullUserReturned() {
        // Arrange
        authError()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.user, null)
    }

    @Test
    fun fetchUser_notInCacheAuthError_nothingCached() {
        // Arrange
        authError()
        // Act
        sut.fetchUser(USER_ID)
        // Assert
        verify(usersCacheMock, never()).cacheUser(capture(userCaptor))
    }

    @Test
    fun fetchUser_notInCacheServerError_failureStatusReturned() {
        // Arrange
        serverError()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.status, UseCaseResultStatus.FAILURE)
    }

    @Test
    fun fetchUser_notInCacheServerError_nullUserReturned() {
        // Arrange
        serverError()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.user, null)
    }

    @Test
    fun fetchUser_notInCacheServerError_nothingCached() {
        // Arrange
        serverError()
        // Act
        sut.fetchUser(USER_ID)
        // Assert
        verify(usersCacheMock, never()).cacheUser(capture(userCaptor))
    }

    @Test
    fun fetchUser_notInCacheNetworkError_networkErrorStatusReturned() {
        // Arrange
        networkError()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.status, UseCaseResultStatus.NETWORK_ERROR)
    }

    @Test
    fun fetchUser_notInCacheNetworkError_nullUserReturned() {
        // Arrange
        networkError()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.user, null)
    }

    @Test
    fun fetchUser_notInCacheNetworkError_nothingCached() {
        // Arrange
        networkError()
        // Act
        sut.fetchUser(USER_ID)
        // Assert
        verify(usersCacheMock, never()).cacheUser(capture(userCaptor))
    }

    @Test
    fun fetchUser_correctUserIdPassedToUsersCache() {
        // Act
        sut.fetchUser(USER_ID)
        // Assert
        verify(usersCacheMock).getUser(capture(stringCaptor))
        assertEquals(stringCaptor.value, USER_ID)
    }

    @Test
    fun fetchUser_inCache_successStatusReturned() {
        // Arrange
        userInCache()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.status, UseCaseResultStatus.SUCCESS)
    }

    @Test
    fun fetchUser_inCache_cachedUserReturned() {
        // Arrange
        userInCache()
        // Act
        val result = sut.fetchUser(USER_ID)
        // Assert
        assertEquals(result.user, user)
    }

    @Test
    fun fetchUser_inCache_notInteractedWithEndpoint() {
        // Arrange
        userInCache()
        // Act
        sut.fetchUser(USER_ID)
        // Assert
        verify(fetchUserHttpEndpoint, never()).fetchUser(capture(stringCaptor))
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun userNotInCache() {
        `when`(
            usersCacheMock.getUser(any())
        ).thenReturn(null)
    }

    private fun userInCache() {
        `when`(
            usersCacheMock.getUser(any())
        ).thenReturn(user)
    }

    private fun success() {
        `when`(
            fetchUserHttpEndpoint.fetchUser(any())
        ).thenReturn(
            EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USER_NAME)
        )
    }

    private fun generalError() {
        `when`(
            fetchUserHttpEndpoint.fetchUser(any())
        ).thenReturn(
            EndpointResult(
                EndpointResultStatus.GENERAL_ERROR,
                DEFAULT_EMPTY_VALUE,
                DEFAULT_EMPTY_VALUE
            )
        )
    }

    private fun authError() {
        `when`(
            fetchUserHttpEndpoint.fetchUser(any())
        ).thenReturn(
            EndpointResult(
                EndpointResultStatus.AUTH_ERROR,
                DEFAULT_EMPTY_VALUE,
                DEFAULT_EMPTY_VALUE
            )
        )
    }

    private fun serverError() {
        `when`(
            fetchUserHttpEndpoint.fetchUser(any())
        ).thenReturn(
            EndpointResult(
                EndpointResultStatus.SERVER_ERROR,
                DEFAULT_EMPTY_VALUE,
                DEFAULT_EMPTY_VALUE
            )
        )
    }

    private fun networkError() {
        `when`(
            fetchUserHttpEndpoint.fetchUser(any())
        ).doAnswer {
            throw NetworkErrorException()
        }
    }
}