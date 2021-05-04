package com.ivettevaldez.dependencyinjection.common.dependencyinjection.application

import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.ActivityComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.ActivityModule
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun newActivityComponent(activityModule: ActivityModule): ActivityComponent
}