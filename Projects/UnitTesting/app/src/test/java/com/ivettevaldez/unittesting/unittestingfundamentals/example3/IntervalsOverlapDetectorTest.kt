package com.ivettevaldez.unittesting.unittestingfundamentals.example3

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IntervalsOverlapDetectorTest {

    private lateinit var sut: IntervalsOverlapDetector

    @Before
    fun setUp() {
        sut = IntervalsOverlapDetector()
    }

    @Test
    fun isOverlap_interval1beforeInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(8, 12)
        val result = sut.isOverlap(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isOverlap_interval1OverlapsInterval2OnStart_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(3, 12)
        val result = sut.isOverlap(interval1, interval2)
        assertTrue(result)
    }

    @Test
    fun isOverlap_interval1ContainedWithinInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-2, 6)
        val result = sut.isOverlap(interval1, interval2)
        assertTrue(result)
    }

    @Test
    fun isOverlap_interval1ContainsInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(0, 3)
        val result = sut.isOverlap(interval1, interval2)
        assertTrue(result)
    }

    @Test
    fun isOverlap_interval1OverlapsInterval2OnEnd_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-4, 4)
        val result = sut.isOverlap(interval1, interval2)
        assertTrue(result)
    }

    @Test
    fun isOverlap_interval1AfterInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-10, -3)
        val result = sut.isOverlap(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isOverlap_interval1BeforeAndAdjacentInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(5, 8)
        val result = sut.isOverlap(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isOverlap_interval1AfterAndAdjacentInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-3, -1)
        val result = sut.isOverlap(interval1, interval2)
        assertFalse(result)
    }

    @Test
    fun isOverlap_interval1SameThanInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-1, 5)
        val result = sut.isOverlap(interval1, interval2)
        assertTrue(result)
    }
}