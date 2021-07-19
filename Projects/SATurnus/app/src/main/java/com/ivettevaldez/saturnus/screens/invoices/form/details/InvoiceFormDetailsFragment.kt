package com.ivettevaldez.saturnus.screens.invoices.form.details

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import javax.inject.Inject

class InvoiceFormDetailsFragment : BaseFragment(),
    Step {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var controller: InvoiceFormDetailsController
    private lateinit var viewMvc: IInvoiceFormDetailsViewMvc

    companion object {

        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"
        private const val ARG_FOLIO = "ARG_FOLIO"

        @JvmStatic
        fun newInstance(folio: String?, issuingRfc: String?) =
            InvoiceFormDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FOLIO, folio)
                    putString(ARG_ISSUING_RFC, issuingRfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newInvoiceFormDetailsController()

        bindArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormDetailsViewMvc(parent)

        controller.bindView(viewMvc)
        controller.bindData()

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

    override fun verifyStep(): VerificationError? {
        return controller.verifyStep()
    }

    override fun onSelected() {
        // Nothing to do here.
    }

    override fun onError(error: VerificationError) {
        controller.onError(error)
    }

    private fun bindArguments() {
        requireArguments().let {
            val issuingRfc = it.getString(ARG_ISSUING_RFC)
            val folio = it.getString(ARG_FOLIO)

            controller.bindArguments(issuingRfc, folio)
        }
    }
}