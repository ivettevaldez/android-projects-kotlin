package com.ivettevaldez.saturnus.screens.invoices.form.main

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceFormMainFragment : BaseFragment(),
    IInvoiceFormMainViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    private lateinit var viewMvc: IInvoiceFormMainViewMvc
    private lateinit var issuingRfc: String

    companion object {

        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"

        @JvmStatic
        fun newInstance(issuingRfc: String) =
            InvoiceFormMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ISSUING_RFC, issuingRfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            issuingRfc = it.getString(ARG_ISSUING_RFC)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormMainViewMvc(parent)
        viewMvc.initStepper(issuingRfc)

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

    override fun onCompletedInvoice() {
        // TODO:
    }

    override fun onStepError() {
        // TODO:
    }

    override fun onStepSelected() {
        // TODO:
    }

    override fun onReturnToPreviousStep() {
        // TODO:
    }
}