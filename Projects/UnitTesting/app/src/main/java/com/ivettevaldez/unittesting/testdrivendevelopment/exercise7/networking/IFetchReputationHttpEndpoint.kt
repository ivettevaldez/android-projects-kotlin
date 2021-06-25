package com.ivettevaldez.unittesting.testdrivendevelopment.exercise7.networking

interface IFetchReputationHttpEndpoint {

    enum class EndpointStatus {

        SUCCESS,
        GENERAL_ERROR,
        NETWORK_ERROR
    }

    fun fetchReputation(): EndpointResult

    class EndpointResult(val status: EndpointStatus, val reputation: Int)
}