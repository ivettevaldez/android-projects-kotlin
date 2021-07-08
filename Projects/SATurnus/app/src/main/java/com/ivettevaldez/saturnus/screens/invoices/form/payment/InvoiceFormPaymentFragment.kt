package com.ivettevaldez.saturnus.screens.invoices.form.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toDoubleValue
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.InvoicePayment
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsFragmentEvent
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import javax.inject.Inject

class InvoiceFormPaymentFragment : BaseFragment(),
    Step,
    IInvoiceFormPaymentViewMvc.Listener,
    FragmentsEventBus.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var fragmentsEventBus: FragmentsEventBus

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var invoiceDao: InvoiceDao

    private lateinit var viewMvc: IInvoiceFormPaymentViewMvc

    private var folio: String? = null
    private var invoice: Invoice? = null
    private var receiverPersonType: String? = null
    private var subtotal: Double? = null
    private var hasChanges = false

    companion object {

        private const val ARG_FOLIO = "ARG_FOLIO"

        @JvmStatic
        fun newInstance(folio: String?) =
            InvoiceFormPaymentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FOLIO, folio)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        requireArguments().let {
            folio = it.getString(ARG_FOLIO)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormPaymentViewMvc(parent)

        if (folio != null) {
            invoice = invoiceDao.findByFolio(folio!!)

            if (invoice != null) {
                subtotal = invoice!!.payment!!.subtotal

                bindPayment(
                    invoice!!.payment!!.subtotal.toCurrency(),
                    invoice!!.payment!!.iva.toCurrency(),
                    invoice!!.payment!!.ivaWithholding.toCurrency(),
                    invoice!!.payment!!.isrWithholding.toCurrency(),
                    invoice!!.payment!!.total.toCurrency()
                )
            }
        }

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)
        fragmentsEventBus.registerListener(this)
    }

    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)
        fragmentsEventBus.unregisterListener(this)
    }

    override fun onFragmentEvent(event: Any) {
        if (event is InvoiceFormDetailsFragmentEvent) {
            hasChanges = true

            invoice = event.invoice
            receiverPersonType = invoice?.receiver?.personType
        }
    }

    override fun verifyStep(): VerificationError? {
        val error = hasFormErrors()
        return if (error != null) {
            return VerificationError(error)
        } else {
            returnCompleteInvoice()
            null
        }
    }

    override fun onSelected() {
        // Nothing to do here.
    }

    override fun onError(error: VerificationError) {
        dialogsManager.showGenericSavingError(null, error.errorMessage)
    }

    override fun onSubtotalChanged() {
        hasChanges = true
    }

    override fun onCalculateClicked(subtotal: String) {
        this.subtotal = subtotal.toDoubleValue()
        val invoicePayment = generateInvoicePayment(this.subtotal!!)

        invoice?.payment = invoicePayment

        val ivaWithholding: String
        val isrWithholding: String

        if (receiverPersonType == Constants.MORAL_PERSON) {
            ivaWithholding = invoicePayment.ivaWithholding.toCurrency()
            isrWithholding = invoicePayment.isrWithholding.toCurrency()
        } else {
            ivaWithholding = getString(R.string.default_unavailable)
            isrWithholding = getString(R.string.default_unavailable)
        }

        bindPayment(
            invoicePayment.subtotal.toCurrency(),
            invoicePayment.iva.toCurrency(),
            ivaWithholding,
            isrWithholding,
            invoicePayment.total.toCurrency()
        )

        hasChanges = false
    }

    private fun setDefaults() {
        val defaultZero = getString(R.string.default_zero).toCurrency()
        val defaultWithholding = if (receiverPersonType == Constants.PHYSICAL_PERSON) {
            getString(R.string.default_unavailable)
        } else {
            defaultZero
        }

        bindPayment(
            defaultZero,
            defaultZero,
            defaultWithholding,
            defaultWithholding,
            defaultZero
        )
    }

    private fun bindPayment(
        subtotal: String,
        iva: String,
        ivaWithholding: String,
        isrWithholding: String,
        total: String
    ) {
        viewMvc.bindPayment(subtotal, iva, ivaWithholding, isrWithholding, total)
    }

    private fun generateInvoicePayment(subtotal: Double): InvoicePayment {
        var isrWithholding = 0.0
        var ivaWithholding = 0.0

        if (receiverPersonType == Constants.MORAL_PERSON) {
            ivaWithholding = subtotal * Constants.IVA_WITHHOLDING
            isrWithholding = subtotal * Constants.ISR_WITHHOLDING
        }

        val iva = subtotal * Constants.IVA

        return InvoicePayment(
            subtotal = subtotal,
            iva = iva,
            ivaWithholding = ivaWithholding,
            isrWithholding = isrWithholding,
            total = subtotal + iva - ivaWithholding - isrWithholding
        )
    }

    private fun hasFormErrors(): String? {
        val defaultZero = getString(R.string.default_zero).toCurrency()

        return if (viewMvc.getSubtotal() == defaultZero ||
            viewMvc.getIva() == defaultZero ||
            viewMvc.getTotal() == defaultZero
        ) {
            getString(R.string.error_missing_calculation)
        } else if (hasChanges) {
            getString(R.string.error_missing_recalculation)
        } else {
            null
        }
    }

    private fun returnCompleteInvoice() {
        setDefaults()

        fragmentsEventBus.postEvent(
            InvoiceFormPaymentFragmentEvent(invoice!!)
        )
    }
}