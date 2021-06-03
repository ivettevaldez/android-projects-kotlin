package com.ivettevaldez.saturnus.screens.invoices.form.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.helpers.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.common.helpers.CurrencyHelper.toDoubleValue
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import javax.inject.Inject

class InvoiceFormPaymentFragment : BaseFragment(),
    Step,
    IInvoiceFormPaymentViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: IInvoiceFormPaymentViewMvc

    private var issuingRfc: String? = null

    companion object {

        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"

        @JvmStatic
        fun newInstance(issuingRfc: String) =
            InvoiceFormPaymentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ISSUING_RFC, issuingRfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        arguments?.let {
            issuingRfc = it.getString(ARG_ISSUING_RFC)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormPaymentViewMvc(parent)

        initFields()

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
    }

    override fun verifyStep(): VerificationError? {
        // TODO:
        return null
    }

    override fun onSelected() {
        // TODO:
    }

    override fun onError(error: VerificationError) {
        // TODO:
    }

    override fun onCalculateClicked(subtotal: String) {
        val subtotalValue = subtotal.toDoubleValue()

        val iva = subtotalValue * Constants.IVA
        viewMvc.setIva(iva.toCurrency())

        val isrWithholding = subtotalValue * Constants.ISR_WITHHOLDING
        viewMvc.setIsrWithholding(isrWithholding.toCurrency())

        val ivaWithholding = subtotalValue * Constants.IVA_WITHHOLDING
        viewMvc.setIvaWithholding(ivaWithholding.toCurrency())

        val total = subtotalValue + iva - ivaWithholding - isrWithholding
        viewMvc.setTotal(total.toCurrency())
    }

    private fun initFields() {
        viewMvc.setSubTotal("0".toCurrency())
        viewMvc.setIva("0".toCurrency())
        viewMvc.setIvaWithholding("0".toCurrency())
        viewMvc.setIsrWithholding("0".toCurrency())
        viewMvc.setTotal("0".toCurrency())
    }
}