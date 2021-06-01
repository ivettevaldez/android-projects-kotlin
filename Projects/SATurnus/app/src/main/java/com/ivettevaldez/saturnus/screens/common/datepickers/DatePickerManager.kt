package com.ivettevaldez.saturnus.screens.common.datepickers

/* ktlint-disable no-wildcard-imports */

import androidx.fragment.app.FragmentManager
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DatePickerManager @Inject constructor(private val fragmentManager: FragmentManager) {

    companion object {

        const val USER_FRIENDLY_FORMAT = "dd/MMMM/yyyy"

        const val TAG_ISSUING_DATE = "TAG_ISSUING_DATE"
        const val TAG_CERTIFICATION_DATE = "TAG_CERTIFICATION_DATE"
    }

    fun showDatePicker(
        date: Calendar,
        listener: DatePickerDialog.OnDateSetListener,
        tag: String?
    ) {
        val dialog: DatePickerDialog = DatePickerDialog.newInstance(
            listener,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
        dialog.version = DatePickerDialog.Version.VERSION_1
        dialog.show(fragmentManager, tag)
    }

    fun parseToCalendar(date: String): Calendar {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val format = SimpleDateFormat(USER_FRIENDLY_FORMAT, Locale.getDefault())
        calendar.time = format.parse(date)!!
        return calendar
    }

    fun getUserFriendlyDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(year, month, day)
        val format = SimpleDateFormat(USER_FRIENDLY_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }
}