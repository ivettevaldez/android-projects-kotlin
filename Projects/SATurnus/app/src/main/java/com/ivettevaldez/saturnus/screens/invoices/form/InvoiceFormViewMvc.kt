package com.ivettevaldez.saturnus.screens.invoices.form

/* ktlint-disable no-wildcard-imports */

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.fields.ISimpleTextInputViewMvc
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.people.item.IPersonItemViewMvc

interface IInvoiceFormViewMvc : IObservableViewMvc<IInvoiceFormViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
        fun onSelectReceiverClicked()
        fun onSelectIssuingDateClicked()
        fun onSelectCertificationDateClicked()
        fun onSaveClicked()
    }

    fun getIssuingDate(): String?
    fun getCertificationDate(): String?
    fun bindIssuingPerson(person: Person)
    fun bindReceiverPerson(person: Person)
    fun setIssuingDate(date: String)
    fun setCertificationDate(date: String)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class InvoiceFormViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IInvoiceFormViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_form
), IInvoiceFormViewMvc {

    private val layoutProgress: FrameLayout = findViewById(R.id.invoice_form_progress)
    private val buttonSave: Button = findViewById(R.id.button_primary)

    private val toolbar: Toolbar = findViewById(R.id.invoice_form_toolbar)
    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    private val personIssuing: FrameLayout = findViewById(R.id.invoice_form_person_issuing)
    private val personIssuingViewMvc: IPersonItemViewMvc =
        viewMvcFactory.newPersonItemViewMvc(personIssuing)

    private val personItemReceiver: FrameLayout = findViewById(R.id.invoice_form_person_receiver)
    private val personItemReceiverViewMvc: IPersonItemViewMvc = viewMvcFactory.newPersonItemViewMvc(
        personItemReceiver
    )

    private val inputIssuingDateContainer: FrameLayout =
        findViewById(R.id.invoice_form_input_issuing_date)
    private val inputIssuingDate: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputIssuingDateContainer)

    private val inputCertificationDateContainer: FrameLayout =
        findViewById(R.id.invoice_form_input_certification_date)
    private val inputCertificationDate: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputCertificationDateContainer)

    init {

        initToolbar()
        initPersonItems()
        initFields()
        setListenerEvents()
    }

    override fun getIssuingDate(): String? {
        val issuingDate = inputIssuingDate.getText()
        return if (issuingDate.isNotEmpty()) {
            issuingDate
        } else {
            null
        }
    }

    override fun getCertificationDate(): String? {
        val certificationDate = inputCertificationDate.getText()
        return if (certificationDate.isNotEmpty()) {
            certificationDate
        } else {
            null
        }
    }

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

    override fun showProgressIndicator() {
        layoutProgress.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        layoutProgress.visibility = View.GONE
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(context.getString(R.string.invoices_new))

        toolbarViewMvc.enableNavigateUpAndListen(object : IToolbarViewMvc.NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        toolbar.addView(toolbarViewMvc.getRootView())
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
        // TODO:
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

        buttonSave.setOnClickListener {
            for (listener in listeners) {
                listener.onSaveClicked()
            }
        }
    }
}