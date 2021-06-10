package com.ivettevaldez.saturnus.screens.invoices.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.helpers.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.invoices.InvoicesHelper

interface IInvoiceListItemViewMvc : IObservableViewMvc<IInvoiceListItemViewMvc.Listener> {

    interface Listener {

        fun onDetailsClicked(folio: String)
    }

    fun bindInvoice(invoice: Invoice)
}

class InvoiceListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val invoicesHelper: InvoicesHelper
) : BaseObservableViewMvc<IInvoiceListItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.item_invoice
), IInvoiceListItemViewMvc {

    private val textStatus: TextView = findViewById(R.id.invoice_status_text_status)
    private val textFolio: TextView = findViewById(R.id.invoice_status_text_folio)
    private val textConcept: TextView = findViewById(R.id.item_invoice_text_concept)
    private val textDescription: TextView = findViewById(R.id.item_invoice_text_description)
    private val textReceiverName: TextView = findViewById(R.id.item_invoice_text_receiver_name)
    private val imageReceiverType: ImageView = findViewById(R.id.item_invoice_image_receiver_type)
    private val buttonSeeDetails: Button = findViewById(R.id.item_invoice_button_details)

    private lateinit var invoice: Invoice

    init {

        setListenerEvents()
    }

    override fun bindInvoice(invoice: Invoice) {
        this.invoice = invoice

        bindStatus()
        bindGeneralData()
        bindReceiver()
    }

    private fun setListenerEvents() {
        buttonSeeDetails.setOnClickListener {
            for (listener in listeners) {
                listener.onDetailsClicked(invoice.folio)
            }
        }
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

    private fun bindReceiver() {
        if (invoice.receiver != null) {
            textReceiverName.text = invoice.receiver!!.name
            imageReceiverType.setImageResource(
                invoicesHelper.getPersonTypeIcon(
                    invoice.receiver!!.personType
                )
            )
        }
    }
}