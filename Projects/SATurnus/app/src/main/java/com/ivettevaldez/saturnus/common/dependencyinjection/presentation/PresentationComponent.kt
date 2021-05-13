package com.ivettevaldez.saturnus.common.dependencyinjection.presentation

import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialog
import com.ivettevaldez.saturnus.screens.common.main.MainActivity
import com.ivettevaldez.saturnus.screens.invoicing.InvoicingFragment
import com.ivettevaldez.saturnus.screens.people.PeopleFragment
import com.ivettevaldez.saturnus.screens.splash.SplashFragment
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: InvoicingFragment)
    fun inject(fragment: PeopleFragment)
    fun inject(dialog: InfoDialog)
    fun inject(dialog: PromptDialog)
}