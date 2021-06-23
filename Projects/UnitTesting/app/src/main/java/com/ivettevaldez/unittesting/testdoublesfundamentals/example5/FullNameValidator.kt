package com.ivettevaldez.unittesting.testdoublesfundamentals.example5

object FullNameValidator {

    fun isValidFullName(fullName: String): Boolean {
        return fullName.isNotBlank()
    }
}