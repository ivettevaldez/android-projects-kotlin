package com.ivettevaldez.saturnus.testdata

import com.ivettevaldez.saturnus.people.Person

object PeopleTestData {

    private const val RFC_1: String = "rfc1"
    private const val NAME_1: String = "name1"
    private const val RFC_2: String = "rfc2"
    private const val NAME_2: String = "name2"

    fun getPeople(): List<Person> {
        return listOf(
            Person(RFC_1, NAME_1),
            Person(RFC_2, NAME_2)
        )
    }
}