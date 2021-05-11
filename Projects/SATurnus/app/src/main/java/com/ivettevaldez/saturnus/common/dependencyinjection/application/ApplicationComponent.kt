package com.ivettevaldez.saturnus.common.dependencyinjection.application

import com.ivettevaldez.saturnus.common.dependencyinjection.activity.ActivityComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.service.ServiceComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.service.ServiceModule
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun newActivityComponentBuilder(): ActivityComponent.Builder
    fun newServiceComponent(serviceModule: ServiceModule): ServiceComponent
}