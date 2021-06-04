package com.ivettevaldez.saturnus.invoices

/* ktlint-disable no-wildcard-imports */

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.Index
import io.realm.annotations.LinkingObjects
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class InvoicePayment(
    @Index var status: String = "",
    var subtotal: Double = 0.0,
    var iva: Double = 0.0,
    var ivaWithholding: Double = 0.0,
    var isrWithholding: Double = 0.0,
    var total: Double = 0.0,
    var balance: Double = 0.0,
    var generatedAt: Date? = null,
    var paidAt: Date? = null,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date(),
    @LinkingObjects("payment") val invoices: RealmResults<Invoice>? = null
) : RealmObject()