package com.ivettevaldez.saturnus.screens.invoices.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.helpers.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.invoices.InvoicesHelper
import com.ivettevaldez.saturnus.screens.people.item.IPersonItemViewMvc

interface IInvoiceDetailsViewMvc : IObservableViewMvc<IInvoiceDetailsViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
    }

    fun bindInvoice(invoice: Invoice)
}

class InvoiceDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory,
    private val invoicesHelper: InvoicesHelper
) : BaseObservableViewMvc<IInvoiceDetailsViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_details
), IInvoiceDetailsViewMvc {

    private val toolbarContainer: Toolbar = findViewById(R.id.invoice_details_toolbar)
    private val toolbar: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbarContainer)

    private val personIssuingContainer: FrameLayout = findViewById(R.id.invoice_details_issuing)
    private val personIssuing: IPersonItemViewMvc =
        viewMvcFactory.newPersonItemViewMvc(personIssuingContainer)

    private val personReceiverContainer: FrameLayout = findViewById(R.id.invoice_details_receiver)
    private val personReceiver: IPersonItemViewMvc = viewMvcFactory.newPersonItemViewMvc(
        personReceiverContainer
    )

    private val textStatus: TextView = findViewById(R.id.invoice_status_text_status)
    private val textFolio: TextView = findViewById(R.id.invoice_status_text_folio)
    private val textConcept: TextView = findViewById(R.id.invoice_details_text_concept)
    private val textDescription: TextView = findViewById(R.id.invoice_details_text_description)
    private val textIssuingDate: TextView = findViewById(R.id.invoice_details_text_issuing_date)
    private val textCertificationDate: TextView =
        findViewById(R.id.invoice_details_text_certification_date)

    private lateinit var invoice: Invoice

    init {

        initToolbar()
        initPersonItems()
    }

    override fun bindInvoice(invoice: Invoice) {
        this.invoice = invoice

        bindPeople()
        bindStatus()
        bindGeneralData()
        bindDates()
    }

    private fun initToolbar() {
        toolbar.setTitle(context.getString(R.string.invoices_detail))

        toolbar.enableNavigateUpAndListen(object : IToolbarViewMvc.NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        toolbarContainer.addView(toolbar.getRootView())
    }

    private fun initPersonItems() {
        personIssuing.seTitle(context.getString(R.string.invoices_issuing))
        personIssuing.setBackgroundColor(R.color.color_primary)
        personIssuingContainer.addView(personIssuing.getRootView())

        personReceiver.seTitle(context.getString(R.string.invoices_receiver))
        personReceiver.setBackgroundColor(R.color.color_primary)
        personReceiverContainer.addView(personReceiver.getRootView())
    }

    private fun bindPeople() {
        personIssuing.bindPerson(invoice.issuing!!)
        personReceiver.bindPerson(invoice.receiver!!)
    }

    private fun bindStatus() {
        textStatus.text = invoicesHelper.getFormattedStatus(invoice.status)
        textStatus.setBackgroundResource(invoicesHelper.getStatusBackground(invoice.status))
    }

    private fun bindGeneralData() {
        textFolio.text = invoicesHelper.getFormattedFolio(invoice.folio)

        textConcept.text = invoicesHelper.getFormattedConcept(
            invoice.concept,
            invoice.payment!!.total.toCurrency(),
            invoice.effect
        )

        textDescription.text = invoice.description
    }

    private fun bindDates() {
        textIssuingDate.text = invoicesHelper.getIssuingDate(invoice.issuedAt!!)

        if (invoice.certificatedAt != null) {
            textCertificationDate.text = invoicesHelper.getCertificationDate(
                invoice.certificatedAt!!
            )
        }
    }
}