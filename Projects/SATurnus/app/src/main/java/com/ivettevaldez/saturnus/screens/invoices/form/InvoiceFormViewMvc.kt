package com.ivettevaldez.saturnus.screens.invoices.form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IInvoiceFormViewMvc : IObservableViewMvc<IInvoiceFormViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
    }

    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class InvoiceFormViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IInvoiceFormViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_form
), IInvoiceFormViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.invoice_form_toolbar)
    private val layoutProgress: FrameLayout = findViewById(R.id.invoice_form_progress)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    init {

        initToolbar()
    }

    override fun showProgressIndicator() {
        layoutProgress.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        layoutProgress.visibility = View.GONE
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(
            context.getString(R.string.invoices_new)
        )

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