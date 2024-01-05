package com.ivettevaldez.unittesting.testdrivendevelopment.example10.networking

interface IPingServerHttpEndpoint {

    enum class EndpointResult {

        SUCCESS,
        GENERAL_ERROR,
        NETWORK_ERROR
    }

    fun pingServer(): EndpointResult
}