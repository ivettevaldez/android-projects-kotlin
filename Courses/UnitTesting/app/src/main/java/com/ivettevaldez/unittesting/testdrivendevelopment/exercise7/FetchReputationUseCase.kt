package com.ivettevaldez.unittesting.testdrivendevelopment.exercise7

import com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.networking.IFetchReputationHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.networking.IFetchReputationHttpEndpoint.EndpointResult
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.networking.IFetchReputationHttpEndpoint.EndpointStatus

class FetchReputationUseCase(private val fetchReputationHttpEndpoint: IFetchReputationHttpEndpoint) {

    enum class UseCaseStatus {

        SUCCESS,
        FAILURE
    }

    fun fetchReputation(): UseCaseResult {
        val result: EndpointResult = fetchReputationHttpEndpoint.fetchReputation()

        return when (result.status) {
            EndpointStatus.GENERAL_ERROR,
            EndpointStatus.NETWORK_ERROR -> {
                UseCaseResult(UseCaseStatus.FAILURE, result.reputation)
            }
            else -> {
                UseCaseResult(UseCaseStatus.SUCCESS, result.reputation)
            }
        }
    }

    class UseCaseResult(val status: UseCaseStatus, val reputation: Int)
}