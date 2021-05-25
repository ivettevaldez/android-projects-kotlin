package com.ivettevaldez.saturnus.screens.common.viewsmvc

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.UtilsHelper
import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoDialogViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.IPromptBottomSheetViewMvc
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.saturnus.screens.common.navigation.NavDrawerViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.toolbar.ToolbarViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.IInvoiceIssuingPeopleViewMvc
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.InvoiceIssuingPeopleViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.list.IInvoiceListItemViewMvc
import com.ivettevaldez.saturnus.screens.invoices.list.IInvoicesListViewMvc
import com.ivettevaldez.saturnus.screens.invoices.list.InvoiceListItemViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.list.InvoicesListViewMvcImpl
import com.ivettevaldez.saturnus.screens.people.form.IPersonFormViewMvc
import com.ivettevaldez.saturnus.screens.people.form.PersonFormViewMvcImpl
import com.ivettevaldez.saturnus.screens.people.list.IPeopleListItemViewMvc
import com.ivettevaldez.saturnus.screens.people.list.IPeopleListViewMvc
import com.ivettevaldez.saturnus.screens.people.list.PeopleListItemViewMvcImpl
import com.ivettevaldez.saturnus.screens.people.list.PeopleListViewMvcImpl
import com.ivettevaldez.saturnus.screens.people.main.IPeopleMainViewMvc
import com.ivettevaldez.saturnus.screens.people.main.PeopleMainViewMvcImpl
import com.ivettevaldez.saturnus.screens.splash.SplashViewMvc
import javax.inject.Inject
import javax.inject.Provider

class ViewMvcFactory @Inject constructor(
    private val inflater: Provider<LayoutInflater>,
    private val uiHandler: Provider<Handler>,
    private val utilsHelper: Provider<UtilsHelper>,
    private val navDrawerHelper: Provider<INavDrawerHelper>
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

    fun newPromptBottomSheetDialogViewMvc(parent: ViewGroup?): IPromptBottomSheetViewMvc {
        return PromptBottomSheetViewMvcImpl(inflater.get(), parent)
    }

    fun newSplashViewMvc(parent: ViewGroup?): SplashViewMvc {
        return SplashViewMvc(inflater.get(), parent)
    }

    fun newInvoiceIssuingPeopleViewMvc(parent: ViewGroup?): IInvoiceIssuingPeopleViewMvc {
        return InvoiceIssuingPeopleViewMvcImpl(
            inflater.get(),
            parent,
            uiHandler.get(),
            utilsHelper.get(),
            navDrawerHelper.get(),
            this
        )
    }

    fun newInvoicesListViewMvc(parent: ViewGroup?): IInvoicesListViewMvc {
        return InvoicesListViewMvcImpl(
            inflater.get(),
            parent,
            uiHandler.get(),
            utilsHelper.get(),
            this
        )
    }

    fun newInvoiceListItemViewMvc(parent: ViewGroup?): IInvoiceListItemViewMvc {
        return InvoiceListItemViewMvcImpl(inflater.get(), parent)
    }

    fun newPeopleMainViewMvc(parent: ViewGroup?): IPeopleMainViewMvc {
        return PeopleMainViewMvcImpl(inflater.get(), parent, this)
    }

    fun newPeopleListViewMvc(parent: ViewGroup?): IPeopleListViewMvc {
        return PeopleListViewMvcImpl(
            inflater.get(),
            parent,
            uiHandler.get(),
            utilsHelper.get(),
            this
        )
    }

    fun newPeopleListItemViewMvc(parent: ViewGroup?): IPeopleListItemViewMvc {
        return PeopleListItemViewMvcImpl(inflater.get(), parent)
    }

    fun newPersonFormViewMvc(parent: ViewGroup?): IPersonFormViewMvc {
        return PersonFormViewMvcImpl(inflater.get(), parent, this)
    }
}