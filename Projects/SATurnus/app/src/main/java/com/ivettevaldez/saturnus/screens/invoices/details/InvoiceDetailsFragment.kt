package com.ivettevaldez.saturnus.screens.invoices.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InvoiceDetailsFragment : BaseFragment(),
    IInvoiceDetailsViewMvc.Listener {

    @Inject
    lateinit var controllerFactory: ControllerFactory

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

    private lateinit var controller: InvoiceDetailsController
    private lateinit var viewMvc: IInvoiceDetailsViewMvc

    private lateinit var folio: String

    private var editionMode: Boolean = false

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

        controller = controllerFactory.newInvoiceDetailsController()

        bindArguments()
    }

    private fun bindArguments() {
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

        controller.bindView(viewMvc)
        bindInvoice()

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onResume() {
        super.onResume()
        controller.onResume()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onEditInvoiceClicked() {
        val issuingRfc = getInvoice()?.issuing?.rfc

        if (issuingRfc == null) {
            messagesHelper.showShortMessage(viewMvc.getRootView(), R.string.error_standard)
        } else {
            editionMode = true
            screensNavigator.toInvoiceForm(folio = folio)
        }
    }

    override fun onDeleteInvoiceClicked() {
        dialogsManager.showDeleteInvoiceConfirmation(null)
    }

    private fun getInvoice(): Invoice? = invoiceDao.findByFolio(folio)

    private fun bindInvoice() {
        viewMvc.showProgressIndicator()

        val invoice = getInvoice()
        if (invoice != null) {
            viewMvc.bindInvoice(invoice)
        }

        viewMvc.hideProgressIndicator()
    }
}