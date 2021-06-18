package com.ivettevaldez.saturnus.screens.invoices.form.main

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
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormChangeFragmentEvent
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentFragmentEvent
import javax.inject.Inject

class InvoiceFormMainFragment : BaseFragment(),
    IInvoiceFormMainViewMvc.Listener,
    FragmentsEventBus.Listener,
    DialogsEventBus.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var fragmentsEventBus: FragmentsEventBus

    @Inject
    lateinit var dialogsEventBus: DialogsEventBus

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var messagesHelper: MessagesHelper

    @Inject
    lateinit var invoiceDao: InvoiceDao

    private lateinit var viewMvc: IInvoiceFormMainViewMvc

    private var folio: String? = null
    private var issuingRfc: String? = null
    private var invoice: Invoice? = null
    private var hasFormChanges: Boolean = false

    companion object {

        private const val ARG_FOLIO = "ARG_FOLIO"
        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"

        @JvmStatic
        fun newInstance(folio: String?, issuingRfc: String?) =
            InvoiceFormMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FOLIO, folio)
                    putString(ARG_ISSUING_RFC, issuingRfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            issuingRfc = it.getString(ARG_ISSUING_RFC)
            folio = it.getString(ARG_FOLIO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // FIXME: The complete form disappears when this screen is opened more than once.

        viewMvc = viewMvcFactory.newInvoiceFormMainViewMvc(parent)

        var toolbarTitle: String = getString(R.string.invoices_new)

        if (folio != null) {
            invoice = invoiceDao.findByFolio(folio!!)
            issuingRfc = invoice?.issuing?.rfc

            toolbarTitle = getString(R.string.action_editing)
        }

        viewMvc.initStepper(folio, issuingRfc)
        viewMvc.setToolbarTitle(toolbarTitle)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)
        fragmentsEventBus.registerListener(this)
        dialogsEventBus.registerListener(this)
    }

    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)
        fragmentsEventBus.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
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
                    // Nothing to do here.
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

        Handler(Looper.getMainLooper()).postDelayed({
            screensNavigator.navigateUp()
        }, Constants.SHOW_MESSAGE_DELAY)
    }
}