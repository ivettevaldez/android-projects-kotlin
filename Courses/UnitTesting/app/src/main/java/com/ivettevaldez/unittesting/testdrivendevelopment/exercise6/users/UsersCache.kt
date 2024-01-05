package com.ivettevaldez.unittesting.testdrivendevelopment.exercise6.users

interface UsersCache {

    fun cacheUser(user: User)

    fun getUser(userId: String): User?
}