package com.ivettevaldez.saturnus.screens.common.navigation

import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.screens.common.fragmentframehelper.FragmentFrameHelper
import com.ivettevaldez.saturnus.screens.invoicing.InvoicingFragment
import com.ivettevaldez.saturnus.screens.people.form.PersonFormFragment
import com.ivettevaldez.saturnus.screens.people.main.PeopleMainFragment
import com.ivettevaldez.saturnus.screens.splash.SplashFragment
import javax.inject.Inject

class ScreensNavigator @Inject constructor(private val fragmentFrameHelper: FragmentFrameHelper) {

    fun navigateUp() {
        fragmentFrameHelper.navigateUp()
    }

    fun toSplash() {
        fragmentFrameHelper.replaceFragmentAndClearBackstack(
            SplashFragment.newInstance()
        )
    }

    fun toInvoicing() {
        fragmentFrameHelper.replaceFragmentAndClearBackstack(
            InvoicingFragment.newInstance()
        )
    }

    fun toPeople() {
        fragmentFrameHelper.replaceFragment(
            PeopleMainFragment.newInstance()
        )
    }

    fun toPersonForm(rfc: String?, clientType: ClientType.Type) {
        fragmentFrameHelper.replaceFragment(
            PersonFormFragment.newInstance(rfc, clientType)
        )
    }
}