package com.ivettevaldez.saturnus.screens.invoices.form.payment

import android.content.Context
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toDoubleValue
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.payment.GenerateInvoicePaymentUseCase
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsFragmentEvent

class InvoiceFormPaymentController(
    private val context: Context,
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
        this.subtotal = subtotal.toDoubleValue()
        val payment = generateInvoicePaymentUseCase.generatePayment(
            this.subtotal!!,
            receiverPersonType!!
        )

        invoice?.payment = payment

//        var ivaWithholding: String = ""
//        var isrWithholding: String = ""
//
//        if (receiverPersonType == Constants.PHYSICAL_PERSON) {
//            val defaultUnavailable = context.getString(R.string.default_unavailable)
//            ivaWithholding = defaultUnavailable
//            isrWithholding = defaultUnavailable
//        }
//
//        bindPayment(
//            payment.subtotal.toCurrency(),
//            payment.iva.toCurrency(),
//            ivaWithholding,
//            isrWithholding,
//            payment.total.toCurrency()
//        )

        hasChanges = false
    }

    override fun onFragmentEvent(event: Any) {
        if (event is InvoiceFormDetailsFragmentEvent) {
            hasChanges = true

            invoice = event.invoice
            receiverPersonType = invoice!!.receiver!!.personType
        }
    }
}