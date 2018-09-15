package com.silviavaldez.sampleapp.models.daos

import android.util.Log
import com.silviavaldez.sampleapp.models.datamodels.Person
import io.realm.Realm
import io.realm.RealmResults

class PersonDao {

    private val tag = PersonDao::class.simpleName
    private val realm = Realm.getDefaultInstance()

    fun create(person: Person) {
        if (realm != null) {
            try {
                realm.executeTransaction {
                    val newPerson = realm.createObject(Person::class.java, person.id)
                    newPerson.name = person.name
                    newPerson.lastName = person.lastName
                    newPerson.email = person.email
                    newPerson.age = person.age
                    newPerson.address = person.address
                    newPerson.city = person.city
                    newPerson.country = person.country
                    newPerson.profession = person.profession
                    newPerson.gender = person.gender
                }
            } catch (ex: Exception) {
                Log.e(tag, "Attempting to create a Person", ex)
            }
        } else {
            Log.e(tag, "NULL!")
        }
    }

    fun findById(id: Long): Person {
        return realm.where(Person::class.java)
                .equalTo("id", id)
                .findFirst()!!
    }

    fun findAllPeople(): RealmResults<Person> {
        return realm.where(Person::class.java)
                .findAll()
                .sort("name")!!
    }

    fun deleteAll() {
        try {
            realm.executeTransaction {
                findAllPeople()
                        .deleteAllFromRealm()
            }
        } catch (ex: Exception) {
            Log.e(tag, "Attempting to delete all People", ex)
        }
    }

    fun getNextId(): Long {
        val allPeople = findAllPeople()
        return if (allPeople.size != 0) {
            val lastId = allPeople.sort("id")
                    .last()!!.id
            lastId + 1
        } else {
            1
        }
    }
}