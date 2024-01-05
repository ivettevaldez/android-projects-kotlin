package com.ivettevaldez.unittesting.unittestingfundamentals.example3

class Interval(val start: Int, val end: Int) {

    init {

        if (start >= end) {
            throw IllegalArgumentException("Invalid interval range")
        }
    }
}