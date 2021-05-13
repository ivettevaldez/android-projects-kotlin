package com.ivettevaldez.saturnus.screens.invoicing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoicingFragment : BaseFragment(),
    IInvoicingViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: IInvoicingViewMvc

    companion object {

        @JvmStatic
        fun newInstance() = InvoicingFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        viewMvc = viewMvcFactory.newInvoicingViewMvc(null)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
}