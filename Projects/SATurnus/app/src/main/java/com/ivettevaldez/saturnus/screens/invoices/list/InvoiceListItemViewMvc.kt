package com.ivettevaldez.saturnus.screens.invoices.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.helpers.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IInvoiceListItemViewMvc : IObservableViewMvc<IInvoiceListItemViewMvc.Listener> {

    interface Listener {

        fun onDetailsClicked(folio: String)
    }

    fun bindInvoice(invoice: Invoice)
}

class InvoiceListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IInvoiceListItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.item_invoice
), IInvoiceListItemViewMvc {

    private val textStatus: TextView = findViewById(R.id.item_invoice_text_status)
    private val textFolio: TextView = findViewById(R.id.item_invoice_text_folio)
    private val textConcept: TextView = findViewById(R.id.item_invoice_text_concept)
    private val textDescription: TextView = findViewById(R.id.item_invoice_text_description)
    private val textPersonName: TextView = findViewById(R.id.item_invoice_text_person_name)
    private val imagePersonType: ImageView = findViewById(R.id.item_invoice_image_person_type)
    private val buttonSeeDetails: Button = findViewById(R.id.item_invoice_button_details)

    private lateinit var invoice: Invoice

    init {

        setListenerEvents()
    }

    override fun bindInvoice(invoice: Invoice) {
        this.invoice = invoice

        textStatus.text = invoice.status
        textStatus.setBackgroundResource(getStatusBackgroundColor())

        textConcept.text = getConcept()
        textDescription.text = invoice.description
        textFolio.text = getFolio()

        if (invoice.receiver != null) {
            textPersonName.text = invoice.receiver!!.name
            imagePersonType.setImageResource(getPersonTypeIcon())
        }
    }

    private fun setListenerEvents() {
        buttonSeeDetails.setOnClickListener {
            for (listener in listeners) {
                listener.onDetailsClicked(invoice.folio)
            }
        }
    }

    private fun getStatusBackgroundColor(): Int = when (invoice.status) {
        Constants.INVOICE_STATUS_ACTIVE -> R.drawable.shape_tag_active
        Constants.INVOICE_STATUS_INACTIVE -> R.drawable.shape_tag_canceled
        else -> R.drawable.shape_tag_active
    }

    private fun getFolio(): String = String.format(
        "%s: %s",
        context.getString(R.string.invoices_folio),
        invoice.folio
    )

    private fun getConcept(): String = String.format(
        context.getString(R.string.invoices_concept_template),
        invoice.concept,
        invoice.payment!!.total.toCurrency(),
        invoice.effect
    )

    private fun getPersonTypeIcon(): Int {
        return when (invoice.receiver!!.personType) {
            Constants.PHYSICAL_PERSON -> R.mipmap.ic_person_grey_36dp
            Constants.MORAL_PERSON -> R.mipmap.ic_people_grey_36dp
            else -> R.mipmap.ic_person_plus_blue_36dp
        }
    }
}