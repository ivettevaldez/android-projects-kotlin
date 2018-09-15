package com.silviavaldez.sampleapp.models.datamodels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Person(@PrimaryKey var id: Long = 0,
                  var name: String = "",
                  var lastName: String = "",
                  var email: String = "",
                  var age: Int = 0,
                  var address: String = "",
                  var city: String = "",
                  var country: String = "",
                  var profession: String = "",
                  var gender: Int = 0)
    : RealmObject()