package com.ivettevaldez.saturnus.screens.invoices.form.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceFormMainFragment : BaseFragment(),
    IInvoiceFormMainViewMvc.Listener,
    FragmentsEventBus.Listener,
    DialogsEventBus.Listener {

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

        requireArguments().let {
            val issuingRfc = it.getString(ARG_ISSUING_RFC)
            val folio = it.getString(ARG_FOLIO)

            controller.bindArguments(issuingRfc, folio)
            controller.findInvoiceIfEditionMode()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // FIXME: The complete form disappears when this screen is opened more than once.

        val viewMvc = viewMvcFactory.newInvoiceFormMainViewMvc(parent)

        controller.bindView(viewMvc)
        controller.setToolbarTitle()
        controller.initStepper()

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

    override fun onNavigateUpClicked() {
        controller.onNavigateUpClicked()
    }

    override fun onFragmentEvent(event: Any) {
        controller.onFragmentEvent(event)
    }

    override fun onDialogEvent(event: Any) {
        controller.onDialogEvent(event)
    }

    override fun onStepSelected() {
        // Nothing to do here.
    }

    override fun onReturnToPreviousStep() {
        // Nothing to do here.
    }

    override fun onStepError() {
        // Nothing to do here.
    }

    override fun onCompletedSteps() {
        controller.onCompletedSteps()
    }
}