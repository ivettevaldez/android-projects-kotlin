package com.ivettevaldez.saturnus.screens.invoices.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceFormFragment : BaseFragment(),
    IInvoiceFormViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    private lateinit var viewMvc: IInvoiceFormViewMvc
    private lateinit var issuingRfc: String

    companion object {

        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"

        @JvmStatic
        fun newInstance(issuingRfc: String) =
            InvoiceFormFragment().apply {
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

        viewMvc = viewMvcFactory.newInvoiceFormViewMvc(parent)
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
}