package com.ivettevaldez.unittesting.unittestingfundamentals.example3

class IntervalsOverlapDetector {

    fun isOverlap(interval1: Interval, interval2: Interval): Boolean {
        return (interval1.end > interval2.start && interval1.start < interval2.end) ||
                sameIntervals(interval1, interval2)
    }

    private fun sameIntervals(interval1: Interval, interval2: Interval): Boolean {
        return interval1.start == interval2.start && interval1.end == interval2.end
    }
}