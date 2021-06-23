package com.ivettevaldez.unittesting.testdoublesfundamentals.example6

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FitnessTrackerTest {

    private lateinit var sut: FitnessTracker

    @Before
    fun setUp() {
        sut = FitnessTracker()
    }

    @Test
    fun step_totalIncremented() {
        sut.step()
        assertEquals(sut.getTotalSteps(), 1)
    }

    @Test
    fun runStep_totalIncrementedByCorrectRatio() {
        sut.runStep()
        assertEquals(sut.getTotalSteps(), FitnessTracker.RUN_STEPS_FACTOR)
    }
}