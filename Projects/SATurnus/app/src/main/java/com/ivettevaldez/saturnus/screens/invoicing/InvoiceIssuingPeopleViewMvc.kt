package com.ivettevaldez.saturnus.screens.invoicing

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.UtilsHelper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.people.list.IPeopleListItemViewMvc
import com.ivettevaldez.saturnus.screens.people.list.PeopleListRecyclerAdapter

interface IInvoiceIssuingPeopleViewMvc : IObservableViewMvc<IInvoiceIssuingPeopleViewMvc.Listener> {

    interface Listener {

        fun onPersonClick(rfc: String)
        fun onPersonLongClick(rfc: String)
    }

    fun bindPeople(people: List<Person>)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class InvoiceIssuingPeopleViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val uiHandler: Handler,
    private val utilsHelper: UtilsHelper,
    private val navDrawerHelper: INavDrawerHelper,
    private val viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IInvoiceIssuingPeopleViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_issuing_people
), IInvoiceIssuingPeopleViewMvc,
    IPeopleListItemViewMvc.Listener {

    private val toolbar: Toolbar = findViewById(R.id.invoice_issuing_people_toolbar)
    private val layoutProgress: FrameLayout = findViewById(R.id.people_list_progress)
    private val recycler: RecyclerView = findViewById(R.id.people_list_recycler)
    private val textAddNew: TextView = findViewById(R.id.people_list_text_add_new)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

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
            listener.onPersonClick(rfc)
        }
    }

    override fun onPersonLongClick(rfc: String) {
        for (listener in listeners) {
            listener.onPersonLongClick(rfc)
        }
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(
            context.getString(R.string.menu_invoicing)
        )

        toolbarViewMvc.enableMenuAndListen(object : IToolbarViewMvc.MenuClickListener {
            override fun onMenuClicked() {
                for (listener in listeners) {
                    navDrawerHelper.openDrawer()
                }
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
            addItemDecoration(
                utilsHelper.getDividerItemDecoration()
            )
        }
    }
}