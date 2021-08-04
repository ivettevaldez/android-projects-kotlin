package com.ivettevaldez.saturnus.screens.common.viewsmvc

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.IPersonSelectorBottomSheetViewMvc
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.IPersonSelectorListItemViewMvc
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.PersonSelectorBottomSheetViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.PersonSelectorListItemViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.IPromptBottomSheetViewMvc
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.fields.ISpinnerInputViewMvc
import com.ivettevaldez.saturnus.screens.common.fields.SimpleTextInputViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.fields.SpinnerInputViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.saturnus.screens.common.navigation.NavDrawerViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.toolbar.ToolbarViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.InvoicesHelper
import com.ivettevaldez.saturnus.screens.invoices.details.IInvoiceDetailsViewMvc
import com.ivettevaldez.saturnus.screens.invoices.details.InvoiceDetailsViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.form.details.IInvoiceFormDetailsViewMvc
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.form.main.IInvoiceFormMainViewMvc
import com.ivettevaldez.saturnus.screens.invoices.form.main.InvoiceFormMainViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.form.payment.IInvoiceFormPaymentViewMvc
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.IInvoiceIssuingPeopleViewMvc
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.InvoiceIssuingPeopleViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.list.IInvoiceListItemViewMvc
import com.ivettevaldez.saturnus.screens.invoices.list.IInvoicesListViewMvc
import com.ivettevaldez.saturnus.screens.invoices.list.InvoiceListItemViewMvcImpl
import com.ivettevaldez.saturnus.screens.invoices.list.InvoicesListViewMvcImpl
import com.ivettevaldez.saturnus.screens.people.form.IPersonFormViewMvc
import com.ivettevaldez.saturnus.screens.people.form.PersonFormViewMvcImpl
import com.ivettevaldez.saturnus.screens.people.item.IPersonItemViewMvc
import com.ivettevaldez.saturnus.screens.people.item.PersonItemViewMvcImpl
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
    private val fragmentManager: FragmentManager,
    private val uiHandler: Provider<Handler>,
    private val navDrawerHelper: Provider<INavDrawerHelper>,
    private val invoicesHelper: Provider<InvoicesHelper>
) {

    fun newNavDrawerViewMvc(parent: ViewGroup?): INavDrawerViewMvc {
        return NavDrawerViewMvcImpl(inflater.get(), parent)
    }

    fun newToolbarViewMvc(parent: ViewGroup?): IToolbarViewMvc {
        return ToolbarViewMvcImpl(inflater.get(), parent)
    }

    fun newSimpleTextInputViewMvc(parent: ViewGroup?): SimpleTextInputViewMvcImpl {
        return SimpleTextInputViewMvcImpl(inflater.get(), parent)
    }

    fun newSpinnerInputViewMvc(parent: ViewGroup?): ISpinnerInputViewMvc {
        return SpinnerInputViewMvcImpl(inflater.get(), parent)
    }

    fun newInfoViewMvc(parent: ViewGroup?): InfoViewMvcImpl {
        return InfoViewMvcImpl(inflater.get(), parent)
    }

    fun newPromptViewMvc(parent: ViewGroup?): PromptViewMvcImpl {
        return PromptViewMvcImpl(inflater.get(), parent)
    }

    fun newPromptBottomSheetViewMvc(parent: ViewGroup?): IPromptBottomSheetViewMvc {
        return PromptBottomSheetViewMvcImpl(inflater.get(), parent)
    }

    fun newPersonSelectorBottomSheetViewMvc(parent: ViewGroup?):
            IPersonSelectorBottomSheetViewMvc {
        return PersonSelectorBottomSheetViewMvcImpl(inflater.get(), parent, this)
    }

    fun newPersonSelectorListItemViewMvc(parent: ViewGroup?): IPersonSelectorListItemViewMvc {
        return PersonSelectorListItemViewMvcImpl(inflater.get(), parent, this)
    }

    fun newPersonItemViewMvc(parent: ViewGroup?): IPersonItemViewMvc {
        return PersonItemViewMvcImpl(inflater.get(), parent)
    }

    fun newSplashViewMvc(parent: ViewGroup?): SplashViewMvc {
        return SplashViewMvc(inflater.get(), parent)
    }

    fun newInvoiceIssuingPeopleViewMvc(parent: ViewGroup?): IInvoiceIssuingPeopleViewMvc {
        return InvoiceIssuingPeopleViewMvcImpl(
            inflater.get(),
            parent,
            uiHandler.get(),
            navDrawerHelper.get(),
            this
        )
    }

    fun newInvoicesListViewMvc(parent: ViewGroup?): IInvoicesListViewMvc {
        return InvoicesListViewMvcImpl(
            inflater.get(),
            parent,
            uiHandler.get(),
            this
        )
    }

    fun newInvoiceListItemViewMvc(parent: ViewGroup?): IInvoiceListItemViewMvc {
        return InvoiceListItemViewMvcImpl(inflater.get(), parent, invoicesHelper.get())
    }

    fun newInvoiceDetailsViewMvc(parent: ViewGroup?): IInvoiceDetailsViewMvc {
        return InvoiceDetailsViewMvcImpl(inflater.get(), parent, this, invoicesHelper.get())
    }

    fun newInvoiceFormMainViewMvc(parent: ViewGroup?): IInvoiceFormMainViewMvc {
        return InvoiceFormMainViewMvcImpl(inflater.get(), parent, this, fragmentManager)
    }

    fun newInvoiceFormDetailsViewMvc(parent: ViewGroup?): IInvoiceFormDetailsViewMvc {
        return InvoiceFormDetailsViewMvcImpl(inflater.get(), parent, this)
    }

    fun newInvoiceFormPaymentViewMvc(parent: ViewGroup?): IInvoiceFormPaymentViewMvc {
        return InvoiceFormPaymentViewMvcImpl(inflater.get(), parent, this)
    }

    fun newPeopleMainViewMvc(parent: ViewGroup?): IPeopleMainViewMvc {
        return PeopleMainViewMvcImpl(inflater.get(), parent, this)
    }

    fun newPeopleListViewMvc(parent: ViewGroup?): IPeopleListViewMvc {
        return PeopleListViewMvcImpl(
            inflater.get(),
            parent,
            uiHandler.get(),
            this
        )
    }

    fun newPeopleListItemViewMvc(parent: ViewGroup?): IPeopleListItemViewMvc {
        return PeopleListItemViewMvcImpl(inflater.get(), parent, this)
    }

    fun newPersonFormViewMvc(parent: ViewGroup?): IPersonFormViewMvc {
        return PersonFormViewMvcImpl(inflater.get(), parent, this)
    }
}