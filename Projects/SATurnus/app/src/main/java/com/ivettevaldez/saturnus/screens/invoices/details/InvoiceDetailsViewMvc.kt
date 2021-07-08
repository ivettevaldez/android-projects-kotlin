package com.ivettevaldez.saturnus.screens.invoices.details

/* ktlint-disable no-wildcard-imports */

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.invoices.InvoicesHelper
import com.ivettevaldez.saturnus.screens.people.item.IPersonItemViewMvc
import java.util.*

interface IInvoiceDetailsViewMvc : IObservableViewMvc<IInvoiceDetailsViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
        fun onEditInvoiceClicked()
        fun onDeleteInvoiceClicked()
    }

    fun bindInvoice(invoice: Invoice)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class InvoiceDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory,
    private val invoicesHelper: InvoicesHelper
) : BaseObservableViewMvc<IInvoiceDetailsViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_invoice_details
), IInvoiceDetailsViewMvc {

    private val layoutProgress: FrameLayout = findViewById(R.id.invoice_details_progress)

    private val toolbarContainer: Toolbar = findViewById(R.id.invoice_details_toolbar)
    private val toolbar: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbarContainer)

    private val personIssuingContainer: FrameLayout = findViewById(R.id.invoice_details_issuing)
    private val personIssuing: IPersonItemViewMvc =
        viewMvcFactory.newPersonItemViewMvc(personIssuingContainer)

    private val personReceiverContainer: FrameLayout = findViewById(R.id.invoice_details_receiver)
    private val personReceiver: IPersonItemViewMvc = viewMvcFactory.newPersonItemViewMvc(
        personReceiverContainer
    )

    private val layoutSubtotal: LinearLayout = findViewById(R.id.invoice_details_subtotal)
    private val textSubtotalLabel: TextView = layoutSubtotal.findViewById(R.id.amount_text_label)
    private val textSubtotalValue: TextView = layoutSubtotal.findViewById(R.id.amount_text_value)

    private val layoutIva: LinearLayout = findViewById(R.id.invoice_details_iva)
    private val textIvaLabel: TextView = layoutIva.findViewById(R.id.amount_text_label)
    private val textIvaValue: TextView = layoutIva.findViewById(R.id.amount_text_value)

    private val layoutIvaWithholding: LinearLayout =
        findViewById(R.id.invoice_details_iva_withholding)
    private val textIvaWithholdingLabel: TextView =
        layoutIvaWithholding.findViewById(R.id.amount_text_label)
    private val textIvaWithholdingValue: TextView =
        layoutIvaWithholding.findViewById(R.id.amount_text_value)

    private val layoutIsrWithholding: LinearLayout =
        findViewById(R.id.invoice_details_isr_withholding)
    private val textIsrWithholdingLabel: TextView =
        layoutIsrWithholding.findViewById(R.id.amount_text_label)
    private val textIsrWithholdingValue: TextView =
        layoutIsrWithholding.findViewById(R.id.amount_text_value)

    private val layoutTotal: LinearLayout = findViewById(R.id.invoice_details_total)
    private val textTotalLabel: TextView = layoutTotal.findViewById(R.id.amount_text_label)
    private val textTotalValue: TextView = layoutTotal.findViewById(R.id.amount_text_value)

    private val textStatus: TextView = findViewById(R.id.invoice_status_text_status)
    private val textFolio: TextView = findViewById(R.id.invoice_status_text_folio)
    private val textConcept: TextView = findViewById(R.id.invoice_details_text_concept)
    private val textDescription: TextView = findViewById(R.id.invoice_details_text_description)
    private val textIssuingDate: TextView = findViewById(R.id.invoice_details_text_issuing_date)
    private val textCertificationDate: TextView =
        findViewById(R.id.invoice_details_text_certification_date)

    private lateinit var invoice: Invoice

    init {

        initToolbar()
        initPersonItems()
        initAmounts()
    }

    override fun bindInvoice(invoice: Invoice) {
        this.invoice = invoice

        bindPeople()
        bindStatus()
        bindGeneralData()
        bindDates()
        bindAmounts()
    }

    override fun showProgressIndicator() {
        layoutProgress.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        layoutProgress.visibility = View.GONE
    }

    private fun initToolbar() {
        toolbar.setTitle(context.getString(R.string.invoices_detail))

        toolbar.enableNavigateUpAndListen(object : IToolbarViewMvc.NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        toolbar.enableEditAndListen(object : IToolbarViewMvc.EditClickListener {
            override fun onEditClicked() {
                for (listener in listeners) {
                    listener.onEditInvoiceClicked()
                }
            }
        })

        toolbar.enableDeleteAndListen(object : IToolbarViewMvc.DeleteClickListener {
            override fun onDeleteClicked() {
                for (listener in listeners) {
                    listener.onDeleteInvoiceClicked()
                }
            }
        })

        toolbarContainer.addView(toolbar.getRootView())
    }

    private fun initPersonItems() {
        personIssuing.seTitle(context.getString(R.string.invoices_issuing))
        personIssuing.setBackgroundColor(R.color.color_primary)
        personIssuingContainer.addView(personIssuing.getRootView())

        personReceiver.seTitle(context.getString(R.string.invoices_receiver))
        personReceiver.setBackgroundColor(R.color.color_primary)
        personReceiverContainer.addView(personReceiver.getRootView())
    }

    private fun initAmounts() {
        textSubtotalLabel.text = context.getString(R.string.invoices_subtotal)

        textIvaLabel.text = context.getString(R.string.invoices_iva)
        textIvaValue.setTextColor(ContextCompat.getColor(context, R.color.puerto_rico))

        textIvaWithholdingLabel.text = context.getString(R.string.invoices_iva_withholding)
        textIvaWithholdingValue.setTextColor(ContextCompat.getColor(context, R.color.bittersweet))

        textIsrWithholdingLabel.text = context.getString(R.string.invoices_isr_withholding)
        textIsrWithholdingValue.setTextColor(ContextCompat.getColor(context, R.color.bittersweet))

        textTotalLabel.text = context.getString(R.string.invoices_total)
        textTotalLabel.typeface = Typeface.DEFAULT_BOLD
        textTotalValue.typeface = Typeface.DEFAULT_BOLD
    }

    private fun bindAmounts() {
        textSubtotalValue.text = invoice.payment!!.subtotal.toCurrency()

        textIvaValue.text = String.format(
            Locale.getDefault(),
            context.getString(
                R.string.default_plus_template,
                invoice.payment!!.iva.toCurrency()
            )
        )

        textIvaWithholdingValue.text = String.format(
            Locale.getDefault(),
            context.getString(
                R.string.default_minus_template,
                invoice.payment!!.ivaWithholding.toCurrency()
            )
        )

        textIsrWithholdingValue.text = String.format(
            Locale.getDefault(),
            context.getString(
                R.string.default_minus_template,
                invoice.payment!!.isrWithholding.toCurrency()
            )
        )

        textTotalValue.text = invoice.payment!!.total.toCurrency()
    }

    private fun bindPeople() {
        personIssuing.bindPerson(invoice.issuing!!)
        personReceiver.bindPerson(invoice.receiver!!)
    }

    private fun bindStatus() {
        textStatus.text = invoicesHelper.getFormattedStatus(invoice.status)
        textStatus.setBackgroundResource(invoicesHelper.getStatusBackground(invoice.status))
    }

    private fun bindGeneralData() {
        textFolio.text = invoicesHelper.getFormattedFolio(invoice.folio)

        textConcept.text = invoicesHelper.getFormattedConcept(
            invoice.concept,
            invoice.payment!!.total.toCurrency(),
            invoice.effect
        )

        textDescription.text = invoice.description
    }

    private fun bindDates() {
        textIssuingDate.text = invoicesHelper.getIssuingDate(invoice.issuedAt!!)

        if (invoice.certificatedAt != null) {
            textCertificationDate.text = invoicesHelper.getCertificationDate(
                invoice.certificatedAt!!
            )
        }
    }
}