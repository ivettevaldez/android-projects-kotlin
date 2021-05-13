package com.ivettevaldez.saturnus.screens.common.viewsmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoDialogViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.saturnus.screens.common.navigation.NavDrawerViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.toolbar.ToolbarViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoicing.IInvoicingViewMvc
import com.ivettevaldez.saturnus.screens.invoicing.InvoicingViewMvcImpl
import javax.inject.Inject
import javax.inject.Provider

class ViewMvcFactory @Inject constructor(
    private val inflater: Provider<LayoutInflater>
) {

    fun newNavDrawerViewMvc(parent: ViewGroup?): INavDrawerViewMvc {
        return NavDrawerViewMvcImpl(inflater.get(), parent)
    }

    fun newToolbarViewMvc(parent: ViewGroup?): IToolbarViewMvc {
        return ToolbarViewMvcImpl(inflater.get(), parent)
    }

    fun newInfoDialogViewMvc(parent: ViewGroup?): InfoDialogViewMvcImpl {
        return InfoDialogViewMvcImpl(inflater.get(), parent)
    }

    fun newPromptDialogViewMvc(parent: ViewGroup?): PromptDialogViewMvcImpl {
        return PromptDialogViewMvcImpl(inflater.get(), parent)
    }

    fun newInvoicingViewMvc(parent: ViewGroup?): IInvoicingViewMvc {
        return InvoicingViewMvcImpl(inflater.get(), parent)
    }
}