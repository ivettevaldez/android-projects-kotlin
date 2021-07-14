package com.ivettevaldez.saturnus.screens.invoices.form.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceFormMainFragment : BaseFragment() {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var controller: InvoiceFormMainController

    companion object {

        private const val ARG_FOLIO = "ARG_FOLIO"
        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"

        @JvmStatic
        fun newInstance(folio: String?, issuingRfc: String?) =
            InvoiceFormMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FOLIO, folio)
                    putString(ARG_ISSUING_RFC, issuingRfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newInvoiceFormMainController()

        bindArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // FIXME: The complete form disappears when this screen is opened more than once.

        val viewMvc = viewMvcFactory.newInvoiceFormMainViewMvc(parent)

        controller.bindView(viewMvc)
        controller.initViews()

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

    private fun bindArguments() {
        requireArguments().let {
            val issuingRfc = it.getString(ARG_ISSUING_RFC)
            val folio = it.getString(ARG_FOLIO)

            controller.bindArguments(issuingRfc, folio)
        }
    }
}