package com.ivettevaldez.coroutines

import android.app.Application
import com.ivettevaldez.coroutines.common.dependencyinjection.ApplicationCompositionRoot

class CustomApplication : Application() {

    val applicationCompositionRoot = ApplicationCompositionRoot()

}