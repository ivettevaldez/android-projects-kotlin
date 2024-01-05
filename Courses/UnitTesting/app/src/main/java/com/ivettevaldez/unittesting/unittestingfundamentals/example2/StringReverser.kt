package com.ivettevaldez.unittesting.unittestingfundamentals.example2

class StringReverser {

    fun reverse(string: String): String {
        val stringBuilder = StringBuilder()
        val charArray = string.toCharArray()

        for (i in string.length - 1 downTo 0) {
            stringBuilder.append(charArray[i])
        }

        return stringBuilder.toString()
    }
}