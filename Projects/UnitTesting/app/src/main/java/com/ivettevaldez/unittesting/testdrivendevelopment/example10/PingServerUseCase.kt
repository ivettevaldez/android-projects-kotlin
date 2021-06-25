package com.ivettevaldez.unittesting.testdrivendevelopment.example10

import com.ivettevaldez.unittesting.testdrivendevelopment.example10.networking.IPingServerHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.example10.networking.IPingServerHttpEndpoint.EndpointResult

class PingServerUseCase(private val pingServerHttpEndpoint: IPingServerHttpEndpoint) {

    enum class UseCaseResult {

        SUCCESS,
        FAILURE
    }

    fun pingServer(): UseCaseResult = when (val result = pingServerHttpEndpoint.pingServer()) {
        EndpointResult.GENERAL_ERROR -> UseCaseResult.FAILURE
        EndpointResult.NETWORK_ERROR -> UseCaseResult.FAILURE
        EndpointResult.SUCCESS -> UseCaseResult.SUCCESS
        else -> throw RuntimeException("Invalid result: $result")
    }
}