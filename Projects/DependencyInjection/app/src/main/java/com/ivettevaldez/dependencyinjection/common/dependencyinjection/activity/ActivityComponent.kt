package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationModule
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent
}