package com.ivettevaldez.saturnus.testdata

/* ktlint-disable no-wildcard-imports */

import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.payment.InvoicePayment
import java.util.*

object InvoiceTestData {

    fun getInvoice(): Invoice = Invoice(
        issuing = PeopleTestData.getPhysicalPerson(),
        receiver = PeopleTestData.getMoralPerson(),
        issuedAt = Date(),
        certificatedAt = Date(),
        payment = getPayment2()
    )

    fun getPayment(): InvoicePayment = InvoicePayment(
        subtotal = 666.0,
        iva = 666.0,
        ivaWithholding = 666.0,
        isrWithholding = 666.0,
        total = 666.0
    )

    private fun getPayment2(): InvoicePayment = InvoicePayment(
        subtotal = 0.0,
        iva = 0.0,
        ivaWithholding = 0.0,
        isrWithholding = 0.0,
        total = 0.0
    )
}