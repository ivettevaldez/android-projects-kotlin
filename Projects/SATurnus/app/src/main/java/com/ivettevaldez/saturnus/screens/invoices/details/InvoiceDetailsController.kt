package com.ivettevaldez.saturnus.screens.invoices.details

import android.os.Handler
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator

class InvoiceDetailsController(
    private val screensNavigator: ScreensNavigator,
    private val dialogsEventBus: DialogsEventBus,
    private val messagesHelper: MessagesHelper,
    private val handler: Handler,
    private val invoiceDao: InvoiceDao
) : IInvoiceDetailsViewMvc.Listener,
    DialogsEventBus.Listener {

    private lateinit var viewMvc: IInvoiceDetailsViewMvc

    lateinit var folio: String

    var editionMode: Boolean = false

    fun bindView(viewMvc: IInvoiceDetailsViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindArguments(folio: String) {
        this.folio = folio
    }

    fun onStart() {
        viewMvc.registerListener(this)
        dialogsEventBus.registerListener(this)
    }

    fun onResume() {
        if (editionMode) {
            bindInvoice()
        }
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
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

    override fun onNavigateUpClicked() {
        TODO("Not yet implemented")
    }

    override fun onEditInvoiceClicked() {
        TODO("Not yet implemented")
    }

    override fun onDeleteInvoiceClicked() {
        TODO("Not yet implemented")
    }

    private fun bindInvoice() {
        viewMvc.showProgressIndicator()

        val invoice = invoiceDao.findByFolio(folio)
        if (invoice != null) {
            viewMvc.bindInvoice(invoice)
        } else {
            throw IllegalArgumentException("@@@@@ Invalid folio: $folio")
        }

        viewMvc.hideProgressIndicator()
    }

    private fun deleteInvoice() {
        invoiceDao.delete(folio)

        messagesHelper.showShortMessage(
            viewMvc.getRootView(),
            R.string.message_deleted_invoice
        )

        handler.postDelayed({
            screensNavigator.navigateUp()
        }, Constants.SPLASH_NAVIGATION_DELAY)
    }
}