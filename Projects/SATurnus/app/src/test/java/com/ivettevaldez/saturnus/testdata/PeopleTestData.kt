package com.ivettevaldez.saturnus.testdata

import com.ivettevaldez.saturnus.people.Person

object PeopleTestData {

    private const val RFC_1: String = "rfc1"
    private const val NAME_1: String = "name1"
    private const val RFC_2: String = "rfc2"
    private const val NAME_2: String = "name2"

    fun getPeople(): List<Person> {
        return listOf(
            getPerson1(),
            getPerson2()
        )
    }

    fun getPerson1(): Person {
        return Person(rfc = RFC_1, name = NAME_1)
    }

    private fun getPerson2(): Person {
        return Person(rfc = RFC_2, name = NAME_2)
    }
}