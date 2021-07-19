package com.ivettevaldez.saturnus.screens.invoices.form.details

/* ktlint-disable no-wildcard-imports */

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
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormChangeFragmentEvent
import com.ivettevaldez.saturnus.testdata.InvoiceTestData
import com.ivettevaldez.saturnus.testdata.PeopleTestData
import com.stepstone.stepper.VerificationError
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class InvoiceFormDetailsControllerTest {

    private lateinit var sut: InvoiceFormDetailsController

    @Mock
    private lateinit var contextMock: Context

    @Mock
    private lateinit var viewMvcMock: IInvoiceFormDetailsViewMvc

    @Mock
    private lateinit var fragmentsEventBusMock: FragmentsEventBus

    @Mock
    private lateinit var dialogsManagerMock: DialogsManager

    @Mock
    private lateinit var datePickerManager: DatePickerManager

    @Mock
    private lateinit var personDaoMock: PersonDao

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    @Mock
    private lateinit var datePickerDialog: DatePickerDialog

    @Mock
    private lateinit var verificationErrorMock: VerificationError

    @Captor
    private val changeEventCaptor: ArgumentCaptor<InvoiceFormChangeFragmentEvent> =
        ArgumentCaptor.forClass(InvoiceFormChangeFragmentEvent::class.java)

    @Captor
    private val invoiceDetailsEventCaptor: ArgumentCaptor<InvoiceFormDetailsFragmentEvent> =
        ArgumentCaptor.forClass(InvoiceFormDetailsFragmentEvent::class.java)

    private val fakeInvoice: Invoice = InvoiceTestData.getInvoice()
    private val fakeIssuingPerson: Person = PeopleTestData.getPhysicalPerson()
    private val fakeReceiverPerson: Person = PeopleTestData.getMoralPerson()

    private val issuingRfc: String = fakeIssuingPerson.rfc
    private val receiverRfc: String = fakeReceiverPerson.rfc
    private val folio: String = fakeInvoice.folio
    private val issuingDate: Date = fakeInvoice.issuedAt!!
    private val certificationDate: Date = fakeInvoice.certificatedAt!!

    enum class FakeFieldValues {

        ALL_BLANK, NOT_BLANK, ONLY_LAST_ONE_IS_BLANK
    }

    companion object {

        private const val ISSUING_DATE = "1/January/2000"
        private const val CERTIFICATION_DATE = "1/January/1990"

        private const val DAY: Int = 1
        private const val MONTH: Int = 0
        private const val YEAR: Int = 200
    }

    @Before
    fun setUp() {
        sut = InvoiceFormDetailsController(
            contextMock,
            fragmentsEventBusMock,
            dialogsManagerMock,
            datePickerManager,
            personDaoMock,
            invoiceDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun findInvoice_queryTheDatabaseAndSetInvoiceAndRfcsIfInvoiceIsFound() {
        // Arrange
        `when`(invoiceDaoMock.findByFolio(folio)).thenReturn(fakeInvoice)
        sut.folio = folio
        // Act
        sut.findInvoiceAndRfcs()
        // Assert
        verify(invoiceDaoMock).findByFolio(folio)
        assertNotNull(sut.invoice)
        assertNotNull(sut.issuingRfc)
        assertNotNull(sut.receiverRfc)
    }

    @Test
    fun bindArguments_newInvoice_issuingRfcIsNotNull() {
        // Arrange
        newInvoice()
        // Act
        // Assert
        assertTrue(sut.newInvoice)
        assertFalse(sut.editingInvoice)
        assertNull(sut.folio)
        assertNull(sut.invoice)
        assertNotNull(sut.issuingRfc)
        assertNull(sut.receiverRfc)
    }

    @Test
    fun bindArguments_editingInvoice_invoiceAndRfcsAreNotNull() {
        // Arrange
        editingInvoice()
        // Act
        // Assert
        assertFalse(sut.newInvoice)
        assertTrue(sut.editingInvoice)
        assertNotNull(sut.folio)
        assertNotNull(sut.invoice)
        assertNotNull(sut.issuingRfc)
        assertNotNull(sut.receiverRfc)
    }

    @Test
    fun bindData_newInvoice_onlyIssuingRfcIsBoundToView() {
        // Arrange
        newInvoice()
        // Act
        sut.bindData()
        // Assert
        verify(personDaoMock).findByRfc(issuingRfc)
        verify(viewMvcMock).bindIssuingPerson(fakeIssuingPerson)
        verifyNoMoreInteractions(viewMvcMock)
    }

    @Test
    fun bindData_editingInvoiceWithCertificationDate_issuingPersonAndInvoiceWithBothDatesAreBoundToView() {
        // Arrange
        editingInvoice()
        // Act
        sut.bindData()
        // Assert
        verify(personDaoMock).findByRfc(issuingRfc)
        verify(viewMvcMock).bindIssuingPerson(fakeIssuingPerson)

        verify(viewMvcMock).bindInvoice(fakeInvoice)
        verify(viewMvcMock).setIssuingDate(issuingDate.friendlyDate())
        verify(viewMvcMock).setCertificationDate(certificationDate.friendlyDate())
    }

    @Test
    fun bindData_editingInvoiceWithoutCertificationDate_issuingPersonAndInvoiceWithIssuingDateAreBoundToView() {
        // Arrange
        editingInvoice()
        fakeInvoice.certificatedAt = null
        // Act
        sut.bindData()
        // Assert
        verify(personDaoMock).findByRfc(issuingRfc)
        verify(viewMvcMock).bindIssuingPerson(fakeIssuingPerson)

        verify(viewMvcMock).bindInvoice(fakeInvoice)
        verify(viewMvcMock).setIssuingDate(issuingDate.friendlyDate())
        verify(viewMvcMock, never()).setCertificationDate(any())
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).registerListener(sut)
    }

    @Test
    fun onStop_listenersUnregistered() {
        // Arrange
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
    }

    @Test
    fun onFieldChanged_hasNotNotifiedChanges_invoiceFormChangeFragmentEventPosted() {
        // Arrange
        newInvoice()
        sut.hasNotifiedChanges = false
        // Act
        sut.onFieldChanged()
        // Assert
        verify(fragmentsEventBusMock).postEvent(capture(changeEventCaptor))
        assert(changeEventCaptor.value is InvoiceFormChangeFragmentEvent)
        assertTrue(sut.hasNotifiedChanges)
    }

    @Test
    fun onFieldChanged_hasNotifiedChanges_invoiceFormChangeFragmentEventNotPosted() {
        // Arrange
        newInvoice()
        sut.hasNotifiedChanges = true
        // Act
        sut.onFieldChanged()
        // Assert
        verifyNoInteractions(fragmentsEventBusMock)
        assertTrue(sut.hasNotifiedChanges)
    }

    @Test
    fun onSelectReceiverClicked_personSelectorDialogIsShown() {
        // Arrange
        newInvoice()
        // Act
        sut.onSelectReceiverClicked()
        // Assert
        verify(dialogsManagerMock).showPersonSelectorDialog(null, sut)
    }

    @Test
    fun onPersonSelected_bindReceiverPersonAndNotifyFormChanges() {
        // Arrange
        newInvoice()
        sut.hasNotifiedChanges = false
        // Act
        sut.onPersonSelected(receiverRfc)
        // Assert
        assertNotNull(sut.receiverRfc)
        verify(viewMvcMock).bindReceiverPerson(fakeReceiverPerson)

        verify(fragmentsEventBusMock).postEvent(capture(changeEventCaptor))
        assert(changeEventCaptor.value is InvoiceFormChangeFragmentEvent)
        assertTrue(sut.hasNotifiedChanges)
    }

    @Test
    fun onSelectIssuingDateClicked_datePickerIsShownWithDateFromFieldAndCorrectTag() {
        // Arrange
        getIssuingDateFromField()
        // Act
        sut.onSelectIssuingDateClicked()
        // Assert
        val date = viewMvcMock.getIssuingDate().calendar()
        verify(datePickerManager).showDatePicker(date, sut, DatePickerManager.TAG_ISSUING_DATE)
    }

    @Test
    fun onSelectCertificationDateClicked_datePickerIsShownWithDateFromFieldAndCorrectTag() {
        // Arrange
        getCertificationDateFromField()
        // Act
        sut.onSelectCertificationDateClicked()
        // Assert
        val date = viewMvcMock.getCertificationDate().calendar()
        verify(datePickerManager).showDatePicker(
            date,
            sut,
            DatePickerManager.TAG_CERTIFICATION_DATE
        )
    }

    @Test
    fun onDateSet_issuingDateSet_issuingDateIsBoundToViewAndChangesNotified() {
        // Arrange
        issuingDateIsSet()
        // Act
        sut.onDateSet(datePickerDialog, YEAR, MONTH, DAY)
        // Assert
        verify(viewMvcMock).setIssuingDate(any())
        verify(viewMvcMock, never()).setCertificationDate(any())

        verify(fragmentsEventBusMock).postEvent(capture(changeEventCaptor))
        assert(changeEventCaptor.value is InvoiceFormChangeFragmentEvent)
        assertTrue(sut.hasNotifiedChanges)
    }

    @Test
    fun onDateSet_certificationDateSet_certificationDateIsBoundToViewAndChangesNotified() {
        // Arrange
        certificationDateIsSet()
        // Act
        sut.onDateSet(datePickerDialog, YEAR, MONTH, DAY)
        // Assert
        verify(viewMvcMock, never()).setIssuingDate(any())
        verify(viewMvcMock).setCertificationDate(any())

        verify(fragmentsEventBusMock).postEvent(capture(changeEventCaptor))
        assert(changeEventCaptor.value is InvoiceFormChangeFragmentEvent)
        assertTrue(sut.hasNotifiedChanges)
    }

    @Test
    fun verifyStep_nullReceiverError_returnsVerificationErrorWithCorrectMessage() {
        // Arrange
        nullReceiverError()
        // Act
        val result = sut.verifyStep()
        // Assert
        assertNotNull(result)
        assert(result is VerificationError)
        verify(contextMock).getString(R.string.error_missing_receiver)
    }

    @Test
    fun verifyStep_missingFieldsError_returnsVerificationErrorWithCorrectMessage() {
        // Arrange
        missingFieldsError()
        // Act
        val result = sut.verifyStep()
        // Assert
        assertNotNull(result)
        assert(result is VerificationError)
        verify(contextMock).getString(R.string.error_missing_fields)
    }

    @Test
    fun verifyStep_existingFolioError_returnsVerificationErrorWithCorrectMessage() {
        // Arrange
        existingFolioError()
        // Act
        val result = sut.verifyStep()
        // Assert
        assertNotNull(result)
        assert(result is VerificationError)
        verify(contextMock).getString(R.string.error_folio_must_be_unique)
    }

    @Test
    fun verifyStep_noFormErrors_generatesInvoiceAndReturnsNull() {
        // Arrange
        noFormErrors()
        // Act
        val result = sut.verifyStep()
        // Assert
        assertNull(result)
    }

    @Test
    fun generateInvoice_createsANewInvoiceAndPostsItAsAnInvoiceFormDetailsFragmentEvent() {
        // Arrange
        noFormErrors()
        // Act
        sut.generateInvoice()
        // Assert
        verify(fragmentsEventBusMock).postEvent(capture(invoiceDetailsEventCaptor))
        assert(invoiceDetailsEventCaptor.value is InvoiceFormDetailsFragmentEvent)
        // TODO: Check the required invoice fields
    }

    @Test
    fun onError_nullReceiverError_genericSavingErrorIsShown() {
        // Arrange
        nullReceiverError()
        // Act
        sut.onError(verificationErrorMock)
        // Assert
        verify(dialogsManagerMock).showGenericSavingError(
            null,
            verificationErrorMock.errorMessage
        )
    }

    @Test
    fun onError_missingFieldsError_genericSavingErrorIsShown() {
        // Arrange
        missingFieldsError()
        // Act
        sut.onError(verificationErrorMock)
        // Assert
        verify(dialogsManagerMock).showGenericSavingError(
            null,
            verificationErrorMock.errorMessage
        )
    }

    @Test
    fun onError_existingFolioError_genericSavingErrorIsShown() {
        // Arrange
        existingFolioError()
        // Act
        sut.onError(verificationErrorMock)
        // Assert
        verify(dialogsManagerMock).showGenericSavingError(
            null,
            verificationErrorMock.errorMessage
        )
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun newInvoice() {
        `when`(personDaoMock.findByRfc(issuingRfc)).thenReturn(fakeIssuingPerson)
        `when`(personDaoMock.findByRfc(receiverRfc)).thenReturn(fakeReceiverPerson)

        sut.bindArguments(issuingRfc, null)
    }

    private fun editingInvoice() {
        `when`(invoiceDaoMock.findByFolio(folio)).thenReturn(fakeInvoice)
        `when`(personDaoMock.findByRfc(issuingRfc)).thenReturn(fakeIssuingPerson)

        sut.bindArguments(null, folio)
    }

    private fun getIssuingDateFromField() {
        `when`(viewMvcMock.getIssuingDate()).thenReturn(ISSUING_DATE)
    }

    private fun getCertificationDateFromField() {
        `when`(viewMvcMock.getCertificationDate()).thenReturn(CERTIFICATION_DATE)
    }

    private fun issuingDateIsSet() {
        sut.selectedDatePicker = DatePickerManager.TAG_ISSUING_DATE
    }

    private fun certificationDateIsSet() {
        sut.selectedDatePicker = DatePickerManager.TAG_CERTIFICATION_DATE
    }

    private fun nullReceiverError() {
        sut.receiverRfc = null

        `when`(contextMock.getString(R.string.error_missing_receiver)).thenReturn("")
        `when`(verificationErrorMock.errorMessage).thenReturn("")
    }

    private fun missingFieldsError() {
        sut.receiverRfc = receiverRfc

        setFieldsReturnValues(FakeFieldValues.ONLY_LAST_ONE_IS_BLANK)

        `when`(contextMock.getString(R.string.error_missing_fields)).thenReturn("")
        `when`(verificationErrorMock.errorMessage).thenReturn("")
    }

    private fun existingFolioError() {
        sut.receiverRfc = receiverRfc
        sut.issuingRfc = issuingRfc

        setFieldsReturnValues(FakeFieldValues.NOT_BLANK)

        `when`(contextMock.getString(R.string.error_folio_must_be_unique)).thenReturn("")
        `when`(verificationErrorMock.errorMessage).thenReturn("")
        `when`(personDaoMock.invoiceFolioExists(anyString(), anyString())).thenReturn(true)
    }

    private fun noFormErrors() {
        sut.receiverRfc = receiverRfc
        sut.issuingRfc = issuingRfc

        setFieldsReturnValues(FakeFieldValues.NOT_BLANK)

        `when`(personDaoMock.invoiceFolioExists(anyString(), anyString())).thenReturn(false)
    }

    private fun setFieldsReturnValues(fieldValues: FakeFieldValues) {
        val fieldValue: String
        val dateValue: String
        val lastFieldValue: String

        when (fieldValues) {
            FakeFieldValues.ALL_BLANK -> {
                fieldValue = ""
                dateValue = ""
                lastFieldValue = ""
            }
            FakeFieldValues.NOT_BLANK -> {
                fieldValue = "x"
                dateValue = ISSUING_DATE
                lastFieldValue = "x"
            }
            FakeFieldValues.ONLY_LAST_ONE_IS_BLANK -> {
                fieldValue = "x"
                dateValue = ISSUING_DATE
                lastFieldValue = ""
            }
        }

        `when`(viewMvcMock.getFolio()).thenReturn(fieldValue)
        `when`(viewMvcMock.getConcept()).thenReturn(fieldValue)
        `when`(viewMvcMock.getDescription()).thenReturn(fieldValue)
        `when`(viewMvcMock.getEffect()).thenReturn(fieldValue)
        `when`(viewMvcMock.getStatus()).thenReturn(fieldValue)
        `when`(viewMvcMock.getIssuingDate()).thenReturn(dateValue)
        `when`(viewMvcMock.getCertificationDate()).thenReturn(dateValue)
        `when`(viewMvcMock.getCancellationStatus()).thenReturn(lastFieldValue)
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}