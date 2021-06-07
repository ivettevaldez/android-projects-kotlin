package com.ivettevaldez.saturnus.screens.invoices.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

class InvoicesListRecyclerAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: IInvoiceListItemViewMvc.Listener
) : RecyclerView.Adapter<InvoicesListRecyclerAdapter.ViewHolder>(),
    IInvoiceListItemViewMvc.Listener {

    private val invoices: MutableList<Invoice> = mutableListOf()

    inner class ViewHolder(val viewMvc: IInvoiceListItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.getRootView())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewMvc = viewMvcFactory.newInvoiceListItemViewMvc(parent)
        viewMvc.registerListener(this)
        return ViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewMvc.bindInvoice(
            invoices[position]
        )
    }

    override fun getItemCount(): Int = invoices.size

    override fun onDetailsClicked(folio: String) {
        listener.onDetailsClicked(folio)
    }

    fun updateData(invoices: List<Invoice>) {
        this.invoices.clear()
        this.invoices.addAll(invoices)
        notifyDataSetChanged()
    }
}