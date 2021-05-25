package com.ivettevaldez.saturnus.people

/* ktlint-disable no-wildcard-imports */

import com.ivettevaldez.saturnus.invoices.Invoice
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class Person(
    @PrimaryKey var rfc: String = "",
    @Index var name: String = "",
    @Index var active: Boolean = true,
    @Index var clientType: String = "",
    var personType: String = "",
    var serviceConcepts: RealmList<String> = RealmList(),
    var createdAt: Date = Date(),
    var updatedAt: Date = Date(),
    var invoices: RealmList<Invoice> = RealmList()
) : RealmObject()