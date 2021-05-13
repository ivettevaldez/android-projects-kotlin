package com.ivettevaldez.saturnus.screens.common.navigation

import com.ivettevaldez.saturnus.screens.common.fragmentframehelper.FragmentFrameHelper
import com.ivettevaldez.saturnus.screens.invoicing.InvoicingFragment
import javax.inject.Inject

class ScreensNavigator @Inject constructor(private val fragmentFrameHelper: FragmentFrameHelper) {

    fun toInvoicing() {
        val fragment = InvoicingFragment.newInstance()
        fragmentFrameHelper.replaceFragmentAndClearBackstack(fragment)
    }
}