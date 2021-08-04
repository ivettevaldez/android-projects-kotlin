package com.ivettevaldez.saturnus.screens.invoices.form.payment

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

class InvoiceFormPaymentFragment : BaseFragment(), Step {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var controllerFactory: ControllerFactory

    private lateinit var controller: InvoiceFormPaymentController

    companion object {

        private const val ARG_FOLIO = "ARG_FOLIO"

        @JvmStatic
        fun newInstance(folio: String?) =
            InvoiceFormPaymentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FOLIO, folio)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newInvoiceFormPaymentController()
        controller.initVariables(
            requireArguments().getString(ARG_FOLIO)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewMvc = viewMvcFactory.newInvoiceFormPaymentViewMvc(parent)

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

    override fun verifyStep(): VerificationError? {
        return controller.verifyStep()
    }

    override fun onSelected() {
        // Nothing to do here.
    }

    override fun onError(error: VerificationError) {
        controller.onError(error)
    }
}