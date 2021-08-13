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
        payment = getDefaultPayment()
    )

    fun getPhysicalPersonPayment(): InvoicePayment = InvoicePayment(
        subtotal = 10000.0,
        iva = 1600.0,
        ivaWithholding = 0.0,
        isrWithholding = 0.0,
        total = 11600.0
    )

    fun getMoralPersonPayment(): InvoicePayment = InvoicePayment(
        subtotal = 10000.0,
        iva = 1600.0,
        ivaWithholding = 1066.67,
        isrWithholding = 1000.0,
        total = 9533.33
    )

    private fun getDefaultPayment(): InvoicePayment = InvoicePayment(
        subtotal = 666.0,
        iva = 666.0,
        ivaWithholding = 666.0,
        isrWithholding = 666.0,
        total = 666.0
    )
}