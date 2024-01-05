package com.ivettevaldez.unittesting.testdoublesfundamentals.example6

class FitnessTracker {

    companion object {

        const val RUN_STEPS_FACTOR = 2
    }

    fun step() {
        Counter.instance.add()
    }

    fun runStep() {
        Counter.instance.add(RUN_STEPS_FACTOR)
    }

    fun getTotalSteps() = Counter.instance.total()
}