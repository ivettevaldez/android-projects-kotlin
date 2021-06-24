package com.ivettevaldez.unittesting.testdrivendevelopment.exercise6

import com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.users.User

interface IFetchUserUseCase {

    enum class UseCaseResultStatus {

        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    fun fetchUser(userId: String): UseCaseResult

    data class UseCaseResult(val status: UseCaseResultStatus, val user: User?)
}