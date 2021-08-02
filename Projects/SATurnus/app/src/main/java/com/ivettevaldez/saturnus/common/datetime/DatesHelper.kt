package com.ivettevaldez.saturnus.common.datetime

/* ktlint-disable no-wildcard-imports */

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DatesHelper {

    private val objectTag: String = this::class.java.simpleName
    private val dateProvider = DateTimeProvider()

    fun String.calendar(): Calendar = try {
        val calendar = getCalendar()
        calendar.time = getUserFriendlyFormat().parse(this)!!
        calendar
    } catch (ex: ParseException) {
        if (this.isNotBlank()) {
            Log.e(objectTag, "@@@@@ Attempting to parse a date: $this", ex)
        }
        getCalendar()
    }

    fun Date.friendlyDate(): String = getUserFriendlyFormat().format(this)

    fun friendlyDate(year: Int, month: Int, day: Int): String {
        if (!year.validYear() || !month.validMonth() || !day.validDayOfMonth()) {
            Log.e(
                objectTag,
                "@@@@@ Invalid date - Year: $year, Month: $month, Day of month: $day"
            )
            return ""
        }
        val calendar = getCalendar()
        calendar.set(year, month, day)
        return getUserFriendlyFormat().format(calendar.time)
    }

    private fun getCalendar(): Calendar = dateProvider.getCalendar()

    private fun getUserFriendlyFormat(): SimpleDateFormat = dateProvider.getUserFriendlyFormat()

    private fun Int.validYear(): Boolean {
        val currentYear = getCalendar().get(Calendar.YEAR)
        return this >= currentYear - 10 || this <= currentYear + 10
    }

    private fun Int.validMonth(): Boolean = this in 0..12

    private fun Int.validDayOfMonth(): Boolean = this in 0..31
}