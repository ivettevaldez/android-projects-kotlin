package com.ivettevaldez.saturnus.screens.invoices.form.details

import android.content.Context
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.datetime.DatesHelper.calendar
import com.ivettevaldez.saturnus.common.datetime.DatesHelper.friendlyDate
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.datepicker.DatePickerManager
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.IPersonSelectorBottomSheetViewMvc
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormChangeFragmentEvent
import com.stepstone.stepper.VerificationError
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

class InvoiceFormDetailsController(
    private val context: Context,
    private val fragmentsEventBus: FragmentsEventBus,
    private val dialogsManager: DialogsManager,
    private val datePickerManager: DatePickerManager,
    private val personDao: PersonDao,
    private val invoiceDao: InvoiceDao
) : IInvoiceFormDetailsViewMvc.Listener,
    IPersonSelectorBottomSheetViewMvc.Listener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewMvc: IInvoiceFormDetailsViewMvc

    var issuingRfc: String? = null
    var receiverRfc: String? = null
    var folio: String? = null
    var invoice: Invoice? = null

    var newInvoice: Boolean = false
    var editingInvoice: Boolean = false
    var hasNotifiedChanges: Boolean = false
    var selectedDatePicker: String? = null

    fun bindView(viewMvc: IInvoiceFormDetailsViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindArguments(issuingRfc: String?, folio: String?) {
        this.issuingRfc = issuingRfc
        this.folio = folio

        if (folio == null) {
            newInvoice = true
        } else {
            editingInvoice = true
        }

        if (editingInvoice) {
            findInvoiceAndRfcs()
        }
    }

    fun findInvoiceAndRfcs() {
        invoice = invoiceDao.findByFolio(folio!!)
        issuingRfc = invoice?.issuing?.rfc
        receiverRfc = invoice?.receiver?.rfc
    }

    fun bindData() {
        bindIssuingPerson()

        if (editingInvoice) {
            bindInvoice()
        }
    }

    fun onStart() {
        viewMvc.registerListener(this)
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    fun verifyStep(): VerificationError? {
        val errorMessage = hasFormErrors()
        return if (errorMessage != null) {
            VerificationError(errorMessage)
        } else {
            generateInvoice()
            null
        }
    }

    fun onError(error: VerificationError) {
        dialogsManager.showGenericSavingError(null, error.errorMessage)
    }

    override fun onFieldChanged() {
        notifyFormChanges()
    }

    override fun onSelectReceiverClicked() {
        dialogsManager.showPersonSelectorDialog(null, this)
    }

    override fun onPersonSelected(rfc: String) {
        receiverRfc = rfc
        bindReceiverPerson()
        notifyFormChanges()
    }

    override fun onSelectIssuingDateClicked() {
        selectedDatePicker = DatePickerManager.TAG_ISSUING_DATE
        val currentDate = viewMvc.getIssuingDate().calendar()

        datePickerManager.showDatePicker(
            currentDate,
            this,
            selectedDatePicker
        )
    }

    override fun onSelectCertificationDateClicked() {
        selectedDatePicker = DatePickerManager.TAG_CERTIFICATION_DATE
        val currentDate = viewMvc.getCertificationDate().calendar()

        datePickerManager.showDatePicker(
            currentDate,
            this,
            selectedDatePicker
        )
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = friendlyDate(year, monthOfYear, dayOfMonth)

        when (selectedDatePicker) {
            DatePickerManager.TAG_ISSUING_DATE -> viewMvc.setIssuingDate(date)
            DatePickerManager.TAG_CERTIFICATION_DATE -> viewMvc.setCertificationDate(date)
            else -> throw RuntimeException("Unsupported tag: $selectedDatePicker")
        }

        notifyFormChanges()
    }

    private fun getPerson(rfc: String): Person? = personDao.findByRfc(rfc)

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

    private fun bindInvoice() {
        viewMvc.bindInvoice(invoice!!)

        viewMvc.setIssuingDate(invoice!!.issuedAt!!.friendlyDate())

        if (invoice!!.certificatedAt != null) {
            viewMvc.setCertificationDate(invoice!!.certificatedAt!!.friendlyDate())
        }
    }

    private fun notifyFormChanges() {
        if (!hasNotifiedChanges) {
            fragmentsEventBus.postEvent(
                InvoiceFormChangeFragmentEvent()
            )

            hasNotifiedChanges = true
        }
    }

    private fun hasFormErrors(): String? = if (receiverRfc == null) {
        context.getString(R.string.error_missing_receiver)
    } else if (viewMvc.getFolio().isBlank() ||
        viewMvc.getConcept().isBlank() ||
        viewMvc.getDescription().isBlank() ||
        viewMvc.getEffect().isBlank() ||
        viewMvc.getStatus().isBlank() ||
        viewMvc.getCancellationStatus().isBlank() ||
        viewMvc.getIssuingDate().isBlank()
    ) {
        context.getString(R.string.error_missing_fields)
    } else if (personDao.invoiceFolioExists(issuingRfc!!, viewMvc.getFolio())) {
        context.getString(R.string.error_folio_must_be_unique)
    } else {
        null
    }

    fun generateInvoice() {
        val invoice = Invoice(
            issuing = getPerson(issuingRfc!!),
            receiver = getPerson(receiverRfc!!),
            folio = viewMvc.getFolio(),
            concept = viewMvc.getConcept(),
            description = viewMvc.getDescription(),
            effect = viewMvc.getEffect(),
            status = viewMvc.getStatus(),
            cancellationStatus = viewMvc.getCancellationStatus(),
            issuedAt = viewMvc.getIssuingDate().calendar().time
        )

        val certificationDate = viewMvc.getCertificationDate()
        if (certificationDate.isNotBlank()) {
            invoice.certificatedAt = certificationDate.calendar().time
        }

        // Pass the invoice draft to InvoiceFormPaymentFragment to continue working on it.
        fragmentsEventBus.postEvent(
            InvoiceFormDetailsFragmentEvent(invoice)
        )
    }
}