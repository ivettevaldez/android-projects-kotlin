package com.ivettevaldez.saturnus.invoices

/* ktlint-disable no-wildcard-imports */

import com.ivettevaldez.saturnus.people.Person
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.Index
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class Invoice(
    @PrimaryKey var folio: String = "",
    @Index var fiscalFolio: String = "",
    @Index var status: String = "",
    @Index var cancellationStatus: String = "",
    @Index var cancellationProcessStatus: String = "",
    var issuing: Person? = null,
    var receiver: Person? = null,
    var concept: String = "",
    var effect: String = "",
    var authorizedCertificationProvider: String = "",
    var issuedAt: Date? = null,
    var certificatedAt: Date? = null,
    var cancelledAt: Date? = null,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date(),
    var payment: InvoicePayment? = null,
    @LinkingObjects("invoices") val persons: RealmResults<Person>? = null
) : RealmObject()