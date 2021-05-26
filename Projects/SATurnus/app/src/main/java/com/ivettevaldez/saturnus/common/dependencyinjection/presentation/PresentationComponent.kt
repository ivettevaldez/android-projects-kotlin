package com.ivettevaldez.saturnus.common.dependencyinjection.presentation

import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.PersonSelectorBottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.main.MainActivity
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormFragment
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.InvoiceIssuingPeopleFragment
import com.ivettevaldez.saturnus.screens.invoices.list.InvoicesListFragment
import com.ivettevaldez.saturnus.screens.people.form.PersonFormFragment
import com.ivettevaldez.saturnus.screens.people.list.PeopleListFragment
import com.ivettevaldez.saturnus.screens.people.main.PeopleMainFragment
import com.ivettevaldez.saturnus.screens.splash.SplashFragment
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: InvoiceIssuingPeopleFragment)
    fun inject(fragment: InvoicesListFragment)
    fun inject(fragment: InvoiceFormFragment)
    fun inject(fragment: PeopleMainFragment)
    fun inject(fragment: PeopleListFragment)
    fun inject(fragment: PersonFormFragment)
    fun inject(dialog: InfoDialog)
    fun inject(dialog: PromptDialog)
    fun inject(dialog: PromptBottomSheetDialog)
    fun inject(dialog: PersonSelectorBottomSheetDialog)
}