package com.ivettevaldez.saturnus.people

import android.util.Log
import com.ivettevaldez.saturnus.common.Constants
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.querySorted
import com.vicpin.krealmextensions.save
import io.realm.Sort
import io.realm.exceptions.RealmException
import javax.inject.Inject

class PersonDao @Inject constructor() {

    private val classTag = this::class.java.simpleName
    private val baseClassTag = Person::class.java.simpleName

    fun save(person: Person): Boolean {
        return try {
            person.save()
            true
        } catch (ex: RealmException) {
            Log.e(classTag, "@@@@@ Attempting to save a $baseClassTag with RFC ${person.rfc}", ex)
            false
        }
    }

    fun findByRfc(rfc: String): Person? {
        val person = Person().queryFirst {
            notEqualTo("deleted", true)
                .and()
            equalTo("rfc", rfc)
        }
        if (person == null) {
            Log.e(classTag, "@@@@@ $baseClassTag with RFC $rfc does not exist")
        }
        return person
    }

    fun findAllIssuing(): List<Person> {
        return Person().querySorted("createdAt", Sort.DESCENDING) {
            notEqualTo("deleted", true)
                .and()
            equalTo("clientType", Constants.CLIENT_ISSUING)
        }
    }

    fun findAllReceivers(): List<Person> {
        return Person().querySorted("createdAt", Sort.DESCENDING) {
            notEqualTo("deleted", true)
                .and()
            equalTo("clientType", Constants.CLIENT_RECEIVER)
        }
    }

    fun delete(rfc: String) {
        val person = findByRfc(rfc)
        if (person != null) {
            Person().delete {
                equalTo("rfc", rfc)
            }
        }
    }
}