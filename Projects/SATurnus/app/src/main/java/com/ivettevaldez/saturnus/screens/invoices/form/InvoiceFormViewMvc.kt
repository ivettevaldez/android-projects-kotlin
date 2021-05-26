package com.ivettevaldez.saturnus.screens.invoices.form

/* ktlint-disable no-wildcard-imports */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.legacy.widget.Space
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.people.Person
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

    fun bindIssuingPerson(person: Person)
    fun bindReceiverPerson(person: Person)
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
    private val buttonSave: Button = findViewById(R.id.invoice_form_button_save)

    private val layoutIssuing: LinearLayout = findViewById(R.id.invoice_form_issuing)
    private val textIssuingTitle: TextView =
        layoutIssuing.findViewById(R.id.item_invoice_person_text_title)
    private val textIssuingName: TextView =
        layoutIssuing.findViewById(R.id.item_invoice_person_text_name)
    private val textIssuingRfc: TextView =
        layoutIssuing.findViewById(R.id.item_invoice_person_text_rfc)
    private val textIssuingAction: TextView =
        layoutIssuing.findViewById(R.id.item_invoice_person_text_action)
    private val imageIssuingPersonType: ImageView =
        layoutIssuing.findViewById(R.id.item_invoice_person_image_person_type)

    private val layoutReceiver: LinearLayout = findViewById(R.id.invoice_form_receiver)
    private val layoutReceiverDetails: LinearLayout =
        layoutReceiver.findViewById(R.id.item_invoice_person_layout_details)
    private val textReceiverTitle: TextView =
        layoutReceiver.findViewById(R.id.item_invoice_person_text_title)
    private val textReceiverName: TextView =
        layoutReceiver.findViewById(R.id.item_invoice_person_text_name)
    private val textReceiverRfc: TextView =
        layoutReceiver.findViewById(R.id.item_invoice_person_text_rfc)
    private val textReceiverAction: TextView =
        layoutReceiver.findViewById(R.id.item_invoice_person_text_action)
    private val imageReceiverPersonType: ImageView =
        layoutReceiver.findViewById(R.id.item_invoice_person_image_person_type)
    private val spaceReceiver: Space =
        layoutReceiver.findViewById(R.id.item_invoice_person_space)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    init {

        initToolbar()
        initFields()
        setListenerEvents()
    }

    override fun bindIssuingPerson(person: Person) {
        textIssuingName.text = person.name
        textIssuingRfc.text = String.format(
            "%s: %s",
            context.getString(R.string.people_rfc),
            person.rfc
        )
        imageIssuingPersonType.setImageResource(getPersonTypeIcon(person.personType))
    }

    override fun bindReceiverPerson(person: Person) {
        layoutReceiverDetails.visibility = View.VISIBLE
        spaceReceiver.visibility = View.VISIBLE
        textReceiverAction.text = context.getString(R.string.action_change)

        textReceiverName.text = person.name
        textReceiverRfc.text = String.format(
            "%s: %s",
            context.getString(R.string.people_rfc),
            person.rfc
        )
        imageReceiverPersonType.setImageResource(getPersonTypeIcon(person.personType))
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
        textIssuingTitle.text = context.getString(R.string.invoices_issuing)
        textReceiverTitle.text = context.getString(R.string.invoices_receiver)

        layoutReceiverDetails.visibility = View.GONE
        spaceReceiver.visibility = View.GONE
        textIssuingAction.visibility = View.GONE
        textReceiverAction.text = context.getString(R.string.action_select_person)

        imageReceiverPersonType.setImageResource(getPersonTypeIcon(""))
    }

    private fun setListenerEvents() {
        buttonSave.setOnClickListener {
            for (listener in listeners) {
                listener.onSaveClicked()
            }
        }

        textReceiverAction.setOnClickListener {
            for (listener in listeners) {
                listener.onSelectReceiverClicked()
            }
        }
    }

    private fun getPersonTypeIcon(personType: String): Int {
        return if (personType == Constants.PHYSICAL_PERSON) {
            R.mipmap.ic_person_grey_36dp
        } else if (personType == Constants.MORAL_PERSON) {
            R.mipmap.ic_people_grey_36dp
        } else {
            R.mipmap.ic_person_plus_blue_36dp
        }
    }
}