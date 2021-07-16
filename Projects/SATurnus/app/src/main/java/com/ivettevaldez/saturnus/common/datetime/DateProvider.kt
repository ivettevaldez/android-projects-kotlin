package com.ivettevaldez.saturnus.common.datetime

/* ktlint-disable no-wildcard-imports */

import java.text.SimpleDateFormat
import java.util.*

open class DateProvider {

    private val locale = Locale.getDefault()

    companion object {

        private const val USER_FRIENDLY_FORMAT = "dd/MMMM/yyyy"
    }

    open fun getCalendar(): Calendar = Calendar.getInstance(locale)

    open fun getUserFriendlyFormat(): SimpleDateFormat =
        SimpleDateFormat(USER_FRIENDLY_FORMAT, locale)
}