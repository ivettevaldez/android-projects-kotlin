package com.ivettevaldez.saturnus.screens.invoices.list

import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator

class InvoicesListController(
    private val screensNavigator: ScreensNavigator,
    private val dialogsManager: DialogsManager,
    private val personDao: PersonDao,
    private val invoiceDao: InvoiceDao
) : IInvoicesListViewMvc.Listener {

    private lateinit var viewMvc: IInvoicesListViewMvc

    lateinit var rfc: String

    fun bindView(viewMvc: IInvoicesListViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindRfc(rfc: String) {
        this.rfc = rfc
    }

    fun onStart() {
        viewMvc.registerListener(this)

        bindInvoices()
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onAddNewInvoiceClicked() {
        if (getReceivers().isNotEmpty()) {
            screensNavigator.toInvoiceForm(issuingRfc = rfc)
        } else {
            dialogsManager.showMissingReceiversError(null)
        }
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

    private fun bindInvoices() {
        viewMvc.showProgressIndicator()
        viewMvc.bindInvoices(getInvoices())
        viewMvc.hideProgressIndicator()
    }

    private fun getPerson(): Person? = personDao.findByRfc(rfc)

    private fun getReceivers(): List<Person> = personDao.findAllReceivers()

    private fun getInvoices(): List<Invoice> = invoiceDao.findAllByIssuingRfc(rfc)
}