package com.ivettevaldez.unittesting.unittestingfundamentals.exercise3

import com.ivettevaldez.unittesting.unittestingfundamentals.example3.Interval
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IntervalAdjacencyDetectorTest {

    private lateinit var sut: IntervalAdjacencyDetector

    @Before
    fun setUp() {
        sut = IntervalAdjacencyDetector()
    }

    @Test
    fun isAdjacent_interval1beforeInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(8, 12)
        val result = sut.isAdjacent(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isAdjacent_interval1BeforeAndAdjacentInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(5, 8)
        val result = sut.isAdjacent(interval1, interval2)
        assertTrue(result)
    }

    @Test
    fun isAdjacent_interval1OverlapsInterval2OnStart_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(3, 12)
        val result = sut.isAdjacent(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isAdjacent_interval1ContainedWithinInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-2, 6)
        val result = sut.isAdjacent(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isAdjacent_interval1ContainsInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(0, 3)
        val result = sut.isAdjacent(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isOverlap_interval1EqualsInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-1, 5)
        val result = sut.isAdjacent(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isAdjacent_interval1OverlapsInterval2OnEnd_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-4, 4)
        val result = sut.isAdjacent(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isAdjacent_interval1AfterAndAdjacentInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-3, -1)
        val result = sut.isAdjacent(interval1, interval2)
        assertTrue(result)
    }

    @Test
    fun isAdjacent_interval1AfterInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-10, -3)
        val result = sut.isAdjacent(interval1, interval2)
        assertFalse(result)
    }
}