package com.ivettevaldez.saturnus.testdata

/* ktlint-disable no-wildcard-imports */

import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.payment.InvoicePayment
import java.util.*

object InvoiceTestData {

    fun getInvoicesList(): List<Invoice> = listOf(
        getInvoice(),
        getInvoice(),
        getInvoice()
    )

    fun getInvoice(): Invoice = Invoice(
        issuing = PersonTestData.getPhysicalPerson(),
        receiver = PersonTestData.getMoralPerson(),
        issuedAt = Date(),
        certificatedAt = Date(),
        payment = getPayment()
    )

    private fun getPayment(): InvoicePayment = InvoicePayment(
        subtotal = 666.0,
        iva = 666.0,
        ivaWithholding = 666.0,
        isrWithholding = 666.0,
        total = 666.0
    )
}