package com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.networking

interface FetchUserHttpEndpoint {

    enum class EndpointResultStatus {

        SUCCESS,
        GENERAL_ERROR,
        AUTH_ERROR,
        SERVER_ERROR
    }

    data class EndpointResult(

        val status: EndpointResultStatus,
        val userId: String,
        val userName: String
    )

    fun fetchUser(userId: String): EndpointResult
}