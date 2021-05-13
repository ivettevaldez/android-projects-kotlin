package com.ivettevaldez.saturnus.screens.invoicing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IInvoicingViewMvc : IObservableViewMvc<IInvoicingViewMvc.Listener> {

    interface Listener
}

class InvoicingViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory,
    private val navDrawerHelper: INavDrawerHelper
) : BaseObservableViewMvc<IInvoicingViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoicing
), IInvoicingViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    init {

        initToolbar()
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(
            context.getString(R.string.menu_invoicing)
        )

        toolbarViewMvc.enableMenuAndListen(object : IToolbarViewMvc.MenuClickListener {
            override fun onMenuClicked() {
                for (listener in listeners) {
                    navDrawerHelper.openDrawer()
                }
            }
        })

        toolbar.addView(toolbarViewMvc.getRootView())
    }
}