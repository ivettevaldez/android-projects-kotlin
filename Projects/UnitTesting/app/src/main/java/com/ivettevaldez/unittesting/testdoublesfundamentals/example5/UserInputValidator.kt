package com.ivettevaldez.unittesting.testdoublesfundamentals.example5

class UserInputValidator {

    fun isValidFullName(fullName: String): Boolean {
        return FullNameValidator.isValidFullName(fullName)
    }

    fun isValidUserName(userName: String): Boolean {
        return UserNameValidator.isValidUserName(userName)
    }
}