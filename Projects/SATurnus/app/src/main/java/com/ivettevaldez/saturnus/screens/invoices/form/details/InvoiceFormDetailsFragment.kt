package com.ivettevaldez.saturnus.screens.invoices.form.details

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.datetime.DatesHelper.calendar
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import javax.inject.Inject

class InvoiceFormDetailsFragment : BaseFragment(),
    Step {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var fragmentsEventBus: FragmentsEventBus

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var personDao: PersonDao

    private lateinit var controller: InvoiceFormDetailsController
    private lateinit var viewMvc: IInvoiceFormDetailsViewMvc

    private var issuingRfc: String? = null
    private var receiverRfc: String? = null

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

        controller = controllerFactory.newInvoiceFormDetailsController()

        bindArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormDetailsViewMvc(parent)

        controller.bindView(viewMvc)
        controller.bindData()

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
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

    private fun getPerson(rfc: String): Person? = personDao.findByRfc(rfc)

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

    private fun bindArguments() {
        requireArguments().let {
            val issuingRfc = it.getString(ARG_ISSUING_RFC)
            val folio = it.getString(ARG_FOLIO)

            controller.bindArguments(issuingRfc, folio)
        }
    }
}