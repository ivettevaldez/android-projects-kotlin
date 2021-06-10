package com.ivettevaldez.saturnus.screens.invoices.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IInvoiceDetailsViewMvc : IObservableViewMvc<IInvoiceDetailsViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
    }
}

class InvoiceDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IInvoiceDetailsViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_details
), IInvoiceDetailsViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.invoice_details_toolbar)
    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    init {

        initToolbar()
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(context.getString(R.string.invoices_invoice))

        toolbarViewMvc.enableNavigateUpAndListen(object : IToolbarViewMvc.NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        toolbar.addView(toolbarViewMvc.getRootView())
    }
}