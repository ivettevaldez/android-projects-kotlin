package com.ivettevaldez.saturnus.screens.common.controllers

import android.content.Context
import android.os.Handler
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.payment.GenerateInvoicePaymentUseCase
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.datepicker.DatePickerManager
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.main.MainController
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsController
import com.ivettevaldez.saturnus.screens.invoices.form.main.InvoiceFormMainController
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentController
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.InvoiceIssuingPeopleController
import javax.inject.Inject
import javax.inject.Provider

class ControllerFactory @Inject constructor(
    private val context: Provider<Context>,
    private val screensNavigator: Provider<ScreensNavigator>,
    private val fragmentsEventBus: Provider<FragmentsEventBus>,
    private val dialogsEventBus: Provider<DialogsEventBus>,
    private val dialogsManager: Provider<DialogsManager>,
    private val messagesHelper: Provider<MessagesHelper>,
    private val datePickerManager: Provider<DatePickerManager>,
    private val uiHandler: Provider<Handler>,
    private val personDao: Provider<PersonDao>,
    private val invoiceDao: Provider<InvoiceDao>,
    private val generateInvoicePaymentUseCase: Provider<GenerateInvoicePaymentUseCase>
) {

    fun newMainController(): MainController {
        return MainController(screensNavigator.get())
    }

    fun newInvoiceIssuingPeopleController(): InvoiceIssuingPeopleController {
        return InvoiceIssuingPeopleController(screensNavigator.get(), personDao.get())
    }

    fun newInvoiceFormMainController(): InvoiceFormMainController {
        return InvoiceFormMainController(
            context.get(),
            screensNavigator.get(),
            fragmentsEventBus.get(),
            dialogsEventBus.get(),
            dialogsManager.get(),
            messagesHelper.get(),
            uiHandler.get(),
            invoiceDao.get()
        )
    }

    fun newInvoiceFormDetailsController(): InvoiceFormDetailsController {
        return InvoiceFormDetailsController(
            context.get(),
            fragmentsEventBus.get(),
            dialogsManager.get(),
            datePickerManager.get(),
            personDao.get(),
            invoiceDao.get()
        )
    }

    fun newInvoiceFormPaymentController(): InvoiceFormPaymentController {
        return InvoiceFormPaymentController(
            context.get(),
            generateInvoicePaymentUseCase.get(),
            fragmentsEventBus.get(),
            invoiceDao.get()
        )
    }
}