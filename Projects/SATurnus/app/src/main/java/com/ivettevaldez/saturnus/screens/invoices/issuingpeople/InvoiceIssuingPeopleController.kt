package com.ivettevaldez.saturnus.screens.invoices.issuingpeople

import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator

class InvoiceIssuingPeopleController(
    private val screensNavigator: ScreensNavigator,
    private val personDao: PersonDao
) : IInvoiceIssuingPeopleViewMvc.Listener {

    private lateinit var viewMvc: IInvoiceIssuingPeopleViewMvc

    fun bindView(viewMvc: IInvoiceIssuingPeopleViewMvc) {
        this.viewMvc = viewMvc
    }

    fun onStart() {
        viewMvc.registerListener(this)
        bindPeople()
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    fun getPeople(): List<Person> = personDao.findAllIssuing()

    fun bindPeople() {
        val people = getPeople()
        if (people.isNotEmpty()) {
            viewMvc.showProgressIndicator()
            viewMvc.bindPeople(people)
            viewMvc.hideProgressIndicator()
        }
    }

    override fun onPersonClick(rfc: String) {
        screensNavigator.toInvoicesList(rfc)
    }
}