package com.ivettevaldez.saturnus.screens.invoices.form.payment

import android.content.Context
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toDoubleValue
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.payment.InvoicePayment
import com.ivettevaldez.saturnus.invoices.payment.calculator.InvoicePaymentCalculator
import com.ivettevaldez.saturnus.invoices.payment.calculator.InvoicePaymentCalculatorFactory
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsFragmentEvent
import com.stepstone.stepper.VerificationError

class InvoiceFormPaymentController(
    private val context: Context,
    private val dialogsManager: DialogsManager,
    private val fragmentsEventBus: FragmentsEventBus,
    private val invoicePaymentCalculatorFactory: InvoicePaymentCalculatorFactory,
    private val invoiceDao: InvoiceDao
) : IInvoiceFormPaymentViewMvc.Listener,
    FragmentsEventBus.Listener {

    private lateinit var viewMvc: IInvoiceFormPaymentViewMvc

    var invoice: Invoice? = null
    var invoicePayment: InvoicePayment? = null
    var receiverPersonType: String? = null
    var invoicePaymentCalculator: InvoicePaymentCalculator? = null

    var newInvoice: Boolean = false
    var editingInvoice: Boolean = false
    var hasChanges: Boolean = false

    fun bindView(viewMvc: IInvoiceFormPaymentViewMvc) {
        this.viewMvc = viewMvc
    }

    fun initVariables(folio: String?) {
        if (folio == null) {
            newInvoice = true
        } else {
            editingInvoice = true
        }

        if (editingInvoice) {
            findInvoiceWithPayment(folio!!)
        }
    }

    private fun findInvoiceWithPayment(folio: String) {
        invoice = invoiceDao.findByFolio(folio)

        if (invoice != null) {
            invoicePayment = invoice!!.payment
        }
    }

    private fun bindPayment() {
        val ivaWithholding: String
        val isrWithholding: String

        if (receiverPersonType == Constants.PHYSICAL_PERSON) {
            val defaultUnavailable = context.getString(R.string.default_unavailable)
            ivaWithholding = defaultUnavailable
            isrWithholding = defaultUnavailable
        } else {
            ivaWithholding = invoicePayment!!.ivaWithholding.toCurrency()
            isrWithholding = invoicePayment!!.isrWithholding.toCurrency()
        }

        viewMvc.bindPayment(
            invoicePayment!!.subtotal.toCurrency(),
            invoicePayment!!.iva.toCurrency(),
            ivaWithholding,
            isrWithholding,
            invoicePayment!!.total.toCurrency()
        )
    }

    fun onStart() {
        viewMvc.registerListener(this)
        fragmentsEventBus.registerListener(this)

        if (editingInvoice) {
            bindPayment()
        }
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
        fragmentsEventBus.unregisterListener(this)
    }

    override fun onSubtotalChanged() {
        hasChanges = true
    }

    override fun onCalculateClicked(subtotal: String) {
        val defaultZero = context.getString(R.string.default_zero).toCurrency()

        if (subtotal.isBlank() || subtotal == defaultZero) {
            dialogsManager.showMissingSubtotalError(null)
        } else {
            val subtotalDouble = subtotal.toDoubleValue()
            invoicePayment = invoicePaymentCalculator!!.calculatePayment(subtotalDouble)

            bindPayment()

            hasChanges = false
        }
    }

    override fun onFragmentEvent(event: Any) {
        if (event is InvoiceFormDetailsFragmentEvent) {
            invoice = event.invoice
            receiverPersonType = invoice!!.receiver!!.personType
            invoicePaymentCalculator =
                invoicePaymentCalculatorFactory.newInvoicePaymentCalculator(receiverPersonType!!)

            if (editingInvoice) {
                onCalculateClicked(viewMvc.getSubtotal())
            }
        }
    }

    fun verifyStep(): VerificationError? {
        val errorMessage: String? = hasFormErrors()
        return if (errorMessage != null) {
            VerificationError(errorMessage)
        } else {
            postCompleteInvoice()
            null
        }
    }

    fun onError(error: VerificationError) {
        dialogsManager.showGenericSavingError(null, error.errorMessage)
    }

    private fun hasFormErrors(): String? {
        val defaultZero = context.getString(R.string.default_zero).toCurrency()

        return if (viewMvc.getSubtotal() == defaultZero ||
            viewMvc.getIva() == defaultZero ||
            viewMvc.getTotal() == defaultZero
        ) {
            context.getString(R.string.error_missing_calculation)
        } else if (hasChanges) {
            context.getString(R.string.error_missing_recalculation)
        } else {
            null
        }
    }

    private fun setDefaults() {
        val defaultZero: String = context.getString(R.string.default_zero).toCurrency()
        val ivaWithholding: String
        val isrWithholding: String

        if (receiverPersonType == Constants.PHYSICAL_PERSON) {
            val defaultUnavailable = context.getString(R.string.default_unavailable)
            ivaWithholding = defaultUnavailable
            isrWithholding = defaultUnavailable
        } else {
            ivaWithholding = defaultZero
            isrWithholding = defaultZero
        }

        viewMvc.bindPayment(
            defaultZero,
            defaultZero,
            ivaWithholding,
            isrWithholding,
            defaultZero
        )
    }

    private fun postCompleteInvoice() {
        setDefaults()

        invoice!!.payment = invoicePayment

        fragmentsEventBus.postEvent(
            InvoiceFormPaymentFragmentEvent(invoice!!)
        )
    }
}