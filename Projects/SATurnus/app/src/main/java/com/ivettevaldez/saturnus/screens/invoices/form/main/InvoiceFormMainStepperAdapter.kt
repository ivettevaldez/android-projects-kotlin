package com.ivettevaldez.saturnus.screens.invoices.form.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsFragment
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter

private const val INVOICE_DETAILS_FORM_POSITION = 0
private const val INVOICE_PAYMENT_FORM_POSITION = 1
private const val STEPS = 2

class InvoiceFormMainStepperAdapter(
    context: Context,
    fragmentManager: FragmentManager,
    private val issuingRfc: String
) : AbstractFragmentStepAdapter(fragmentManager, context) {

    override fun createStep(position: Int): Step {
        return if (position == INVOICE_DETAILS_FORM_POSITION) {
            InvoiceFormDetailsFragment.newInstance(issuingRfc)
        } else {
            InvoiceFormPaymentFragment.newInstance(issuingRfc)
        }
    }

    override fun getCount(): Int = STEPS
}