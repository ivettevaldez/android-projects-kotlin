package com.ivettevaldez.saturnus.testdata

import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.people.Person

object PeopleTestData {

    private const val RFC_1: String = "rfc1"
    private const val NAME_1: String = "name1"
    private const val RFC_2: String = "rfc2"
    private const val NAME_2: String = "name2"
    private const val RFC_3: String = "rfc3"
    private const val NAME_3: String = "name3"
    private const val RFC_4: String = "rfc4"
    private const val NAME_4: String = "name4"
    private const val RFC_5: String = "rfc5"
    private const val NAME_5: String = "name5"
    private const val RFC_6: String = "rfc6"
    private const val NAME_6: String = "name6"

    private val ISSUING = ClientType.getString(ClientType.Type.ISSUING)
    private val RECEIVER = ClientType.getString(ClientType.Type.RECEIVER)

    fun getPeople(): List<Person> {
        return listOf(
            getPhysicalPerson(),
            getMoralPerson(),
            getIssuingPersonOne(),
            getIssuingPersonTwo(),
            getReceiverPersonOne(),
            getReceiverPersonTwo()
        )
    }

    fun getAllIssuing(): List<Person> {
        return listOf(
            getIssuingPersonOne(),
            getIssuingPersonTwo()
        )
    }

    fun getAllReceivers(): List<Person> {
        return listOf(
            getReceiverPersonOne(),
            getReceiverPersonTwo()
        )
    }

    fun getPhysicalPerson(): Person {
        return Person(
            rfc = RFC_1,
            name = NAME_1,
            personType = Constants.PHYSICAL_PERSON
        )
    }

    fun getMoralPerson(): Person {
        return Person(
            rfc = RFC_2,
            name = NAME_2,
            personType = Constants.MORAL_PERSON
        )
    }

    private fun getIssuingPersonOne(): Person {
        return Person(
            rfc = RFC_3,
            name = NAME_3,
            clientType = ISSUING
        )
    }

    private fun getIssuingPersonTwo(): Person {
        return Person(
            rfc = RFC_4,
            name = NAME_4,
            clientType = ISSUING
        )
    }

    private fun getReceiverPersonOne(): Person {
        return Person(
            rfc = RFC_5,
            name = NAME_5,
            clientType = RECEIVER
        )
    }

    private fun getReceiverPersonTwo(): Person {
        return Person(
            rfc = RFC_6,
            name = NAME_6,
            clientType = RECEIVER
        )
    }
}