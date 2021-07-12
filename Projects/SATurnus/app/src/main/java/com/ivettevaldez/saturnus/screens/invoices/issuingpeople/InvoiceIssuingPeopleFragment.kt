package com.ivettevaldez.saturnus.screens.invoices.issuingpeople

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceIssuingPeopleFragment : BaseFragment() {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var controller: InvoiceIssuingPeopleController

    companion object {

        @JvmStatic
        fun newInstance() = InvoiceIssuingPeopleFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        controller = controllerFactory.newInvoiceIssuingPeopleController()

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewMvc = viewMvcFactory.newInvoiceIssuingPeopleViewMvc(parent)
        controller.bindView(viewMvc)
        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }
}