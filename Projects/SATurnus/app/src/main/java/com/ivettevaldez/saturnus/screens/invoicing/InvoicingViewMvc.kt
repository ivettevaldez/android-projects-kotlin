package com.ivettevaldez.saturnus.screens.invoicing

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IInvoicingViewMvc : IObservableViewMvc<IInvoicingViewMvc.Listener> {

    interface Listener
}

class InvoicingViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IInvoicingViewMvc.Listener>(
    inflater,
    parent,
    R.layout.fragment_invoicing
), IInvoicingViewMvc