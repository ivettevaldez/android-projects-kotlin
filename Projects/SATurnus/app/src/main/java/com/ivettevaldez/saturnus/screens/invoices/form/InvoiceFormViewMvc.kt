package com.ivettevaldez.saturnus.screens.invoices.form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IInvoiceFormViewMvc : IObservableViewMvc<IInvoiceFormViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
        fun onSelectReceiverClicked()
        fun onSaveClicked()
    }

    fun bindIssuingPerson(name: String)
    fun bindReceiverPerson(name: String)
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

    private val toolbar: Toolbar = findViewById(R.id.invoice_form_toolbar)
    private val layoutProgress: FrameLayout = findViewById(R.id.invoice_form_progress)
    private val inputIssuing: TextInputLayout = findViewById(R.id.invoice_form_input_issuing)
    private val inputReceiver: TextInputLayout = findViewById(R.id.invoice_form_input_receiver)
    private val editIssuing: TextInputEditText =
        inputIssuing.findViewById(R.id.text_input_edit_text_simple)
    private val editReceiver: TextInputEditText =
        inputReceiver.findViewById(R.id.text_input_edit_text_simple)
    private val buttonSave: Button = findViewById(R.id.invoice_form_button_save)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    init {

        initToolbar()
        initFields()
        setListenerEvents()
    }

    override fun bindIssuingPerson(name: String) {
        editIssuing.setText(name)
    }

    override fun bindReceiverPerson(name: String) {
        editReceiver.setText(name)
    }

    override fun showProgressIndicator() {
        layoutProgress.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        layoutProgress.visibility = View.GONE
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(
            context.getString(R.string.invoices_new)
        )

        toolbarViewMvc.enableNavigateUpAndListen(object : IToolbarViewMvc.NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        toolbar.addView(toolbarViewMvc.getRootView())
    }

    private fun initFields() {
        // Hints
        inputIssuing.hint = context.getString(R.string.people_client_type_issuing)
        inputReceiver.hint = context.getString(R.string.people_client_type_receiver)

        // Other
        editIssuing.isEnabled = false
        editReceiver.isEnabled = false
    }

    private fun setListenerEvents() {
        buttonSave.setOnClickListener {
            for (listener in listeners) {
                listener.onSaveClicked()
            }
        }

        inputReceiver.setOnClickListener {
            for (listener in listeners) {
                listener.onSelectReceiverClicked()
            }
        }
    }

    private fun cleanFields() {
        editIssuing.setText("")
        editReceiver.setText("")
    }
}