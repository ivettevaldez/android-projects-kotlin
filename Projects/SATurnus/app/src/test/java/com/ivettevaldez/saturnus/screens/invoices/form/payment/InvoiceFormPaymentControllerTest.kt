package com.ivettevaldez.saturnus.screens.invoices.form.payment

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.payment.InvoicePayment
import com.ivettevaldez.saturnus.invoices.payment.calculator.InvoicePaymentCalculator
import com.ivettevaldez.saturnus.invoices.payment.calculator.InvoicePaymentCalculatorFactory
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormChangeFragmentEvent
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
import org.mockito.kotlin.anyOrNull
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
    private lateinit var invoicePaymentCalculatorFactoryMock: InvoicePaymentCalculatorFactory

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    @Mock
    private lateinit var invoiceFormDetailsFragmentEventMock: InvoiceFormDetailsFragmentEvent

    @Mock
    private lateinit var wrongEventMock: InvoiceFormChangeFragmentEvent

    @Mock
    private lateinit var verificationErrorMock: VerificationError

    @Mock
    private lateinit var invoicePaymentCalculatorMock: InvoicePaymentCalculator

    @Captor
    private val paymentEventCaptor: ArgumentCaptor<InvoiceFormPaymentFragmentEvent> =
        ArgumentCaptor.forClass(InvoiceFormPaymentFragmentEvent::class.java)

    private val fakeInvoice: Invoice = InvoiceTestData.getInvoice()
    private val fakeInvoicePayment: InvoicePayment = fakeInvoice.payment!!

    private val folio: String = fakeInvoice.folio

    enum class FakeFieldValues {

        ALL_FILLED, SUBTOTAL_IS_ZERO
    }

    companion object {

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
            fragmentsEventBusMock,
            invoicePaymentCalculatorFactoryMock,
            invoiceDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun initVariables_nullFolio_configuresNewInvoice() {
        // Arrange
        newInvoice()
        // Act
        // Assert
        verify(invoiceDaoMock, never()).findByFolio(anyOrNull())

        assertTrue(sut.newInvoice)
        assertFalse(sut.editingInvoice)
        assertNull(sut.invoice)
        assertNull(sut.invoicePayment)
    }

    @Test
    fun initVariables_folioIsNotNull_configuresEditingInvoice() {
        // Arrange
        editingInvoice()
        // Act
        // Assert
        verify(invoiceDaoMock).findByFolio(folio)

        assertFalse(sut.newInvoice)
        assertTrue(sut.editingInvoice)
        assertEquals(sut.invoice, fakeInvoice)
        assertEquals(sut.invoicePayment, fakeInvoicePayment)
    }

    @Test
    fun onStart_newInvoice_noDataIsBoundToView() {
        // Arrange
        newInvoice()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock, never()).bindPayment(
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull()
        )
    }

    @Test
    fun onStart_editingInvoiceWithPhysicalReceiver_invoicePaymentIsBoundToViewWithCorrectData() {
        // Arrange
        editingInvoiceWithPhysicalReceiver()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).bindPayment(
            subtotal = anyString(),
            iva = anyString(),
            ivaWithholding = org.mockito.kotlin.eq(UNAVAILABLE_STRING_VALUE),
            isrWithholding = org.mockito.kotlin.eq(UNAVAILABLE_STRING_VALUE),
            total = anyString()
        )
    }

    @Test
    fun onStart_editingInvoiceWithMoralReceiver_invoicePaymentIsBoundToViewWithCorrectData() {
        // Arrange
        editingInvoiceWithMoralReceiver()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).bindPayment(
            subtotal = anyString(),
            iva = anyString(),
            ivaWithholding = anyString(),
            isrWithholding = anyString(),
            total = anyString()
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
    fun onFragmentEvent_eventIsNotInvoiceFormDetailsFragmentEvent_noDataIsBound() {
        // Arrange
        sut.invoice = null
        sut.receiverPersonType = null
        sut.invoicePayment = null
        // Act
        sut.onFragmentEvent(wrongEventMock)
        // Assert
        assertNull(sut.invoice)
        assertNull(sut.receiverPersonType)
        assertNull(sut.invoicePaymentCalculator)
        assertNull(sut.invoicePayment)
        verifyNoInteractions(viewMvcMock)
    }

    @Test
    fun onFragmentEvent_newInvoiceWithInvoiceFormDetailsFragmentEvent_onlyInvoiceAndReceiverPersonTypeAreBound() {
        // Arrange
        setInvoiceFormDetailsFragmentEvent()
        getInvoicePaymentCalculator()
        // Act
        sut.onFragmentEvent(invoiceFormDetailsFragmentEventMock)
        // Assert
        assertNotNull(sut.invoice)
        assertNotNull(sut.receiverPersonType)
        assertNotNull(sut.invoicePaymentCalculator)
        verifyNoInteractions(viewMvcMock)
    }

    @Test
    fun onFragmentEvent_editingInvoiceWithInvoiceFormDetailsFragmentEvent_dataIsBoundToView() {
        // Arrange
        editingInvoiceWithInvoiceFormDetailsFragmentEvent()
        // Act
        sut.onFragmentEvent(invoiceFormDetailsFragmentEventMock)
        // Assert
        assertNotNull(sut.invoice)
        assertNotNull(sut.receiverPersonType)
        assertEquals(sut.invoicePayment, fakeInvoicePayment)
        assertFalse(sut.hasChanges)

        verify(viewMvcMock).bindPayment(
            anyString(),
            anyString(),
            anyString(),
            anyString(),
            anyString()
        )
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
        getDefaultZeroString()
        // Act
        sut.onCalculateClicked("")
        // Assert
        verify(dialogsManagerMock).showMissingSubtotalError(any())
    }

    @Test
    fun onCalculateClicked_physicalReceiver_generatedPaymentIsBoundToViewWithDefaultWithholdings() {
        // Arrange
        generateInvoicePayment(Constants.PHYSICAL_PERSON)
        // Act
        sut.onCalculateClicked(SUBTOTAL)
        // Assert
        assertEquals(sut.invoicePayment, fakeInvoicePayment)
        assertFalse(sut.hasChanges)

        verify(viewMvcMock).bindPayment(
            subtotal = anyString(),
            iva = anyString(),
            ivaWithholding = org.mockito.kotlin.eq(UNAVAILABLE_STRING_VALUE),
            isrWithholding = org.mockito.kotlin.eq(UNAVAILABLE_STRING_VALUE),
            total = anyString()
        )
    }

    @Test
    fun onCalculateClicked_moralReceiver_generatedPaymentIsBoundToView() {
        // Arrange
        generateInvoicePayment(Constants.MORAL_PERSON)
        // Act
        sut.onCalculateClicked(SUBTOTAL)
        // Assert
        assertEquals(sut.invoicePayment, fakeInvoicePayment)
        assertFalse(sut.hasChanges)

        verify(viewMvcMock).bindPayment(
            subtotal = anyString(),
            iva = anyString(),
            ivaWithholding = anyString(),
            isrWithholding = anyString(),
            total = anyString()
        )
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
        assertEquals(sut.invoice!!.payment!!, sut.invoicePayment!!)

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
        assertEquals(sut.invoice!!.payment!!, sut.invoicePayment!!)

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
        sut.initVariables(null)
    }

    private fun editingInvoice() {
        findInvoiceByFolio()
        sut.initVariables(folio)
    }

    private fun editingInvoiceWithPhysicalReceiver() {
        editingInvoice()
        getUnavailableString()
        sut.receiverPersonType = Constants.PHYSICAL_PERSON
    }

    private fun editingInvoiceWithInvoiceFormDetailsFragmentEvent() {
        editingInvoiceWithPhysicalReceiver()
        getInvoicePaymentCalculator()
        generateInvoicePayment(sut.receiverPersonType!!)
        setInvoiceFormDetailsFragmentEvent()
        getSubtotal()
    }

    private fun editingInvoiceWithMoralReceiver() {
        editingInvoice()
        sut.receiverPersonType = Constants.MORAL_PERSON
    }

    private fun findInvoiceByFolio() {
        `when`(invoiceDaoMock.findByFolio(folio)).thenReturn(fakeInvoice)
    }

    private fun setInvoiceFormDetailsFragmentEvent() {
        `when`(invoiceFormDetailsFragmentEventMock.invoice).thenReturn(fakeInvoice)
    }

    private fun getInvoicePaymentCalculator() {
        `when`(
            invoicePaymentCalculatorFactoryMock.newInvoicePaymentCalculator(anyString())
        ).thenReturn(
            invoicePaymentCalculatorMock
        )
    }

    private fun generateInvoicePayment(receiverPersonType: String) {
        sut.receiverPersonType = receiverPersonType
        sut.invoicePaymentCalculator = invoicePaymentCalculatorMock

        editingInvoice()
        getUnavailableString()
        getDefaultZeroString()

        `when`(
            invoicePaymentCalculatorMock.calculatePayment(anyDouble())
        ).thenReturn(
            fakeInvoicePayment
        )
    }

    private fun getSubtotal() {
        `when`(viewMvcMock.getSubtotal()).thenReturn(SUBTOTAL)
    }

    private fun getUnavailableString() {
        `when`(contextMock.getString(UNAVAILABLE_STRING_ID)).thenReturn(UNAVAILABLE_STRING_VALUE)
    }

    private fun getDefaultZeroString() {
        `when`(contextMock.getString(DEFAULT_ZERO_ID)).thenReturn(DEFAULT_ZERO_VALUE)
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