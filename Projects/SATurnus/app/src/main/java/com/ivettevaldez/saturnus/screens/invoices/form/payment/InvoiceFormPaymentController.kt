package com.ivettevaldez.saturnus.screens.invoices.form.payment

import android.content.Context
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toDoubleValue
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.payment.GenerateInvoicePaymentUseCase
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsFragmentEvent
import com.stepstone.stepper.VerificationError

class InvoiceFormPaymentController(
    private val context: Context,
    private val dialogsManager: DialogsManager,
    private val generateInvoicePaymentUseCase: GenerateInvoicePaymentUseCase,
    private val fragmentsEventBus: FragmentsEventBus,
    private val invoiceDao: InvoiceDao
) : IInvoiceFormPaymentViewMvc.Listener,
    FragmentsEventBus.Listener {

    private lateinit var viewMvc: IInvoiceFormPaymentViewMvc

    var invoice: Invoice? = null
    var subtotal: Double? = null
    var receiverPersonType: String? = null

    var newInvoice: Boolean = false
    var editingInvoice: Boolean = false
    var hasChanges: Boolean = false

    fun bindView(viewMvc: IInvoiceFormPaymentViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindArguments(folio: String?) {
        if (folio == null) {
            newInvoice = true
        } else {
            editingInvoice = true
        }

        if (editingInvoice) {
            findInvoice(folio!!)
        }
    }

    fun findInvoice(folio: String) {
        invoice = invoiceDao.findByFolio(folio)

        if (invoice != null) {
            subtotal = invoice!!.payment!!.subtotal
        }
    }

    fun bindData() {
        if (editingInvoice) {
            bindPayment(
                invoice!!.payment!!.subtotal.toCurrency(),
                invoice!!.payment!!.iva.toCurrency(),
                invoice!!.payment!!.ivaWithholding.toCurrency(),
                invoice!!.payment!!.isrWithholding.toCurrency(),
                invoice!!.payment!!.total.toCurrency()
            )
        }
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

    fun onStart() {
        viewMvc.registerListener(this)
        fragmentsEventBus.registerListener(this)
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
        fragmentsEventBus.unregisterListener(this)
    }

    override fun onSubtotalChanged() {
        hasChanges = true
    }

    override fun onCalculateClicked(subtotal: String) {
        if (subtotal.isBlank()) {
            showSavingErrorDialog(context.getString(R.string.error_missing_subtotal))
        } else {
            this.subtotal = subtotal.toDoubleValue()
            val payment = generateInvoicePaymentUseCase.generatePayment(
                this.subtotal!!,
                receiverPersonType!!
            )

            invoice?.payment = payment

            val ivaWithholding: String
            val isrWithholding: String

            when (receiverPersonType) {
                Constants.PHYSICAL_PERSON -> {
                    val defaultUnavailable = context.getString(R.string.default_unavailable)
                    ivaWithholding = defaultUnavailable
                    isrWithholding = defaultUnavailable
                }
                Constants.MORAL_PERSON -> {
                    ivaWithholding = payment.ivaWithholding.toCurrency()
                    isrWithholding = payment.isrWithholding.toCurrency()
                }
                else -> {
                    throw RuntimeException("Unsupported receiver type: $receiverPersonType")
                }
            }

            bindPayment(
                payment.subtotal.toCurrency(),
                payment.iva.toCurrency(),
                ivaWithholding,
                isrWithholding,
                payment.total.toCurrency()
            )

            hasChanges = false
        }
    }

    override fun onFragmentEvent(event: Any) {
        if (event is InvoiceFormDetailsFragmentEvent) {
            hasChanges = true

            invoice = event.invoice
            receiverPersonType = invoice!!.receiver!!.personType
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
        showSavingErrorDialog(error.errorMessage)
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

    private fun showSavingErrorDialog(error: String) {
        dialogsManager.showGenericSavingError(null, error)
    }

    private fun setDefaults() {
        val defaultZero: String = context.getString(R.string.default_zero).toCurrency()
        val withholding: String = if (receiverPersonType == Constants.PHYSICAL_PERSON) {
            context.getString(R.string.default_unavailable)
        } else {
            defaultZero
        }

        bindPayment(
            subtotal = defaultZero,
            iva = defaultZero,
            ivaWithholding = withholding,
            isrWithholding = withholding,
            total = defaultZero
        )
    }

    private fun postCompleteInvoice() {
        setDefaults()

        fragmentsEventBus.postEvent(
            InvoiceFormPaymentFragmentEvent(invoice!!)
        )
    }
}