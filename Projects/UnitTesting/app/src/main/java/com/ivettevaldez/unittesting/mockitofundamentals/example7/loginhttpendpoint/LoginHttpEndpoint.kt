package com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint

interface LoginHttpEndpoint {

    fun login(userName: String, password: String): EndpointResult

    enum class EndpointResultStatus {

        SUCCESS, AUTH_ERROR, SERVER_ERROR, GENERAL_ERROR
    }

    class EndpointResult(val status: EndpointResultStatus, val authToken: String)
}