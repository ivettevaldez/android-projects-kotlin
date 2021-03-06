package com.ivettevaldez.saturnus.screens.invoices.form.payment

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.FrameLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.screens.common.fields.ISimpleTextInputViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IInvoiceFormPaymentViewMvc : IObservableViewMvc<IInvoiceFormPaymentViewMvc.Listener> {

    interface Listener {

        fun onSubtotalChanged()
        fun onCalculateClicked(subtotal: String)
    }

    fun getSubtotal(): String
    fun getIva(): String
    fun getIvaWithholding(): String
    fun getIsrWithholding(): String
    fun getTotal(): String
    fun bindPayment(
        subtotal: String,
        iva: String,
        ivaWithholding: String,
        isrWithholding: String,
        total: String
    )
}

class InvoiceFormPaymentViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IInvoiceFormPaymentViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_form_payment
), IInvoiceFormPaymentViewMvc {

    private val buttonCalculate: Button = findViewById(R.id.button_primary)

    private val inputSubtotalContainer: FrameLayout =
        findViewById(R.id.invoice_form_payment_input_subtotal)
    private val inputSubtotal: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputSubtotalContainer)

    private val inputIvaContainer: FrameLayout =
        findViewById(R.id.invoice_form_payment_input_iva)
    private val inputIva: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputIvaContainer)

    private val inputIvaWithholdingContainer: FrameLayout =
        findViewById(R.id.invoice_form_payment_input_iva_withholding)
    private val inputIvaWithholding: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputIvaWithholdingContainer)

    private val inputIsrWithholdingContainer: FrameLayout =
        findViewById(R.id.invoice_form_payment_input_isr_withholding)
    private val inputIsrWithholding: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputIsrWithholdingContainer)

    private val inputTotalContainer: FrameLayout =
        findViewById(R.id.invoice_form_payment_input_total)
    private val inputTotal: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputTotalContainer)

    init {

        initViews()
        setListenerEvents()
    }

    override fun getSubtotal(): String = inputSubtotal.getText()

    override fun getIva(): String = inputIva.getText()

    override fun getIvaWithholding(): String = inputIvaWithholding.getText()

    override fun getIsrWithholding(): String = inputIsrWithholding.getText()

    override fun getTotal(): String = inputTotal.getText()

    override fun bindPayment(
        subtotal: String,
        iva: String,
        ivaWithholding: String,
        isrWithholding: String,
        total: String
    ) {
        setSubTotal(subtotal)
        setIva(iva)
        setIvaWithholding(ivaWithholding)
        setIsrWithholding(isrWithholding)
        setTotal(total)
    }

    private fun initViews() {
        // Subtotal field
        inputSubtotal.setHint(context.getString(R.string.invoices_subtotal))
        inputSubtotal.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER)
        inputSubtotal.setImeOptions(EditorInfo.IME_ACTION_DONE)
        inputSubtotalContainer.addView(inputSubtotal.getRootView())

        // IVA field
        inputIva.setHint(context.getString(R.string.invoices_iva))
        inputIva.disable()
        inputIvaContainer.addView(inputIva.getRootView())

        // IVA Withholding field
        inputIvaWithholding.setHint(context.getString(R.string.invoices_iva_withholding))
        inputIvaWithholding.disable()
        inputIvaWithholdingContainer.addView(inputIvaWithholding.getRootView())

        // ISR Withholding field
        inputIsrWithholding.setHint(context.getString(R.string.invoices_isr_withholding))
        inputIsrWithholding.disable()
        inputIsrWithholdingContainer.addView(inputIsrWithholding.getRootView())

        // Total field
        inputTotal.setHint(context.getString(R.string.invoices_total))
        inputTotal.disable()
        inputTotalContainer.addView(inputTotal.getRootView())

        // Button calculate
        buttonCalculate.setText(R.string.action_calculate)
    }

    private fun setListenerEvents() {
        inputSubtotal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputSubtotal.removeTextChangedListener(this)

                setSubTotal(value!!.toCurrency())

                for (listener in listeners) {
                    listener.onSubtotalChanged()
                }

                inputSubtotal.addTextChangedListener(this)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        buttonCalculate.setOnClickListener {
            for (listener in listeners) {
                listener.onCalculateClicked(getSubtotal())
            }
        }
    }

    private fun setSubTotal(value: String) {
        inputSubtotal.setText(value)
    }

    private fun setIva(value: String) {
        inputIva.setText(value)
    }

    private fun setIvaWithholding(value: String) {
        inputIvaWithholding.setText(value)
    }

    private fun setIsrWithholding(value: String) {
        inputIsrWithholding.setText(value)
    }

    private fun setTotal(value: String) {
        inputTotal.setText(value)
    }
}