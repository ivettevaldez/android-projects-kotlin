package com.ivettevaldez.saturnus.testdata

import com.ivettevaldez.saturnus.invoices.Invoice

object InvoiceTestData {

    fun getInvoice(): Invoice {
        return Invoice(issuing = PeopleTestData.getPerson1())
    }
}