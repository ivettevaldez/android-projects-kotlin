package com.ivettevaldez.unittesting.testdoublesfundamentals.example4

import com.ivettevaldez.unittesting.mockitofundamentals.example7.authtokencache.AuthTokenCache
import com.ivettevaldez.unittesting.mockitofundamentals.example7.eventbus.EventBusPoster
import com.ivettevaldez.unittesting.mockitofundamentals.example7.eventbus.LoggedInEvent
import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.LoginHttpEndpoint
import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.LoginHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.LoginHttpEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.NetworkErrorException

class LoginUseCase(
    private val loginHttpEndpoint: LoginHttpEndpoint,
    private val authTokenCache: AuthTokenCache,
    private val eventBusPoster: EventBusPoster
) {

    enum class UseCaseResult {

        SUCCESS, FAILURE, NETWORK_ERROR
    }

    fun login(userName: String, password: String): UseCaseResult {
        return try {
            val result: EndpointResult = loginHttpEndpoint.login(userName, password)

            if (result.isSuccessful()) {
                authTokenCache.cacheAuthToken(result.authToken)
                eventBusPoster.postEvent(LoggedInEvent())

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