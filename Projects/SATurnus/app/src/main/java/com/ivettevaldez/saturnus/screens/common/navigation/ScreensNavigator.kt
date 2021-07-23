package com.ivettevaldez.saturnus.screens.common.navigation

import com.ivettevaldez.saturnus.screens.common.fragmentframehelper.FragmentFrameHelper
import com.ivettevaldez.saturnus.screens.invoices.details.InvoiceDetailsFragment
import com.ivettevaldez.saturnus.screens.invoices.form.main.InvoiceFormMainFragment
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.InvoiceIssuingPeopleFragment
import com.ivettevaldez.saturnus.screens.invoices.list.InvoicesListFragment
import com.ivettevaldez.saturnus.screens.people.form.PersonFormFragment
import com.ivettevaldez.saturnus.screens.people.main.PeopleMainFragment
import com.ivettevaldez.saturnus.screens.splash.SplashFragment
import javax.inject.Inject

open class ScreensNavigator @Inject constructor(
    private val fragmentFrameHelper: FragmentFrameHelper
) {

    open fun navigateUp() {
        fragmentFrameHelper.navigateUp()
    }

    open fun toSplash() {
        fragmentFrameHelper.replaceFragmentAndClearBackstack(
            SplashFragment.newInstance()
        )
    }

    open fun toInvoiceIssuingPeople() {
        fragmentFrameHelper.replaceFragmentAndClearBackstack(
            InvoiceIssuingPeopleFragment.newInstance()
        )
    }

    open fun toInvoicesList(rfc: String) {
        fragmentFrameHelper.replaceFragment(
            InvoicesListFragment.newInstance(rfc)
        )
    }

    open fun toInvoiceDetails(folio: String) {
        fragmentFrameHelper.replaceFragment(
            InvoiceDetailsFragment.newInstance(folio)
        )
    }

    open fun toInvoiceForm(folio: String? = null, issuingRfc: String? = null) {
        fragmentFrameHelper.replaceFragment(
            InvoiceFormMainFragment.newInstance(folio, issuingRfc)
        )
    }

    open fun toPeople() {
        fragmentFrameHelper.replaceFragment(
            PeopleMainFragment.newInstance()
        )
    }

    open fun toPersonForm(rfc: String?) {
        fragmentFrameHelper.replaceFragment(
            PersonFormFragment.newInstance(rfc)
        )
    }
}