package com.ivettevaldez.unittesting.mockitofundamentals.exercise5

import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent
import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.networking.UpdateUserNameHttpEndpoint
import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.networking.UpdateUserNameHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.mockitofundamentals.exercise5.networking.UpdateUserNameHttpEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.eventbus.EventBusPoster
import com.ivettevaldez.unittesting.testdoublesfundamentals.example4.loginhttpendpoint.NetworkErrorException
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.users.User
import com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.users.UsersCache

class UpdateUserNameUseCase(
    private val updateUserNameHttpEndpoint: UpdateUserNameHttpEndpoint,
    private val usersCache: UsersCache,
    private val eventBusPoster: EventBusPoster
) {

    enum class UseCaseResult {

        SUCCESS, FAILURE, NETWORK_ERROR
    }

    fun updateUserName(userId: String, userName: String): UseCaseResult {
        return try {
            val endpointResult = updateUserNameHttpEndpoint.updateUserName(userId, userName)
            if (endpointResult.isSuccessful()) {
                val user = User(endpointResult.userId, endpointResult.userName)
                usersCache.cacheUser(user)
                eventBusPoster.postEvent(
                    UserDetailsChangedEvent(user)
                )

                UseCaseResult.SUCCESS
            } else {
                UseCaseResult.FAILURE
            }
        } catch (ex: NetworkErrorException) {
            UseCaseResult.NETWORK_ERROR
        }
    }

    private fun EndpointResult.isSuccessful(): Boolean =
        this.status === EndpointResultStatus.SUCCESS
}