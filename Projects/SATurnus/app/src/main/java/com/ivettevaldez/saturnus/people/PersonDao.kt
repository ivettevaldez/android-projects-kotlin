package com.ivettevaldez.saturnus.people

/* ktlint-disable no-wildcard-imports */

import android.util.Log
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.querySorted
import com.vicpin.krealmextensions.save
import io.realm.Sort
import io.realm.exceptions.RealmException
import java.util.*
import javax.inject.Inject

open class PersonDao @Inject constructor() {

    private val classTag = this::class.java.simpleName
    private val baseClassTag = Person::class.java.simpleName

    companion object {

        private const val RFC = "rfc"
        private const val CLIENT_TYPE = "clientType"
        private const val UPDATED_AT = "updatedAt"
    }

    fun save(person: Person): Boolean {
        return try {
            person.save()
            true
        } catch (ex: RealmException) {
            Log.e(
                classTag,
                "@@@@@ Attempting to save a $baseClassTag with $RFC '${person.rfc}'",
                ex
            )
            false
        }
    }

    fun findByRfc(rfc: String): Person? {
        val person = Person().queryFirst { equalTo(RFC, rfc) }
        if (person == null) {
            Log.e(classTag, "@@@@@ $baseClassTag with $RFC '$rfc' does not exist")
        }
        return person
    }

    open fun findAllIssuing(): List<Person> {
        return Person().querySorted(UPDATED_AT, Sort.DESCENDING) {
            equalTo(
                CLIENT_TYPE,
                ClientType.getString(ClientType.Type.ISSUING)
            )
        }
    }

    fun findAllReceivers(): List<Person> {
        return Person().querySorted(UPDATED_AT, Sort.DESCENDING) {
            equalTo(
                CLIENT_TYPE,
                ClientType.getString(ClientType.Type.RECEIVER)
            )
        }
    }

    fun activate(rfc: String) {
        val person = findByRfc(rfc)
        if (person != null) {
            person.active = true
            person.updatedAt = Date()
            save(person)
        }
    }

    fun deactivate(rfc: String) {
        val person = findByRfc(rfc)
        if (person != null) {
            person.active = false
            person.updatedAt = Date()
            save(person)
        }
    }

    fun delete(rfc: String) {
        val person = findByRfc(rfc)
        if (person != null) {
            Person().delete { equalTo(RFC, rfc) }
        }
    }
}