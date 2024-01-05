package com.ivettevaldez.unittesting.testdrivendevelopment.exercise8

import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.contacts.Contact
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.networking.ContactSchema
import com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.networking.FetchContactsHttpEndpoint

class FetchContactsUseCase(private val fetchContactsHttpEndpoint: FetchContactsHttpEndpoint) :
    FetchContactsHttpEndpoint.Callback {

    private val listeners: ArrayList<Listener> = arrayListOf()

    interface Listener {

        fun onContactsFetched(contacts: List<Contact>)
        fun onFetchContactsFailed()
    }

    fun fetchContactsAndNotify(filterTerm: String) {
        fetchContactsHttpEndpoint.fetchContacts(filterTerm, this)
    }

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun onFetchContactsSucceeded(contacts: List<ContactSchema>) {
        for (listener in listeners) {
            listener.onContactsFetched(
                getContactsFromSchemas(contacts)
            )
        }
    }

    override fun onFetchContactsFailed(reason: FetchContactsHttpEndpoint.FailReason) {
        for (listener in listeners) {
            listener.onFetchContactsFailed()
        }
    }

    private fun getContactsFromSchemas(schemas: List<ContactSchema>): List<Contact> {
        val contacts: MutableList<Contact> = mutableListOf()
        for (schema in schemas) {
            contacts.add(
                Contact(
                    id = schema.id,
                    fullName = schema.fullName,
                    fullPhoneNumber = schema.fullPhoneNumber,
                    photoUrl = schema.photoUrl,
                    age = schema.age
                )
            )
        }
        return contacts
    }
}