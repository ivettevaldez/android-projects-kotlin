package com.ivettevaldez.saturnus.screens.invoices.details

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceDetailsFragment : BaseFragment(),
    IInvoiceDetailsViewMvc.Listener,
    DialogsEventBus.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var dialogsEventBus: DialogsEventBus

    @Inject
    lateinit var messagesHelper: MessagesHelper

    @Inject
    lateinit var invoiceDao: InvoiceDao

    private lateinit var viewMvc: IInvoiceDetailsViewMvc
    private lateinit var folio: String

    companion object {

        private const val ARG_FOLIO = "ARG_FOLIO"

        @JvmStatic
        fun newInstance(folio: String) =
            InvoiceDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FOLIO, folio)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            folio = it.getString(ARG_FOLIO)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceDetailsViewMvc(parent)

        bindInvoice()

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)
        dialogsEventBus.registerListener(this)
    }

    override fun onStop() {
        super.onStop()

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
            }
        }
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onDeleteInvoiceClicked() {
        dialogsManager.showDeleteInvoiceConfirmation(null)
    }

    private fun getInvoice(): Invoice? = invoiceDao.findByFolio(folio)

    private fun bindInvoice() {
        val invoice = getInvoice()
        if (invoice != null) {
            viewMvc.bindInvoice(invoice)
        }
    }

    private fun deleteInvoice() {
        invoiceDao.delete(folio)

        messagesHelper.showShortMessage(viewMvc.getRootView(), R.string.message_deleted_invoice)

        Handler(Looper.getMainLooper()).postDelayed({
            screensNavigator.navigateUp()
        }, Constants.SHOW_MESSAGE_DELAY)
    }
}