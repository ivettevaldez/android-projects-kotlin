package com.ivettevaldez.saturnus.screens.invoices.issuingpeople

import android.graphics.Paint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.people.list.IPeopleListItemViewMvc
import com.ivettevaldez.saturnus.screens.people.list.PeopleListRecyclerAdapter

interface IInvoiceIssuingPeopleViewMvc : IObservableViewMvc<IInvoiceIssuingPeopleViewMvc.Listener> {

    interface Listener {

        fun onAddNewPersonClicked()
        fun onPersonClicked(rfc: String)
    }

    fun bindPeople(people: List<Person>)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class InvoiceIssuingPeopleViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val uiHandler: Handler,
    private val navDrawerHelper: INavDrawerHelper,
    private val viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IInvoiceIssuingPeopleViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_issuing_people
), IInvoiceIssuingPeopleViewMvc,
    IPeopleListItemViewMvc.Listener {

    private val toolbar: Toolbar = findViewById(R.id.invoice_issuing_people_toolbar)
    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    private val layoutProgress: FrameLayout = findViewById(R.id.people_list_progress)
    private val recycler: RecyclerView = findViewById(R.id.people_list_recycler)
    private val textAddNew: TextView = findViewById(R.id.people_list_text_add_new)

    private lateinit var peopleListRecyclerAdapter: PeopleListRecyclerAdapter

    init {

        initToolbar()
        initRecycler()
    }

    override fun bindPeople(people: List<Person>) {
        uiHandler.post {
            if (people.isNotEmpty()) {
                textAddNew.visibility = View.GONE
                recycler.visibility = View.VISIBLE

                peopleListRecyclerAdapter.updateData(people)
            } else {
                textAddNew.visibility = View.VISIBLE
                textAddNew.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                textAddNew.setTextColor(ContextCompat.getColor(context, R.color.dodger_blue))
                textAddNew.setOnClickListener {
                    for (listener in listeners) {
                        listener.onAddNewPersonClicked()
                    }
                }

                recycler.visibility = View.GONE
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

    override fun onPersonClick(rfc: String) {
        for (listener in listeners) {
            listener.onPersonClicked(rfc)
        }
    }

    override fun onPersonLongClick(rfc: String) {
        // Nothing to do here.
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(context.getString(R.string.menu_invoices))

        toolbarViewMvc.enableMenuAndListen(object : IToolbarViewMvc.MenuClickListener {
            override fun onMenuClicked() {
                navDrawerHelper.openDrawer()
            }
        })

        toolbar.addView(toolbarViewMvc.getRootView())
    }

    private fun initRecycler() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.isSmoothScrollbarEnabled = true

        peopleListRecyclerAdapter = PeopleListRecyclerAdapter(
            viewMvcFactory,
            this
        )

        with(recycler) {
            layoutManager = linearLayoutManager
            adapter = peopleListRecyclerAdapter
        }
    }
}