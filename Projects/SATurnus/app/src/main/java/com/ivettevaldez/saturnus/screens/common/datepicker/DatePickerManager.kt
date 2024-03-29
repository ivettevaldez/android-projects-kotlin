package com.ivettevaldez.saturnus.screens.common.datepicker

/* ktlint-disable no-wildcard-imports */

import androidx.fragment.app.FragmentManager
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*
import javax.inject.Inject

open class DatePickerManager @Inject constructor(private val fragmentManager: FragmentManager) {

    companion object {

        const val TAG_ISSUING_DATE = "TAG_ISSUING_DATE"
        const val TAG_CERTIFICATION_DATE = "TAG_CERTIFICATION_DATE"
    }

    open fun showDatePicker(
        date: Calendar,
        listener: DatePickerDialog.OnDateSetListener,
        tag: String?
    ) {
        val datePickerDialog = DatePickerDialog.newInstance(
            listener,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.version = DatePickerDialog.Version.VERSION_1
        datePickerDialog.show(fragmentManager, tag)
    }
}