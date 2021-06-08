package com.ivettevaldez.saturnus.screens.invoices.form.details

/* ktlint-disable no-wildcard-imports */

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.fields.ISimpleTextInputViewMvc
import com.ivettevaldez.saturnus.screens.common.fields.ISpinnerInputViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.people.item.IPersonItemViewMvc

interface IInvoiceFormDetailsViewMvc : IObservableViewMvc<IInvoiceFormDetailsViewMvc.Listener> {

    interface Listener {

        fun onFieldChanged()
        fun onSelectReceiverClicked()
        fun onSelectIssuingDateClicked()
        fun onSelectCertificationDateClicked()
    }

    fun getFolio(): String
    fun getConcept(): String
    fun getDescription(): String
    fun getEffect(): String
    fun getStatus(): String
    fun getCancellationStatus(): String
    fun getIssuingDate(): String
    fun getCertificationDate(): String
    fun bindIssuingPerson(person: Person)
    fun bindReceiverPerson(person: Person)
    fun setIssuingDate(date: String)
    fun setCertificationDate(date: String)
}

class InvoiceFormDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IInvoiceFormDetailsViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_form_details
), IInvoiceFormDetailsViewMvc,
    ISpinnerInputViewMvc.ItemSelectedListener {

    private val personIssuing: FrameLayout = findViewById(R.id.invoice_form_details_issuing)
    private val personIssuingViewMvc: IPersonItemViewMvc =
        viewMvcFactory.newPersonItemViewMvc(personIssuing)

    private val personItemReceiver: FrameLayout = findViewById(R.id.invoice_form_details_receiver)
    private val personItemReceiverViewMvc: IPersonItemViewMvc = viewMvcFactory.newPersonItemViewMvc(
        personItemReceiver
    )

    private val inputFolioContainer: FrameLayout =
        findViewById(R.id.invoice_form_details_input_folio)
    private val inputFolio: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputFolioContainer)

    private val inputConceptContainer: FrameLayout =
        findViewById(R.id.invoice_form_details_input_concept)
    private val inputConcept: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputConceptContainer)

    private val inputDescriptionContainer: FrameLayout =
        findViewById(R.id.invoice_form_details_input_description)
    private val inputDescription: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputDescriptionContainer)

    private val inputEffectContainer: FrameLayout =
        findViewById(R.id.invoice_form_details_input_effect)
    private val inputEffect: ISpinnerInputViewMvc =
        viewMvcFactory.newSpinnerInputViewMvc(inputEffectContainer)

    private val inputStatusContainer: FrameLayout =
        findViewById(R.id.invoice_form_details_input_status)
    private val inputStatus: ISpinnerInputViewMvc =
        viewMvcFactory.newSpinnerInputViewMvc(inputStatusContainer)

    private val inputCancellationStatusContainer: FrameLayout =
        findViewById(R.id.invoice_form_details_input_cancellation)
    private val inputCancellationStatus: ISpinnerInputViewMvc =
        viewMvcFactory.newSpinnerInputViewMvc(inputCancellationStatusContainer)

    private val inputIssuingDateContainer: FrameLayout =
        findViewById(R.id.invoice_form_details_input_issuing_date)
    private val inputIssuingDate: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputIssuingDateContainer)

    private val inputCertificationDateContainer: FrameLayout =
        findViewById(R.id.invoice_form_details_input_certification_date)
    private val inputCertificationDate: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputCertificationDateContainer)

    init {

        initPersonItems()
        initFields()
        setListenerEvents()
    }

    override fun getFolio(): String = inputFolio.getText()

    override fun getConcept(): String = inputConcept.getText()

    override fun getDescription(): String = inputDescription.getText()

    override fun getEffect(): String = inputEffect.getText()

    override fun getStatus(): String = inputStatus.getText()

    override fun getCancellationStatus(): String = inputCancellationStatus.getText()

    override fun getIssuingDate(): String = inputIssuingDate.getText()

    override fun getCertificationDate(): String = inputCertificationDate.getText()

    override fun bindIssuingPerson(person: Person) {
        personIssuingViewMvc.bindPerson(person)
    }

    override fun bindReceiverPerson(person: Person) {
        personItemReceiverViewMvc.bindPerson(person)
    }

    override fun setIssuingDate(date: String) {
        inputIssuingDate.setText(date)
    }

    override fun setCertificationDate(date: String) {
        inputCertificationDate.setText(date)
    }

    override fun onItemSelected() {
        for (listener in listeners) {
            listener.onFieldChanged()
        }
    }

    private fun initPersonItems() {
        personIssuingViewMvc.seTitle(context.getString(R.string.invoices_issuing))
        personIssuingViewMvc.setBackgroundColor(R.color.color_primary)
        personIssuing.addView(personIssuingViewMvc.getRootView())

        personItemReceiverViewMvc.seTitle(context.getString(R.string.invoices_receiver))
        personItemReceiverViewMvc.setBackgroundColor(R.color.color_primary)
        personItemReceiverViewMvc.setEmpty()
        personItemReceiverViewMvc.enableActionAndListen(
            context.getString(R.string.action_change),
            object : IPersonItemViewMvc.ActionClickListener {
                override fun onActionClicked() {
                    for (listener in listeners) {
                        listener.onSelectReceiverClicked()
                    }
                }
            })
        personItemReceiver.addView(personItemReceiverViewMvc.getRootView())
    }

    private fun initFields() {
        // Folio field
        inputFolio.setHint(context.getString(R.string.invoices_folio))
        inputFolio.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
        inputFolio.setImeOptions(EditorInfo.IME_ACTION_DONE)
        inputFolioContainer.addView(inputFolio.getRootView())

        // Concept field
        inputConcept.setHint(context.getString(R.string.invoices_concept))
        inputConcept.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
        inputConcept.setImeOptions(EditorInfo.IME_ACTION_DONE)
        inputConceptContainer.addView(inputConcept.getRootView())

        // Description field
        inputDescription.setHint(context.getString(R.string.invoices_description))
        inputDescription.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
        inputDescription.setImeOptions(EditorInfo.IME_ACTION_DONE)
        inputDescriptionContainer.addView(inputDescription.getRootView())

        // Effect field
        inputEffect.setHint(context.getString(R.string.invoices_effect))
        inputEffect.bindValues(R.array.invoices_effects)
        inputEffectContainer.addView(inputEffect.getRootView())

        // Status field
        inputStatus.setHint(context.getString(R.string.invoices_status))
        inputStatus.bindValues(R.array.invoices_statuses)
        inputStatusContainer.addView(inputStatus.getRootView())

        // CancellationStatus field
        inputCancellationStatus.setHint(context.getString(R.string.invoices_cancellation_status))
        inputCancellationStatus.bindValues(R.array.invoices_cancellation_statuses)
        inputCancellationStatusContainer.addView(inputCancellationStatus.getRootView())

        // IssuingDate field
        inputIssuingDate.setHint(context.getString(R.string.invoices_issuing_date))
        inputIssuingDate.setDrawable(R.mipmap.ic_calendar_grey_24dp)
        inputIssuingDate.disable()
        inputIssuingDateContainer.addView(inputIssuingDate.getRootView())

        // CertificationDate field
        inputCertificationDate.setHint(context.getString(R.string.invoices_certification_date))
        inputCertificationDate.setDrawable(R.mipmap.ic_calendar_grey_24dp)
        inputCertificationDate.disable()
        inputCertificationDateContainer.addView(inputCertificationDate.getRootView())
    }

    private fun setListenerEvents() {
        inputIssuingDate.enableClickAndListen(object : ISimpleTextInputViewMvc.ClickListener {
            override fun onEditTextClicked() {
                for (listener in listeners) {
                    listener.onSelectIssuingDateClicked()
                }
            }
        })

        inputCertificationDate.enableClickAndListen(object : ISimpleTextInputViewMvc.ClickListener {
            override fun onEditTextClicked() {
                for (listener in listeners) {
                    listener.onSelectCertificationDateClicked()
                }
            }
        })

        val fieldChangedTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                for (listener in listeners) {
                    listener.onFieldChanged()
                }
            }
        }

        inputFolio.addTextChangedListener(fieldChangedTextWatcher)
        inputConcept.addTextChangedListener(fieldChangedTextWatcher)
        inputDescription.addTextChangedListener(fieldChangedTextWatcher)

        inputEffect.addItemSelectedListener(this)
        inputStatus.addItemSelectedListener(this)
        inputCancellationStatus.addItemSelectedListener(this)
    }
}