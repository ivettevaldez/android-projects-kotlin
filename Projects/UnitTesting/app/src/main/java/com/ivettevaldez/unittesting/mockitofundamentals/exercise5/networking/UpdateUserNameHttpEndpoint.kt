package com.ivettevaldez.unittesting.mockitofundamentals.exercise5.networking

interface UpdateUserNameHttpEndpoint {

    enum class EndpointResultStatus {

        SUCCESS, GENERAL_ERROR, AUTH_ERROR, SERVER_ERROR
    }

    fun updateUserName(userId: String, userName: String): EndpointResult

    class EndpointResult(val status: EndpointResultStatus, val userId: String, val userName: String)
}