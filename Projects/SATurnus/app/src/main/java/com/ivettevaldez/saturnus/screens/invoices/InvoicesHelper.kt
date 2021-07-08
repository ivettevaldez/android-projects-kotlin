package com.ivettevaldez.saturnus.screens.invoices

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.dependencyinjection.presentation.PresentationScope
import com.ivettevaldez.saturnus.common.dates.DatesHelper.friendlyDate
import java.util.*
import javax.inject.Inject

@PresentationScope
class InvoicesHelper @Inject constructor(private val context: Context) {

    fun getStatusBackground(status: String): Int = when (status) {
        Constants.INVOICE_STATUS_ACTIVE -> R.drawable.shape_tag_active
        Constants.INVOICE_STATUS_INACTIVE -> R.drawable.shape_tag_inactive
        else -> R.drawable.shape_tag_active
    }

    fun getFormattedStatus(status: String): String = status.uppercase()

    fun getFormattedFolio(folio: String): String = String.format(
        context.getString(R.string.default_label_value_template),
        context.getString(R.string.invoices_folio).uppercase(),
        folio
    )

    fun getFormattedConcept(concept: String, payment: String, effect: String): String =
        String.format(
            context.getString(R.string.invoices_concept_template),
            concept,
            payment,
            effect
        )

    fun getIssuingDate(date: Date): String = String.format(
        context.getString(R.string.default_label_value_template),
        context.getString(R.string.invoices_issuing_date),
        date.friendlyDate()
    )

    fun getCertificationDate(date: Date): String = String.format(
        context.getString(R.string.default_label_value_template),
        context.getString(R.string.invoices_certification_date),
        date.friendlyDate()
    )

    fun getPersonTypeIcon(personType: String): Int {
        return when (personType) {
            Constants.PHYSICAL_PERSON -> R.mipmap.ic_person_grey_36dp
            Constants.MORAL_PERSON -> R.mipmap.ic_people_grey_36dp
            else -> R.mipmap.ic_person_plus_blue_36dp
        }
    }
}