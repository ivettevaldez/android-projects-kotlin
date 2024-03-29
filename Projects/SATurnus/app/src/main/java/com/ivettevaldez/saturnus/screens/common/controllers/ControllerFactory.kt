package com.ivettevaldez.saturnus.screens.common.controllers

import android.content.Context
import android.os.Handler
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.payment.calculator.InvoicePaymentCalculatorFactory
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.datepicker.DatePickerManager
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoController
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.IPersonSelectorBottomSheetViewMvc
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.PersonSelectorBottomSheetController
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.PersonSelectorBottomSheetDialog.PersonType
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptController
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetController
import com.ivettevaldez.saturnus.screens.common.main.MainController
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.invoices.details.InvoiceDetailsController
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsController
import com.ivettevaldez.saturnus.screens.invoices.form.main.InvoiceFormMainController
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentController
import com.ivettevaldez.saturnus.screens.invoices.issuingpeople.InvoiceIssuingPeopleController
import com.ivettevaldez.saturnus.screens.invoices.list.InvoicesListController
import com.ivettevaldez.saturnus.screens.people.form.PersonFormController
import com.ivettevaldez.saturnus.screens.people.list.PeopleListController
import com.ivettevaldez.saturnus.screens.people.main.PeopleMainController
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
    private val invoicePaymentCalculatorFactory: Provider<InvoicePaymentCalculatorFactory>
) {

    fun newMainController(): MainController {
        return MainController(
            screensNavigator.get()
        )
    }

    fun newInvoiceIssuingPeopleController(): InvoiceIssuingPeopleController {
        return InvoiceIssuingPeopleController(
            screensNavigator.get(),
            personDao.get()
        )
    }

    fun newInvoicesListController(): InvoicesListController {
        return InvoicesListController(
            screensNavigator.get(),
            dialogsManager.get(),
            personDao.get(),
            invoiceDao.get()
        )
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
            dialogsManager.get(),
            fragmentsEventBus.get(),
            invoicePaymentCalculatorFactory.get(),
            invoiceDao.get()
        )
    }

    fun newInvoiceDetailsController(): InvoiceDetailsController {
        return InvoiceDetailsController(
            screensNavigator.get(),
            dialogsManager.get(),
            dialogsEventBus.get(),
            messagesHelper.get(),
            uiHandler.get(),
            invoiceDao.get()
        )
    }

    fun newPeopleMainController(): PeopleMainController {
        return PeopleMainController(
            screensNavigator.get()
        )
    }

    fun newPeopleListController(): PeopleListController {
        return PeopleListController(
            screensNavigator.get(),
            dialogsManager.get(),
            dialogsEventBus.get(),
            personDao.get()
        )
    }

    fun newPersonFormController(): PersonFormController {
        return PersonFormController(
            context.get(),
            screensNavigator.get(),
            dialogsManager.get(),
            messagesHelper.get(),
            uiHandler.get(),
            personDao.get()
        )
    }

    fun newInfoController(): InfoController {
        return InfoController()
    }

    fun newPromptController(): PromptController {
        return PromptController(
            dialogsEventBus.get()
        )
    }

    fun newPromptBottomSheetController(): PromptBottomSheetController {
        return PromptBottomSheetController(
            dialogsEventBus.get()
        )
    }

    fun newPersonSelectorBottomSheetController(
        personType: PersonType,
        listener: IPersonSelectorBottomSheetViewMvc.Listener
    ): PersonSelectorBottomSheetController {
        return PersonSelectorBottomSheetController(
            personType,
            listener,
            context.get(),
            personDao.get()
        )
    }
}