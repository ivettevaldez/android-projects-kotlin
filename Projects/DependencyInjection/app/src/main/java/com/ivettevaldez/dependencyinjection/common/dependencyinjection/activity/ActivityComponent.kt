package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun newPresentationComponent(): PresentationComponent
}