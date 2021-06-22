package com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4

import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.loginhttpendpoint.NetworkErrorException
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.FetchUserProfileUseCase.UseCaseResult
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpoint
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.users.User
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.users.UsersCache
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FetchUserProfileUseCaseTest {

    private lateinit var sut: FetchUserProfileUseCase

    private lateinit var userProfileHttpEndpoint: UserProfileHttpEndpointTd
    private lateinit var usersCache: UsersCacheTd

    companion object {

        private const val DEFAULT_VALUE: String = ""

        private const val USER_ID: String = "userId"
        private const val USER_FULL_NAME: String = "Ivette Valdez"
        private const val USER_PHOTO_URL: String = "http://imageUrl.com"
    }

    @Before
    fun setUp() {
        userProfileHttpEndpoint = UserProfileHttpEndpointTd()
        usersCache = UsersCacheTd()

        sut = FetchUserProfileUseCase(
            userProfileHttpEndpoint,
            usersCache
        )
    }

    @Test
    fun fetchUserProfile_success_userIdPassedToEndpoint() {
        sut.fetchUserProfile(USER_ID)
        assertEquals(userProfileHttpEndpoint.userId, USER_ID)
    }

    @Test
    fun fetchUserProfile_success_userProfileCached() {
        sut.fetchUserProfile(USER_ID)

        val cachedUser = usersCache.getUser(USER_ID)
        assertEquals(cachedUser?.id, USER_ID)
        assertEquals(cachedUser?.fullName, USER_FULL_NAME)
        assertEquals(cachedUser?.photoUrl, USER_PHOTO_URL)
    }

    @Test
    fun fetch_generalError_userProfileNotCached() {
        userProfileHttpEndpoint.isGeneralError = true
        sut.fetchUserProfile(USER_ID)
        assertEquals(usersCache.getUser(USER_ID), null)
    }

    @Test
    fun fetchUserProfile_authError_userProfileNotCached() {
        userProfileHttpEndpoint.isAuthError = true
        sut.fetchUserProfile(USER_ID)
        assertEquals(usersCache.getUser(USER_ID), null)
    }

    @Test
    fun fetchUserProfile_serverError_userProfileNotCached() {
        userProfileHttpEndpoint.isServerError = true
        sut.fetchUserProfile(USER_ID)
        assertEquals(usersCache.getUser(USER_ID), null)
    }

    @Test
    fun fetchUserProfile_success_successReturned() {
        val result: UseCaseResult = sut.fetchUserProfile(USER_ID)
        assertEquals(result, UseCaseResult.SUCCESS)
    }

    @Test
    fun fetchUserProfile_generalError_failureReturned() {
        userProfileHttpEndpoint.isGeneralError = true
        val result: UseCaseResult = sut.fetchUserProfile(USER_ID)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun fetchUserProfile_authError_failureReturned() {
        userProfileHttpEndpoint.isAuthError = true
        val result: UseCaseResult = sut.fetchUserProfile(USER_ID)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun fetchUserProfile_serverError_failureReturned() {
        userProfileHttpEndpoint.isServerError = true
        val result: UseCaseResult = sut.fetchUserProfile(USER_ID)
        assertEquals(result, UseCaseResult.FAILURE)
    }

    @Test
    fun fetchUserProfile_networkError_failureReturned() {
        userProfileHttpEndpoint.isNetworkError = true
        val result: UseCaseResult = sut.fetchUserProfile(USER_ID)
        assertEquals(result, UseCaseResult.NETWORK_ERROR)
    }

    // --------------------------------------------------------------------------------------------
    // HELPER CLASSES
    // --------------------------------------------------------------------------------------------

    private class UserProfileHttpEndpointTd : UserProfileHttpEndpoint {

        var userId: String = DEFAULT_VALUE

        var isGeneralError: Boolean = false
        var isAuthError: Boolean = false
        var isServerError: Boolean = false
        var isNetworkError: Boolean = false

        override fun fetchUserProfile(userId: String): EndpointResult {
            this.userId = userId

            return when {
                isGeneralError -> EndpointResult(
                    EndpointResultStatus.GENERAL_ERROR, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE
                )
                isAuthError -> EndpointResult(
                    EndpointResultStatus.AUTH_ERROR, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE
                )
                isServerError -> EndpointResult(
                    EndpointResultStatus.SERVER_ERROR, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE
                )
                isNetworkError -> {
                    throw NetworkErrorException()
                }
                else -> EndpointResult(
                    EndpointResultStatus.SUCCESS, USER_ID, USER_FULL_NAME, USER_PHOTO_URL
                )
            }
        }
    }

    private class UsersCacheTd : UsersCache {

        var users: MutableList<User> = mutableListOf()

        override fun cacheUser(user: User) {
            val userExists = users.any { it.id == user.id }
            if (userExists) {
                users.remove(user)
            }
            users.add(user)
        }

        fun getUser(id: String): User? {
            return users.firstOrNull { it.id == id }
        }
    }
}