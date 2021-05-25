package com.ivettevaldez.saturnus.screens.invoices.issuingpeople

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceIssuingPeopleFragment : BaseFragment(),
    IInvoiceIssuingPeopleViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var peopleDao: PersonDao

    private lateinit var viewMvc: IInvoiceIssuingPeopleViewMvc

    companion object {

        @JvmStatic
        fun newInstance() = InvoiceIssuingPeopleFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceIssuingPeopleViewMvc(parent)

        bindPeople()

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

    private fun getPeople(): List<Person> = peopleDao.findAllIssuing()

    private fun bindPeople() {
        Thread {
            viewMvc.showProgressIndicator()
            viewMvc.bindPeople(getPeople())
            viewMvc.hideProgressIndicator()
        }.start()
    }

    override fun onPersonClick(rfc: String) {
        screensNavigator.toInvoicesList(rfc)
    }

    override fun onPersonLongClick(rfc: String) {
        // TODO:
    }
}