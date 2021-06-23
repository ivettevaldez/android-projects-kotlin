package com.ivettevaldez.unittesting.testdoublesfundamentals.example5

object UserNameValidator {

    fun isValidUserName(userName: String): Boolean {
        // This sleep mimics network request that checks whether username is free,
        // but fails due to absence of network connection.
        return try {
            Thread.sleep(1000L)
            throw RuntimeException("No network exception")
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
            false
        }
    }
}