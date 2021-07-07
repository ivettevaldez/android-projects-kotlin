package com.ivettevaldez.saturnus.common.helpers

/* ktlint-disable no-wildcard-imports */

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DatesHelper {

    private const val USER_FRIENDLY_FORMAT = "dd/MMMM/yyyy"

    private val objectTag: String = this::class.java.simpleName

    fun String.calendar(): Calendar = try {
        val calendar = getCalendar()
        calendar.time = getUserFriendlyFormat().parse(this)!!
        calendar
    } catch (ex: ParseException) {
        Log.e(objectTag, "@@@@@ Attempting to parse a date: $this", ex)
        getCalendar()
    }

    fun Date.friendlyDate(): String = getUserFriendlyFormat().format(this)

    fun friendlyDate(year: Int, month: Int, day: Int): String {
        val calendar = getCalendar()
        calendar.set(year, month, day)
        return getUserFriendlyFormat().format(calendar.time)
    }

    private fun getCalendar(): Calendar = Calendar.getInstance(Locale.getDefault())

    private fun getUserFriendlyFormat(): SimpleDateFormat =
        SimpleDateFormat(USER_FRIENDLY_FORMAT, Locale.getDefault())
}