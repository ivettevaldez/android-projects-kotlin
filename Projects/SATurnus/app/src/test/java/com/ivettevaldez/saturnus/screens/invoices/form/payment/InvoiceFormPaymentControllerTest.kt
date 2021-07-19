package com.ivettevaldez.saturnus.screens.invoices.form.payment

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.invoices.payment.GenerateInvoicePaymentUseCase
import com.ivettevaldez.saturnus.invoices.payment.InvoicePayment
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.invoices.form.details.InvoiceFormDetailsFragmentEvent
import com.ivettevaldez.saturnus.testdata.InvoiceTestData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class InvoiceFormPaymentControllerTest {

    private lateinit var sut: InvoiceFormPaymentController

    @Mock
    private lateinit var contextMock: Context

    @Mock
    private lateinit var viewMvcMock: IInvoiceFormPaymentViewMvc

    @Mock
    private lateinit var fragmentsEventBusMock: FragmentsEventBus

    @Mock
    private lateinit var generateInvoicePaymentUseCaseMock: GenerateInvoicePaymentUseCase

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    @Mock
    private lateinit var invoiceFormDetailsFragmentEventMock: InvoiceFormDetailsFragmentEvent

    private val fakeInvoice: Invoice = InvoiceTestData.getInvoice()
    private val fakeInvoicePayment: InvoicePayment = InvoiceTestData.getPayment()

    private val folio: String = fakeInvoice.folio

    companion object {

        private const val DELTA: Double = 0.0
        private const val SUBTOTAL: String = "1000000"

        private const val UNAVAILABLE_STRING_ID: Int = R.string.default_unavailable
        private const val UNAVAILABLE_STRING_VALUE: String = "N/A"
    }

    @Before
    fun setUp() {
        sut = InvoiceFormPaymentController(
            contextMock,
            generateInvoicePaymentUseCaseMock,
            fragmentsEventBusMock,
            invoiceDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun findInvoice_queryTheDatabaseAndSetInvoiceAndSubtotalIfInvoiceIsFound() {
        // Arrange
        `when`(invoiceDaoMock.findByFolio(folio)).thenReturn(fakeInvoice)
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
    fun onCalculateClicked_generatedPaymentIsBoundToInvoiceAndHasChangesIsFalse() {
        // Arrange
        setInvoiceFormDetailsFragmentEvent()
        sut.onFragmentEvent(invoiceFormDetailsFragmentEventMock)
        generateInvoicePayment()
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

//    @Test
//    fun onCalculateClicked_physicalReceiver_generatedPaymentIsBoundToViewWithDefaultWithholdings() {
//        // Arrange
//        setInvoiceFormDetailsFragmentEvent()
//        sut.onFragmentEvent(invoiceFormDetailsFragmentEventMock)
//        generateInvoicePayment()
//        sut.receiverPersonType = Constants.PHYSICAL_PERSON
//        getUnavailableString()
//        // Act
//        sut.onCalculateClicked(SUBTOTAL)
//        // Assert
//        verify(viewMvcMock).bindPayment(
//            anyString(),
//            anyString(),
//            contains(UNAVAILABLE_STRING_VALUE),
//            contains(UNAVAILABLE_STRING_VALUE),
//            anyString()
//        )
//    }

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

    private fun setInvoiceFormDetailsFragmentEvent() {
        `when`(invoiceFormDetailsFragmentEventMock.invoice).thenReturn(fakeInvoice)
    }

    private fun generateInvoicePayment() {
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
}