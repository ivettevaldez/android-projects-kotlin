package com.ivettevaldez.dependencyinjection.screens.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.common.CustomApplication
import com.ivettevaldez.dependencyinjection.common.composition.ActivityCompositionRoot
import com.ivettevaldez.dependencyinjection.common.composition.AppCompositionRoot

open class BaseActivity : AppCompatActivity() {

    private val appCompositionRoot: AppCompositionRoot
        get() = (application as CustomApplication).appCompositionRoot

    val compositionRoot by lazy {
        ActivityCompositionRoot(this, appCompositionRoot)
    }
}