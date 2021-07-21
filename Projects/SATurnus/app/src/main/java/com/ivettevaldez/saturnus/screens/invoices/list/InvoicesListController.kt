package com.ivettevaldez.saturnus.screens.invoices.list

import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator

class InvoicesListController(
    private val screensNavigator: ScreensNavigator,
    private val personDao: PersonDao,
    private val invoiceDao: InvoiceDao
) : IInvoicesListViewMvc.Listener {

    private lateinit var viewMvc: IInvoicesListViewMvc

    var rfc: String? = null

    fun bindView(viewMvc: IInvoicesListViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindArguments(rfc: String) {
        this.rfc = rfc
    }

    fun onStart() {
        viewMvc.registerListener(this)
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onAddNewInvoiceClicked() {
        screensNavigator.toInvoiceForm(issuingRfc = rfc)
    }

    override fun onDetailsClicked(folio: String) {
        screensNavigator.toInvoiceDetails(folio)
    }

    fun setToolbarTitle() {
        val person = getPerson()
        if (person != null) {
            viewMvc.setToolbarTitle(person.name)
        } else {
            throw IllegalArgumentException("@@@@@ Invalid RFC: $rfc")
        }
    }

    fun bindInvoices() {
        viewMvc.showProgressIndicator()
        viewMvc.bindInvoices(getInvoices())
        viewMvc.hideProgressIndicator()
    }

    private fun getPerson(): Person? = personDao.findByRfc(rfc!!)

    private fun getInvoices(): List<Invoice> = invoiceDao.findAllByIssuingRfc(rfc!!)
}