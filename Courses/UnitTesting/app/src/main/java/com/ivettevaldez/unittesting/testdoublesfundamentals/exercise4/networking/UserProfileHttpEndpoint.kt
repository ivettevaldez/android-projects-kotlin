package com.ivettevaldez.unittesting.testdoublesfundamentals.exercise4.networking

interface UserProfileHttpEndpoint {

    fun fetchUserProfile(userId: String): EndpointResult

    enum class EndpointResultStatus {

        SUCCESS, GENERAL_ERROR, AUTH_ERROR, SERVER_ERROR
    }

    class EndpointResult(
        val status: EndpointResultStatus,
        val id: String,
        val fullName: String,
        val photoUrl: String
    )
}