package com.ivettevaldez.cleanarchitecture.common.dependencyinjection

import androidx.fragment.app.FragmentActivity
import com.ivettevaldez.cleanarchitecture.common.permissions.PermissionsHelper
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsEventBus

class ActivityCompositionRoot(
    val activity: FragmentActivity,
    private val compositionRoot: CompositionRoot
) {

    private var permissionsHelper: PermissionsHelper? = null

    fun getStackOverflowApi(): StackOverflowApi {
        return compositionRoot.getStackOverflowApi()
    }

    fun getDialogsEventBus(): DialogsEventBus {
        return DialogsEventBus()
    }

    fun getPermissionsHelper(): PermissionsHelper {
        if (permissionsHelper == null) {
            permissionsHelper = PermissionsHelper(activity)
        }
        return permissionsHelper!!
    }
}