package com.ivettevaldez.saturnus.screens.invoices.form

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.datepickers.DatePickerManager
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.IPersonSelectorBottomSheetViewMvc
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*
import javax.inject.Inject

class InvoiceFormFragment : BaseFragment(),
    IInvoiceFormViewMvc.Listener,
    IPersonSelectorBottomSheetViewMvc.Listener,
    DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var datePickerManager: DatePickerManager

    @Inject
    lateinit var personDao: PersonDao

    private lateinit var viewMvc: IInvoiceFormViewMvc
    private lateinit var issuingRfc: String

    private var receiverRfc: String? = null

    companion object {

        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"

        @JvmStatic
        fun newInstance(issuingRfc: String) =
            InvoiceFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ISSUING_RFC, issuingRfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            issuingRfc = it.getString(ARG_ISSUING_RFC)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormViewMvc(parent)

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

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = datePickerManager.getUserFriendlyDate(year, monthOfYear, dayOfMonth)

        if (view?.tag == DatePickerManager.TAG_ISSUING_DATE) {
            viewMvc.setIssuingDate(date)
        } else if (view?.tag == DatePickerManager.TAG_CERTIFICATION_DATE) {
            viewMvc.setCertificationDate(date)
        }
    }

    override fun onSelectReceiverClicked() {
        dialogsManager.showPersonSelectorDialog(null, this)
    }

    override fun onSelectIssuingDateClicked() {
        val currentDate = viewMvc.getIssuingDate().getDate()
        datePickerManager.showDatePicker(
            currentDate,
            this,
            DatePickerManager.TAG_ISSUING_DATE
        )
    }

    override fun onSelectCertificationDateClicked() {
        val currentDate = viewMvc.getCertificationDate().getDate()
        datePickerManager.showDatePicker(
            currentDate,
            this,
            DatePickerManager.TAG_CERTIFICATION_DATE
        )
    }

    override fun onPersonSelected(rfc: String) {
        receiverRfc = rfc
        bindReceiverPerson()
    }

    override fun onSaveClicked() {
        // TODO:
    }

    private fun getPerson(rfc: String): Person? = personDao.findByRfc(rfc)

    private fun String?.getDate(): Calendar = if (this == null) {
        Calendar.getInstance()
    } else {
        datePickerManager.parseToCalendar(this)
    }

    private fun bindIssuingPerson() {
        val person = getPerson(issuingRfc)
        if (person != null) {
            viewMvc.bindIssuingPerson(person)
        } else {
            throw RuntimeException("Person with RFC $issuingRfc cannot be found")
        }
    }

    private fun bindReceiverPerson() {
        val person = getPerson(receiverRfc!!)
        if (person != null) {
            viewMvc.bindReceiverPerson(person)
        } else {
            throw RuntimeException("Person with RFC $receiverRfc cannot be found")
        }
    }
}