package com.ivettevaldez.saturnus.invoices.payment.calculator

import com.ivettevaldez.saturnus.common.Constants
import javax.inject.Inject

open class InvoicePaymentCalculatorFactory @Inject constructor() {

    open fun newInvoicePaymentCalculator(personType: String): InvoicePaymentCalculator {
        return when (personType) {
            Constants.MORAL_PERSON -> InvoicePaymentCalculatorMoralPerson()
            Constants.PHYSICAL_PERSON -> InvoicePaymentCalculatorPhysicalPerson()
            else -> InvoicePaymentCalculatorPhysicalPerson()
        }
    }
}