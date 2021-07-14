package com.ivettevaldez.saturnus.screens.invoices.form.main

import android.content.Context
import android.os.Handler
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormChangeFragmentEvent
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentFragmentEvent

class InvoiceFormMainController(
    private val context: Context,
    private val screensNavigator: ScreensNavigator,
    private val fragmentsEventBus: FragmentsEventBus,
    private val dialogsEventBus: DialogsEventBus,
    private val dialogsManager: DialogsManager,
    private val messagesHelper: MessagesHelper,
    private val uiHandler: Handler,
    private val invoiceDao: InvoiceDao
) : IInvoiceFormMainViewMvc.Listener,
    FragmentsEventBus.Listener,
    DialogsEventBus.Listener {

    private lateinit var viewMvc: IInvoiceFormMainViewMvc

    var issuingRfc: String? = null
    var folio: String? = null
    var invoice: Invoice? = null
    var hasFormChanges: Boolean = false

    var newInvoice: Boolean = false
    var editingInvoice: Boolean = false

    fun bindArguments(issuingRfc: String?, folio: String?) {
        this.issuingRfc = issuingRfc
        this.folio = folio

        if (folio == null) {
            newInvoice = true
        } else {
            editingInvoice = true
        }

        if (editingInvoice) {
            findInvoiceAndIssuingRfc()
        }
    }

    fun findInvoiceAndIssuingRfc() {
        invoice = invoiceDao.findByFolio(folio!!)
        issuingRfc = invoice?.issuing?.rfc
    }

    fun bindView(viewMvc: IInvoiceFormMainViewMvc) {
        this.viewMvc = viewMvc
    }

    fun onStart() {
        viewMvc.registerListener(this)
        fragmentsEventBus.registerListener(this)
        dialogsEventBus.registerListener(this)
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
        fragmentsEventBus.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
    }

    fun getToolbarTitle(): String = if (newInvoice) {
        context.getString(R.string.invoices_new)
    } else {
        context.getString(R.string.action_editing)
    }

    fun setToolbarTitle() {
        viewMvc.setToolbarTitle(getToolbarTitle())
    }

    fun initStepper() {
        viewMvc.initStepper(folio, issuingRfc)
    }

    override fun onNavigateUpClicked() {
        if (hasFormChanges) {
            dialogsManager.showExitWithoutSavingChangesConfirmation(null)
        } else {
            screensNavigator.navigateUp()
        }
    }

    override fun onFragmentEvent(event: Any) {
        if (event is InvoiceFormChangeFragmentEvent) {
            hasFormChanges = true
        } else if (event is InvoiceFormPaymentFragmentEvent) {
            invoice = event.invoice
        }
    }

    override fun onDialogEvent(event: Any) {
        if (event is PromptDialogEvent) {
            when (event.clickedButton) {
                PromptDialogEvent.Button.POSITIVE -> {
                    screensNavigator.navigateUp()
                }
                PromptDialogEvent.Button.NEGATIVE -> {
                    // Nothing to do here
                }
                else -> {
                    throw RuntimeException("Unsupported Button type: ${event.clickedButton}")
                }
            }
        }
    }

    override fun onStepSelected() {
        // Nothing to do here.
    }

    override fun onReturnToPreviousStep() {
        // Nothing to do here.
    }

    override fun onStepError() {
        // Nothing to do here.
    }

    override fun onCompletedSteps() {
        invoiceDao.save(invoice!!)

        messagesHelper.showShortMessage(viewMvc.getRootView(), R.string.message_saved)

        uiHandler.postDelayed({
            screensNavigator.navigateUp()
        }, Constants.SHOW_MESSAGE_DELAY)
    }
}