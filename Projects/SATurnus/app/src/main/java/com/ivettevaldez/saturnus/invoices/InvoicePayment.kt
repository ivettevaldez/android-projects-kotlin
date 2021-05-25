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
    var subtotal: Long = 0L,
    var iva: Long = 0L,
    var ivaRetention: Long = 0L,
    var isrRetention: Long = 0L,
    var total: Long = 0L,
    var balance: Long = 0L,
    var generatedAt: Date? = null,
    var paidAt: Date? = null,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date(),
    @LinkingObjects("payment") val invoices: RealmResults<Invoice>? = null
) : RealmObject()