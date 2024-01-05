package com.ivettevaldez.unittesting.testdrivendevelopment.exercise8.networking

interface FetchContactsHttpEndpoint {

    enum class FailReason {

        GENERAL_ERROR,
        NETWORK_ERROR
    }

    interface Callback {

        fun onFetchContactsSucceeded(contacts: List<ContactSchema>)
        fun onFetchContactsFailed(reason: FailReason)
    }

    fun fetchContacts(filterTerm: String, callback: Callback)
}