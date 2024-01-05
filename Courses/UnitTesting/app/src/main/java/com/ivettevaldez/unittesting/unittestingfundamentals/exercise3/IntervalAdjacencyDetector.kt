package com.ivettevaldez.unittesting.unittestingfundamentals.exercise3

import com.ivettevaldez.unittesting.unittestingfundamentals.example3.Interval

class IntervalAdjacencyDetector {

    fun isAdjacent(interval1: Interval, interval2: Interval): Boolean {
        return (interval1.start == interval2.end || interval1.end == interval2.start) &&
                !sameIntervals(interval1, interval2)
    }

    private fun sameIntervals(interval1: Interval, interval2: Interval): Boolean {
        return interval1.start == interval2.start && interval1.end == interval2.end
    }
}