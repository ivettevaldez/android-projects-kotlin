package com.ivettevaldez.saturnus.screens.invoices.details

import android.os.Handler
import android.util.Log
import androidx.annotation.StringRes
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator

class InvoiceDetailsController(
    private val screensNavigator: ScreensNavigator,
    private val dialogsManager: DialogsManager,
    private val dialogsEventBus: DialogsEventBus,
    private val messagesHelper: MessagesHelper,
    private val handler: Handler,
    private val invoiceDao: InvoiceDao
) : IInvoiceDetailsViewMvc.Listener,
    DialogsEventBus.Listener {

    private val classTag: String = this::class.java.simpleName

    private lateinit var viewMvc: IInvoiceDetailsViewMvc

    lateinit var folio: String

    fun bindView(viewMvc: IInvoiceDetailsViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindFolio(folio: String) {
        this.folio = folio
    }

    fun onStart() {
        viewMvc.registerListener(this)
        dialogsEventBus.registerListener(this)

        bindInvoice()
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onEditInvoiceClicked() {
        screensNavigator.toInvoiceForm(folio = folio)
    }

    override fun onDeleteInvoiceClicked() {
        dialogsManager.showDeleteInvoiceConfirmation(null)
    }

    override fun onDialogEvent(event: Any) {
        if (event is PromptDialogEvent) {
            when (event.clickedButton) {
                PromptDialogEvent.Button.POSITIVE -> {
                    deleteInvoice()
                }
                PromptDialogEvent.Button.NEGATIVE -> {
                    // Nothing to do here.
                }
                else -> {
                    throw IllegalArgumentException(
                        "@@@@@ Unsupported clicked button: ${event.clickedButton}"
                    )
                }
            }
        }
    }

    private fun getInvoice(): Invoice? = invoiceDao.findByFolio(folio)

    private fun bindInvoice() {
        viewMvc.showProgressIndicator()

        val invoice = getInvoice()
        if (invoice != null) {
            viewMvc.bindInvoice(invoice)
        } else {
            Log.e(classTag, "@@@@@ Invalid folio: $folio")
            showShortMessage(R.string.error_standard)
        }

        viewMvc.hideProgressIndicator()
    }

    private fun deleteInvoice() {
        invoiceDao.delete(folio)

        showShortMessage(R.string.message_deleted_invoice)

        handler.postDelayed({
            screensNavigator.navigateUp()
        }, Constants.SPLASH_NAVIGATION_DELAY)
    }

    private fun showShortMessage(@StringRes id: Int) {
        messagesHelper.showShortMessage(viewMvc.getRootView(), id)
    }
}