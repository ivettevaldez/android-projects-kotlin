package com.ivettevaldez.saturnus.screens.common.controllers

import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.main.MainController
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.InvoiceIssuingPeopleController
import javax.inject.Inject
import javax.inject.Provider

class ControllerFactory @Inject constructor(
    private val screensNavigator: Provider<ScreensNavigator>,
    private val personDao: Provider<PersonDao>
) {

    fun newMainController(): MainController {
        return MainController(screensNavigator.get())
    }

    fun newInvoiceIssuingPeopleController(): InvoiceIssuingPeopleController {
        return InvoiceIssuingPeopleController(screensNavigator.get(), personDao.get())
    }
}