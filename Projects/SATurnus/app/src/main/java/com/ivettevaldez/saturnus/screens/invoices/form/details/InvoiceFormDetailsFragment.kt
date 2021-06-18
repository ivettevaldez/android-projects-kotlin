package com.ivettevaldez.saturnus.screens.invoices.form.details

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.helpers.DatesHelper.calendar
import com.ivettevaldez.saturnus.common.helpers.DatesHelper.friendlyDate
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dates.DatePickerManager
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.IPersonSelectorBottomSheetViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormChangeFragmentEvent
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import javax.inject.Inject

class InvoiceFormDetailsFragment : BaseFragment(),
    Step,
    IInvoiceFormDetailsViewMvc.Listener,
    IPersonSelectorBottomSheetViewMvc.Listener,
    DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var fragmentsEventBus: FragmentsEventBus

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var datePickerManager: DatePickerManager

    @Inject
    lateinit var personDao: PersonDao

    @Inject
    lateinit var invoiceDao: InvoiceDao

    private lateinit var viewMvc: IInvoiceFormDetailsViewMvc

    private var folio: String? = null
    private var issuingRfc: String? = null
    private var receiverRfc: String? = null
    private var invoice: Invoice? = null
    private var hasNotifiedChanges: Boolean = false

    companion object {

        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"
        private const val ARG_FOLIO = "ARG_FOLIO"

        @JvmStatic
        fun newInstance(folio: String?, issuingRfc: String?) =
            InvoiceFormDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FOLIO, folio)
                    putString(ARG_ISSUING_RFC, issuingRfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            folio = it.getString(ARG_FOLIO)
            issuingRfc = it.getString(ARG_ISSUING_RFC)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormDetailsViewMvc(parent)

        if (folio != null) {
            invoice = invoiceDao.findByFolio(folio!!)

            if (invoice != null) {
                issuingRfc = invoice?.issuing?.rfc
                receiverRfc = invoice?.receiver?.rfc

                bindInvoice(invoice!!)
            }
        }

        bindIssuingPerson()

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
        val error = hasFormErrors()
        return if (error != null) {
            return VerificationError(error)
        } else {
            generateInvoice()
            null
        }
    }

    override fun onSelected() {
        // Nothing to do here.
    }

    override fun onError(error: VerificationError) {
        dialogsManager.showGenericSavingError(null, error.errorMessage)
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = friendlyDate(year, monthOfYear, dayOfMonth)

        if (view?.tag == DatePickerManager.TAG_ISSUING_DATE) {
            viewMvc.setIssuingDate(date)
        } else if (view?.tag == DatePickerManager.TAG_CERTIFICATION_DATE) {
            viewMvc.setCertificationDate(date)
        }

        notifyFormChanges()
    }

    override fun onFieldChanged() {
        notifyFormChanges()
    }

    override fun onSelectReceiverClicked() {
        dialogsManager.showPersonSelectorDialog(null, this)
    }

    override fun onSelectIssuingDateClicked() {
        val currentDate = viewMvc.getIssuingDate().calendar()
        datePickerManager.showDatePicker(
            currentDate,
            this,
            DatePickerManager.TAG_ISSUING_DATE
        )
    }

    override fun onSelectCertificationDateClicked() {
        val currentDate = viewMvc.getCertificationDate().calendar()
        datePickerManager.showDatePicker(
            currentDate,
            this,
            DatePickerManager.TAG_CERTIFICATION_DATE
        )
    }

    override fun onPersonSelected(rfc: String) {
        receiverRfc = rfc
        bindReceiverPerson()
        notifyFormChanges()
    }

    private fun getPerson(rfc: String): Person? = personDao.findByRfc(rfc)

    private fun bindInvoice(invoice: Invoice) {
        viewMvc.bindInvoice(invoice)

        viewMvc.setIssuingDate(invoice.issuedAt!!.friendlyDate())

        if (invoice.certificatedAt != null) {
            viewMvc.setIssuingDate(invoice.certificatedAt!!.friendlyDate())
        }
    }

    private fun bindIssuingPerson() {
        val person = getPerson(issuingRfc!!)
        if (person != null) {
            viewMvc.bindIssuingPerson(person)
        } else {
            throw RuntimeException("@@@@@ Person with RFC $issuingRfc cannot be found")
        }
    }

    private fun bindReceiverPerson() {
        val person = getPerson(receiverRfc!!)
        if (person != null) {
            viewMvc.bindReceiverPerson(person)
        } else {
            throw RuntimeException("@@@@@ Person with RFC $receiverRfc cannot be found")
        }
    }

    private fun hasFormErrors(): String? = if (receiverRfc == null) {
        getString(R.string.error_missing_receiver)
    } else if (viewMvc.getFolio().isBlank() ||
        viewMvc.getConcept().isBlank() ||
        viewMvc.getDescription().isBlank() ||
        viewMvc.getEffect().isBlank() ||
        viewMvc.getStatus().isBlank() ||
        viewMvc.getCancellationStatus().isBlank() ||
        viewMvc.getIssuingDate().isBlank()
    ) {
        getString(R.string.error_missing_fields)
    } else if (folioExists(viewMvc.getFolio())) {
        getString(R.string.error_folio_must_be_unique)
    } else {
        null
    }

    private fun folioExists(folio: String): Boolean {
        val issuingPerson = personDao.findByRfc(issuingRfc!!)

        return if (issuingPerson != null) {
            val folioExists: Boolean = issuingPerson.invoices.any {
                it.folio == folio
            }
            folioExists
        } else {
            false
        }
    }

    private fun generateInvoice() {
        val invoice = Invoice(
            issuing = getPerson(issuingRfc!!),
            receiver = getPerson(receiverRfc!!),
            folio = viewMvc.getFolio(),
            concept = viewMvc.getConcept(),
            description = viewMvc.getDescription(),
            effect = viewMvc.getEffect(),
            status = viewMvc.getStatus(),
            cancellationStatus = viewMvc.getCancellationStatus(),
            issuedAt = viewMvc.getIssuingDate().calendar().time,
            certificatedAt = viewMvc.getCertificationDate().calendar().time
        )

        // Pass the invoice draft to InvoiceFormPaymentFragment to continue working on it.
        fragmentsEventBus.postEvent(
            InvoiceFormDetailsFragmentEvent(invoice)
        )
    }

    private fun notifyFormChanges() {
        if (!hasNotifiedChanges) {
            fragmentsEventBus.postEvent(
                InvoiceFormChangeFragmentEvent()
            )

            hasNotifiedChanges = true
        }
    }
}