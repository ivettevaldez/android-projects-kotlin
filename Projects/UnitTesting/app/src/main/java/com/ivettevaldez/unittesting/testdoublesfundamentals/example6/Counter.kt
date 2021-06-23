package com.ivettevaldez.unittesting.testdoublesfundamentals.example6

object Counter {

    val instance = this

    private var total = 0

    fun add() {
        total++
    }

    fun add(value: Int) {
        total += value
    }

    fun total(): Int = total
}