package com.ivettevaldez.saturnus.screens.invoices.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.WorkerThread
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoicesListFragment : BaseFragment(),
    IInvoicesListViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var personDao: PersonDao

    @Inject
    lateinit var invoiceDao: InvoiceDao

    private lateinit var viewMvc: IInvoicesListViewMvc
    private lateinit var rfc: String

    companion object {

        private const val ARG_RFC = "ARG_RFC"

        @JvmStatic
        fun newInstance(rfc: String) =
            InvoicesListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RFC, rfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            rfc = it.getString(ARG_RFC)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoicesListViewMvc(parent)

        setToolbarTitle()
        bindInvoices()

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onInvoiceClick(folio: String) {
        // TODO:
    }

    override fun onAddNewInvoiceClick() {
        screensNavigator.toInvoiceForm(rfc)
    }

    @WorkerThread
    private fun getPerson(): Person? = personDao.findByRfc(rfc)

    @WorkerThread
    private fun getInvoices(): List<Invoice> = invoiceDao.findAllByIssuingRfc(rfc)

    private fun setToolbarTitle() {
        Thread {
            val person = getPerson()
            if (person != null) {
                viewMvc.setToolbarTitle(person.name)
            } else {
                throw IllegalArgumentException("Invalid RFC: $rfc")
            }
        }.start()
    }

    private fun bindInvoices() {
        Thread {
            viewMvc.showProgressIndicator()
            viewMvc.bindInvoices(getInvoices())
            viewMvc.hideProgressIndicator()
        }.start()
    }
}