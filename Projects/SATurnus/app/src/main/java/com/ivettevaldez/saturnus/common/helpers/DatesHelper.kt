package com.ivettevaldez.saturnus.common.helpers

/* ktlint-disable no-wildcard-imports */

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DatesHelper {

    private const val USER_FRIENDLY_FORMAT = "dd/MMMM/yyyy"

    private val objectTag: String = this::class.java.simpleName

    fun String.calendar(): Calendar = if (this.isNotBlank()) {
        val calendar = Calendar.getInstance(Locale.getDefault())

        try {
            calendar.time = getFormat().parse(this)!!
        } catch (ex: ParseException) {
            Log.e(objectTag, "@@@@@ Attempting to parse a date: $this", ex)
        }

        calendar
    } else {
        Calendar.getInstance()
    }

    fun Date.friendlyDate(): String = getFormat().format(this)

    fun friendlyDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(year, month, day)
        return getFormat().format(calendar.time)
    }

    private fun getFormat() = SimpleDateFormat(USER_FRIENDLY_FORMAT, Locale.getDefault())
}