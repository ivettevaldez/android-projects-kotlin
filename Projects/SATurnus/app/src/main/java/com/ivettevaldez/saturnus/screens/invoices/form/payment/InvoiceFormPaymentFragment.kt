package com.ivettevaldez.saturnus.screens.invoices.form.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.helpers.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.common.helpers.CurrencyHelper.toDoubleValue
import com.ivettevaldez.saturnus.invoices.Invoice
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

    private lateinit var viewMvc: IInvoiceFormPaymentViewMvc

    private var invoice: Invoice? = null
    private var receiverPersonType: String? = null
    private var isSubtotalChanged = false

    companion object {

        @JvmStatic
        fun newInstance() = InvoiceFormPaymentFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormPaymentViewMvc(parent)
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
            invoice = event.invoice
            receiverPersonType = invoice?.receiver?.personType

            setDefaults()
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
        isSubtotalChanged = true
    }

    override fun onCalculateClicked(subtotal: String) {
        isSubtotalChanged = false

        val subtotalValue = subtotal.toDoubleValue()
        val invoicePayment = generateInvoicePayment(subtotalValue)

        viewMvc.setIva(invoicePayment.iva.toCurrency())
        viewMvc.setTotal(invoicePayment.total.toCurrency())

        if (receiverPersonType == Constants.MORAL_PERSON) {
            viewMvc.setIvaWithholding(invoicePayment.ivaWithholding.toCurrency())
            viewMvc.setIsrWithholding(invoicePayment.isrWithholding.toCurrency())
        }

        invoice?.payment = invoicePayment
    }

    private fun setDefaults() {
        val defaultZero = getString(R.string.default_zero).toCurrency()
        val defaultWithholding = if (receiverPersonType == Constants.PHYSICAL_PERSON) {
            getString(R.string.default_unavailable)
        } else {
            defaultZero
        }

        viewMvc.setSubTotal(defaultZero)
        viewMvc.setIva(defaultZero)
        viewMvc.setIvaWithholding(defaultWithholding)
        viewMvc.setIsrWithholding(defaultWithholding)
        viewMvc.setTotal(defaultZero)
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
            viewMvc.getTotal() == defaultZero ||
            isSubtotalChanged
        ) {
            getString(R.string.error_missing_calculation)
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