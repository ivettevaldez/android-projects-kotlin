package com.ivettevaldez.saturnus.common.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ivettevaldez.saturnus.common.CustomApplication
import com.ivettevaldez.saturnus.common.dependencyinjection.service.ServiceComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.service.ServiceModule

open class BaseService : Service() {

    private val applicationComponent by lazy {
        (application as CustomApplication).applicationComponent
    }

    val serviceComponent: ServiceComponent by lazy {
        applicationComponent.newServiceComponent(
            ServiceModule(this)
        )
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}