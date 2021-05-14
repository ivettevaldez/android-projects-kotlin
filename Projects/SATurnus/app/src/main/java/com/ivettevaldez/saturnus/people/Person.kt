package com.ivettevaldez.saturnus.people

/* ktlint-disable no-wildcard-imports */

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class Person(
    @PrimaryKey var rfc: String = "",
    @Index var name: String = "",
    @Index var personType: String = "",
    @Index var clientType: String = "",
    @Index var deleted: Boolean = false,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date(),
    var deletedAt: Date? = null
) : RealmObject()