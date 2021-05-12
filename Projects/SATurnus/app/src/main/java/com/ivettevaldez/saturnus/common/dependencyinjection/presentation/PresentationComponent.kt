package com.ivettevaldez.saturnus.common.dependencyinjection.presentation

import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoDialog
import com.ivettevaldez.saturnus.screens.common.main.MainActivity
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {

    fun inject(activity: MainActivity)
    fun inject(dialog: InfoDialog)
}