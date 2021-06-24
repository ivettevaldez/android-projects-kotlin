package com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4

import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.NetworkErrorException
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpoint
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.users.User
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.users.UsersCache

class FetchUserProfileUseCase(
    private val userProfileHttpEndpoint: UserProfileHttpEndpoint,
    private val usersCache: UsersCache
) {

    enum class UseCaseResult {

        SUCCESS, FAILURE, NETWORK_ERROR
    }

    fun fetchUserProfile(userId: String): UseCaseResult {
        return try {
            val result = userProfileHttpEndpoint.fetchUserProfile(userId)

            if (result.isSuccessful()) {
                val user = User(result.id, result.fullName, result.photoUrl)
                usersCache.cacheUser(user)

                UseCaseResult.SUCCESS
            } else {
                UseCaseResult.FAILURE
            }
        } catch (ex: NetworkErrorException) {
            UseCaseResult.NETWORK_ERROR
        }
    }

    private fun EndpointResult.isSuccessful() = this.status == EndpointResultStatus.SUCCESS
}
