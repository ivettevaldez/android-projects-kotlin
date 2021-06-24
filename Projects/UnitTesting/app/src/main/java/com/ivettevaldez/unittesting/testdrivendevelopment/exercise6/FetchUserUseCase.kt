package com.ivettevaldez.unittesting.testdrivendevelopment.exercise6

import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.NetworkErrorException
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.IFetchUserUseCase.UseCaseResult
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.IFetchUserUseCase.UseCaseResultStatus
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.users.User
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.users.UsersCache

class FetchUserUseCase(
    private val fetchUserEndpoint: FetchUserHttpEndpoint,
    private val usersCache: UsersCache
) : IFetchUserUseCase {

    override fun fetchUser(userId: String): UseCaseResult {
        var user = usersCache.getUser(userId)

        return if (user != null) {
            UseCaseResult(UseCaseResultStatus.SUCCESS, user)
        } else {
            return try {
                val result = fetchUserEndpoint.fetchUser(userId)

                when (result.status) {
                    EndpointResultStatus.SUCCESS -> {
                        user = cacheUser(result)
                        UseCaseResult(UseCaseResultStatus.SUCCESS, user)
                    }
                    EndpointResultStatus.AUTH_ERROR -> {
                        UseCaseResult(UseCaseResultStatus.FAILURE, null)
                    }
                    EndpointResultStatus.GENERAL_ERROR -> {
                        UseCaseResult(UseCaseResultStatus.FAILURE, null)
                    }
                    EndpointResultStatus.SERVER_ERROR -> {
                        UseCaseResult(UseCaseResultStatus.FAILURE, null)
                    }
                    else -> throw RuntimeException("Invalid status: ${result.status}")
                }
            } catch (ex: NetworkErrorException) {
                UseCaseResult(UseCaseResultStatus.NETWORK_ERROR, null)
            }
        }
    }

    private fun cacheUser(result: EndpointResult): User {
        val user = User(
            result.userId,
            result.userName
        )

        usersCache.cacheUser(user)

        return user
    }
}