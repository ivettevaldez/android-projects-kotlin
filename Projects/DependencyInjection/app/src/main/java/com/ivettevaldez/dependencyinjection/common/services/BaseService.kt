package com.ivettevaldez.dependencyinjection.common.services

import android.app.Service
import com.ivettevaldez.dependencyinjection.common.CustomApplication
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.service.ServiceComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.service.ServiceModule

abstract class BaseService : Service() {

    private val applicationComponent: ApplicationComponent by lazy {
        (application as CustomApplication).applicationComponent
    }

    val serviceComponent: ServiceComponent by lazy {
        applicationComponent.newServiceComponent(ServiceModule(this))
    }
}