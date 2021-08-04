package com.ivettevaldez.saturnus.screens.invoices.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceDetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var controllerFactory: ControllerFactory

    private lateinit var controller: InvoiceDetailsController

    companion object {

        private const val ARG_FOLIO = "ARG_FOLIO"

        @JvmStatic
        fun newInstance(folio: String) =
            InvoiceDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FOLIO, folio)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newInvoiceDetailsController()
        controller.bindFolio(
            requireArguments().getString(ARG_FOLIO)!!
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewMvc = viewMvcFactory.newInvoiceDetailsViewMvc(parent)

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