package com.ivettevaldez.saturnus.screens.invoices.list

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.screens.common.UtilsHelper
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IInvoicesListViewMvc : IObservableViewMvc<IInvoicesListViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
        fun onAddNewInvoiceClick()
        fun onInvoiceClick(folio: String)
    }

    fun setToolbarTitle(title: String)
    fun bindInvoices(invoices: List<Invoice>)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class InvoicesListViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val uiHandler: Handler,
    private val utilsHelper: UtilsHelper,
    private val viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IInvoicesListViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoices_list
), IInvoicesListViewMvc,
    IInvoiceListItemViewMvc.Listener {

    private val toolbar: Toolbar = findViewById(R.id.invoices_list_toolbar)
    private val layoutProgress: FrameLayout = findViewById(R.id.invoices_list_progress)
    private val recycler: RecyclerView = findViewById(R.id.invoices_list_recycler)
    private val textAddNew: TextView = findViewById(R.id.invoices_list_text_add_new)
    private val textIssued: TextView = findViewById(R.id.invoices_list_text_issued)
    private val fabAddNew: FloatingActionButton = findViewById(R.id.invoices_list_fab_add_new)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    private lateinit var invoicesListRecyclerAdapter: InvoicesListRecyclerAdapter

    init {

        initToolbar()
        initRecycler()
        setListenerEvents()
    }

    override fun setToolbarTitle(title: String) {
        toolbarViewMvc.setTitle(title)
    }

    override fun bindInvoices(invoices: List<Invoice>) {
        uiHandler.post {
            if (invoices.isEmpty()) {
                textAddNew.visibility = View.VISIBLE
                textIssued.visibility = View.GONE
                recycler.visibility = View.GONE
            } else {
                textAddNew.visibility = View.GONE
                textIssued.visibility = View.VISIBLE
                recycler.visibility = View.VISIBLE

                invoicesListRecyclerAdapter.updateData(invoices)
            }
        }
    }

    override fun showProgressIndicator() {
        uiHandler.post {
            layoutProgress.visibility = View.VISIBLE
        }
    }

    override fun hideProgressIndicator() {
        uiHandler.post {
            layoutProgress.visibility = View.GONE
        }
    }

    override fun onInvoiceClick(folio: String) {
        for (listener in listeners) {
            listener.onInvoiceClick(folio)
        }
    }

    private fun initToolbar() {
        toolbarViewMvc.enableNavigateUpAndListen(object : IToolbarViewMvc.NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        toolbar.addView(toolbarViewMvc.getRootView())
    }

    private fun initRecycler() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.isSmoothScrollbarEnabled = true

        invoicesListRecyclerAdapter = InvoicesListRecyclerAdapter(
            viewMvcFactory,
            this
        )

        with(recycler) {
            layoutManager = linearLayoutManager
            adapter = invoicesListRecyclerAdapter
            addItemDecoration(
                utilsHelper.getDividerItemDecoration()
            )
        }
    }

    private fun setListenerEvents() {
        fabAddNew.setOnClickListener {
            for (listener in listeners) {
                listener.onAddNewInvoiceClick()
            }
        }
    }
}