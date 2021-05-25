package com.ivettevaldez.saturnus.screens.invoices.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IInvoiceListItemViewMvc : IObservableViewMvc<IInvoiceListItemViewMvc.Listener> {

    interface Listener {

        fun onInvoiceClick(folio: String)
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

    private val layoutItem: LinearLayout = findViewById(R.id.item_invoice_layout_root)

    private lateinit var invoice: Invoice

    init {

        setListenerEvents()
    }

    override fun bindInvoice(invoice: Invoice) {
        this.invoice = invoice
    }

    private fun setListenerEvents() {
        with(layoutItem) {
            setOnClickListener {
                for (listener in listeners) {
                    listener.onInvoiceClick(invoice.folio)
                }
            }
        }
    }
}