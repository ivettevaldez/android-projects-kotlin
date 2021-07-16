package com.ivettevaldez.saturnus.testdata

/* ktlint-disable no-wildcard-imports */

import com.ivettevaldez.saturnus.invoices.Invoice
import java.util.*

object InvoiceTestData {

    fun getInvoice(): Invoice {
        return Invoice(
            issuing = PeopleTestData.getPerson1(),
            receiver = PeopleTestData.getPerson2(),
            issuedAt = Date(),
            certificatedAt = Date()
        )
    }
}