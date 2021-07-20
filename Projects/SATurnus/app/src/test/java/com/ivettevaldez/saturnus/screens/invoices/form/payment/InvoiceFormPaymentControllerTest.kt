package com.ivettevaldez.saturnus.screens.invoices.form.payment

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.payment.GenerateInvoicePaymentUseCase
import com.ivettevaldez.saturnus.invoices.payment.InvoicePayment
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsFragmentEvent
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentControllerTest.FakeFieldValues.ALL_FILLED
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentControllerTest.FakeFieldValues.SUBTOTAL_IS_ZERO
import com.ivettevaldez.saturnus.testdata.InvoiceTestData
import com.stepstone.stepper.VerificationError
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class InvoiceFormPaymentControllerTest {

    private lateinit var sut: InvoiceFormPaymentController

    @Mock
    private lateinit var contextMock: Context

    @Mock
    private lateinit var viewMvcMock: IInvoiceFormPaymentViewMvc

    @Mock
    private lateinit var dialogsManagerMock: DialogsManager

    @Mock
    private lateinit var fragmentsEventBusMock: FragmentsEventBus

    @Mock
    private lateinit var generateInvoicePaymentUseCaseMock: GenerateInvoicePaymentUseCase

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    @Mock
    private lateinit var invoiceFormDetailsFragmentEventMock: InvoiceFormDetailsFragmentEvent

    @Mock
    private lateinit var verificationErrorMock: VerificationError

    @Captor
    private val stringCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

    @Captor
    private val paymentEventCaptor: ArgumentCaptor<InvoiceFormPaymentFragmentEvent> =
        ArgumentCaptor.forClass(InvoiceFormPaymentFragmentEvent::class.java)

    private val fakeInvoice: Invoice = InvoiceTestData.getInvoice()
    private val fakeInvoicePayment: InvoicePayment = InvoiceTestData.getPayment()

    private val folio: String = fakeInvoice.folio

    enum class FakeFieldValues {

        ALL_FILLED, SUBTOTAL_IS_ZERO
    }

    companion object {

        private const val DELTA: Double = 0.0
        private const val SUBTOTAL: String = "1000000"

        private const val UNAVAILABLE_STRING_ID: Int = R.string.default_unavailable
        private const val UNAVAILABLE_STRING_VALUE: String = "N/A"

        private const val DEFAULT_ZERO_ID: Int = R.string.default_zero
        private const val DEFAULT_ZERO_VALUE: String = "0"

        private const val ERROR_MISSING_SUBTOTAL_ID: Int = R.string.error_missing_subtotal
        private const val ERROR_MISSING_CALCULATION_ID: Int = R.string.error_missing_calculation
        private const val ERROR_MISSING_RECALCULATION_ID: Int = R.string.error_missing_recalculation
    }

    @Before
    fun setUp() {
        sut = InvoiceFormPaymentController(
            contextMock,
            dialogsManagerMock,
            generateInvoicePaymentUseCaseMock,
            fragmentsEventBusMock,
            invoiceDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun findInvoice_queryTheDatabaseAndSetInvoiceAndSubtotalIfInvoiceIsFound() {
        // Arrange
        findInvoiceByFolio()
        // Act
        sut.findInvoice(folio)
        // Assert
        verify(invoiceDaoMock).findByFolio(folio)
        assertNotNull(sut.invoice)
        assertNotNull(sut.subtotal)
    }

    @Test
    fun bindArguments_newInvoice_folioIsNull() {
        // Arrange
        newInvoice()
        // Act
        // Assert
        assertTrue(sut.newInvoice)
        assertFalse(sut.editingInvoice)
        assertNull(sut.invoice)
        assertNull(sut.subtotal)
    }

    @Test
    fun bindArguments_editingInvoice_folioIsNotNull() {
        // Arrange
        editingInvoice()
        // Act
        // Assert
        assertFalse(sut.newInvoice)
        assertTrue(sut.editingInvoice)
        assertNotNull(sut.invoice)
        assertNotNull(sut.subtotal)
    }

    @Test
    fun bindData_newInvoice_noDataIsBoundToView() {
        // Arrange
        newInvoice()
        // Act
        sut.bindData()
        // Assert
        verifyNoInteractions(viewMvcMock)
    }

    @Test
    fun bindData_editingInvoice_invoicePaymentIsBoundToView() {
        // Arrange
        editingInvoice()
        // Act
        sut.bindData()
        // Assert
        verify(viewMvcMock).bindPayment(
            anyString(),
            anyString(),
            anyString(),
            anyString(),
            anyString()
        )
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).registerListener(sut)
        verify(fragmentsEventBusMock).registerListener(sut)
    }

    @Test
    fun onStop_listenersUnregistered() {
        // Arrange
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
        verify(fragmentsEventBusMock).unregisterListener(sut)
    }

    @Test
    fun onFragmentEvent_eventIsInvoiceFormDetailsFragmentEvent_invoiceAndReceiverPersonTypeAreBoundToView() {
        // Arrange
        setInvoiceFormDetailsFragmentEvent()
        // Act
        sut.onFragmentEvent(invoiceFormDetailsFragmentEventMock)
        // Assert
        assertTrue(sut.hasChanges)
        assertNotNull(sut.invoice)
        assertNotNull(sut.receiverPersonType)
    }

    @Test
    fun onSubtotalChanged_hasChangesIsTrue() {
        // Arrange
        // Act
        sut.onSubtotalChanged()
        // Assert
        assertTrue(sut.hasChanges)
    }

    @Test
    fun onCalculateClicked_emptySubtotal_genericSavingErrorDialogIsShown() {
        // Arrange
        getMissingSubtotalString()
        // Act
        sut.onCalculateClicked("")
        // Assert
        verify(contextMock).getString(ERROR_MISSING_SUBTOTAL_ID)
        verify(dialogsManagerMock).showGenericSavingError(any(), anyString())
    }

    @Test
    fun onCalculateClicked_generatedPaymentIsBoundToInvoiceAndHasChangesIsFalse() {
        // Arrange
        generateInvoicePayment(Constants.PHYSICAL_PERSON)
        // Act
        sut.onCalculateClicked(SUBTOTAL)
        // Assert
        val invoicePayment = sut.invoice!!.payment!!
        assertEquals(invoicePayment.subtotal, fakeInvoicePayment.subtotal, DELTA)
        assertEquals(invoicePayment.iva, fakeInvoicePayment.iva, DELTA)
        assertEquals(invoicePayment.ivaWithholding, fakeInvoicePayment.ivaWithholding, DELTA)
        assertEquals(invoicePayment.isrWithholding, fakeInvoicePayment.isrWithholding, DELTA)
        assertEquals(invoicePayment.total, fakeInvoicePayment.total, DELTA)

        assertFalse(sut.hasChanges)
    }

    @Test
    fun onCalculateClicked_physicalReceiver_generatedPaymentIsBoundToViewWithDefaultWithholdings() {
        // Arrange
        generateInvoicePayment(Constants.PHYSICAL_PERSON)
        // Act
        sut.onCalculateClicked(SUBTOTAL)
        // Assert
        verify(viewMvcMock).bindPayment(
            subtotal = anyString(),
            iva = anyString(),
            ivaWithholding = capture(stringCaptor),
            isrWithholding = capture(stringCaptor),
            total = anyString()
        )

        val ivaWithholding: String = stringCaptor.allValues[0]
        assertEquals(ivaWithholding, UNAVAILABLE_STRING_VALUE)
        val isrWithholding: String = stringCaptor.allValues[1]
        assertEquals(isrWithholding, UNAVAILABLE_STRING_VALUE)
    }

    @Test
    fun onCalculateClicked_moralReceiver_generatedPaymentIsBoundToView() {
        // Arrange
        generateInvoicePayment(Constants.MORAL_PERSON)
        // Act
        sut.onCalculateClicked(SUBTOTAL)
        // Assert
        verify(viewMvcMock).bindPayment(
            subtotal = anyString(),
            iva = anyString(),
            ivaWithholding = capture(stringCaptor),
            isrWithholding = capture(stringCaptor),
            total = anyString()
        )

        val ivaWithholding: String = stringCaptor.allValues[0]
        assertNotEquals(ivaWithholding, UNAVAILABLE_STRING_VALUE)
        val isrWithholding: String = stringCaptor.allValues[1]
        assertNotEquals(isrWithholding, UNAVAILABLE_STRING_VALUE)
    }

    @Test
    fun verifyStep_missingCalculationError_returnsVerificationErrorWithCorrectMessage() {
        // Arrange
        missingCalculationError()
        // Act
        val result = sut.verifyStep()
        // Assert
        assertNotNull(result)
        assert(result is VerificationError)
        verify(contextMock).getString(ERROR_MISSING_CALCULATION_ID)
    }

    @Test
    fun verifyStep_missingRecalculationError_returnsVerificationErrorWithCorrectMessage() {
        // Arrange
        missingRecalculationError()
        // Act
        val result = sut.verifyStep()
        // Assert
        assertNotNull(result)
        assert(result is VerificationError)
        verify(contextMock).getString(ERROR_MISSING_RECALCULATION_ID)
    }

    @Test
    fun verifyStep_noFormErrorsAndPhysicalReceiver_setsDefaultValuesAndPostsCompleteInvoice() {
        // Arrange
        noFormErrors(Constants.PHYSICAL_PERSON)
        // Act
        val result = sut.verifyStep()
        // Assert
        assertNull(result)

        verify(viewMvcMock).bindPayment(
            subtotal = DEFAULT_ZERO_VALUE.toCurrency(),
            iva = DEFAULT_ZERO_VALUE.toCurrency(),
            ivaWithholding = UNAVAILABLE_STRING_VALUE,
            isrWithholding = UNAVAILABLE_STRING_VALUE,
            total = DEFAULT_ZERO_VALUE.toCurrency()
        )

        verify(fragmentsEventBusMock).postEvent(capture(paymentEventCaptor))
    }

    @Test
    fun verifyStep_noFormErrorsAndMoralReceiver_setsDefaultValuesAndPostsCompleteInvoice() {
        // Arrange
        noFormErrors(Constants.MORAL_PERSON)
        // Act
        val result = sut.verifyStep()
        // Assert
        assertNull(result)

        verify(viewMvcMock).bindPayment(
            subtotal = DEFAULT_ZERO_VALUE.toCurrency(),
            iva = DEFAULT_ZERO_VALUE.toCurrency(),
            ivaWithholding = DEFAULT_ZERO_VALUE.toCurrency(),
            isrWithholding = DEFAULT_ZERO_VALUE.toCurrency(),
            total = DEFAULT_ZERO_VALUE.toCurrency()
        )

        verify(fragmentsEventBusMock).postEvent(capture(paymentEventCaptor))
    }

    @Test
    fun onError_missingCalculationError_genericSavingErrorWithCorrectMessageIsShown() {
        // Arrange
        missingCalculationError()
        // Act
        sut.onError(verificationErrorMock)
        // Assert
        verify(dialogsManagerMock).showGenericSavingError(
            null,
            verificationErrorMock.errorMessage
        )
    }

    @Test
    fun onError_missingRecalculationError_genericSavingErrorWithCorrectMessageIsShown() {
        // Arrange
        missingRecalculationError()
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
        sut.bindArguments(null)
    }

    private fun editingInvoice() {
        `when`(invoiceDaoMock.findByFolio(folio)).thenReturn(fakeInvoice)

        sut.bindArguments(folio)
    }

    private fun findInvoiceByFolio() {
        `when`(invoiceDaoMock.findByFolio(folio)).thenReturn(fakeInvoice)
    }

    private fun setInvoiceFormDetailsFragmentEvent() {
        `when`(invoiceFormDetailsFragmentEventMock.invoice).thenReturn(fakeInvoice)
    }

    private fun generateInvoicePayment(receiverPersonType: String) {
        sut.receiverPersonType = receiverPersonType

        editingInvoice()
        getUnavailableString()

        `when`(
            generateInvoicePaymentUseCaseMock.generatePayment(
                anyDouble(),
                anyString()
            )
        ).thenReturn(fakeInvoicePayment)
    }

    private fun getUnavailableString() {
        `when`(contextMock.getString(UNAVAILABLE_STRING_ID)).thenReturn(UNAVAILABLE_STRING_VALUE)
    }

    private fun getDefaultZeroString() {
        `when`(contextMock.getString(DEFAULT_ZERO_ID)).thenReturn(DEFAULT_ZERO_VALUE)
    }

    private fun getMissingSubtotalString() {
        `when`(contextMock.getString(ERROR_MISSING_SUBTOTAL_ID)).thenReturn("")
    }

    private fun getVerificationErrorMessage() {
        `when`(verificationErrorMock.errorMessage).thenReturn("")
    }

    private fun missingCalculationError() {
        `when`(contextMock.getString(ERROR_MISSING_CALCULATION_ID)).thenReturn("")

        sut.hasChanges = false

        getDefaultZeroString()
        getVerificationErrorMessage()
        setFieldsReturnValues(SUBTOTAL_IS_ZERO)
    }

    private fun missingRecalculationError() {
        `when`(contextMock.getString(ERROR_MISSING_RECALCULATION_ID)).thenReturn("")

        sut.hasChanges = true

        getDefaultZeroString()
        getVerificationErrorMessage()
        setFieldsReturnValues(ALL_FILLED)
    }

    private fun noFormErrors(receiverPersonType: String) {
        sut.hasChanges = false
        sut.receiverPersonType = receiverPersonType

        getDefaultZeroString()
        getUnavailableString()
        editingInvoice()
    }

    private fun setFieldsReturnValues(fieldValues: FakeFieldValues) {
        val subtotal: String
        val otherFieldValue: String

        when (fieldValues) {
            ALL_FILLED -> {
                subtotal = "x"
                otherFieldValue = "x"
            }
            SUBTOTAL_IS_ZERO -> {
                subtotal = DEFAULT_ZERO_VALUE.toCurrency()
                otherFieldValue = "x"
            }
            else -> {
                throw IllegalArgumentException("Unsupported fake field values: $fieldValues")
            }
        }

        `when`(viewMvcMock.getSubtotal()).thenReturn(subtotal)
        `when`(viewMvcMock.getIva()).thenReturn(otherFieldValue)
        `when`(viewMvcMock.getTotal()).thenReturn(otherFieldValue)
    }
}