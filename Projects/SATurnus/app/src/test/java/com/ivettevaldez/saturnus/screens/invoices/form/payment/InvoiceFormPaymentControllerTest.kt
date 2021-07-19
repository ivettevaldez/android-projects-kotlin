package com.ivettevaldez.saturnus.screens.invoices.form.payment

/* ktlint-disable no-wildcard-imports */

import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
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
    private lateinit var viewMvcMock: IInvoiceFormPaymentViewMvc

    @Mock
    private lateinit var fragmentsEventBusMock: FragmentsEventBus

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    @Mock
    private lateinit var invoiceFormDetailsFragmentEventMock: InvoiceFormDetailsFragmentEvent

    private val fakeInvoice: Invoice = InvoiceTestData.getInvoice()

    private val folio: String = fakeInvoice.folio

    @Before
    fun setUp() {
        sut = InvoiceFormPaymentController(
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
        editingInvoice()
        val subtotal = fakeInvoice.payment!!.subtotal.toCurrency()
        // Act
        sut.onCalculateClicked(subtotal)
        // Assert
        assertNotNull(sut.invoice!!.payment)
        assertFalse(sut.hasChanges)
    }

    // onCalculateClicked_moralReceiverPerson_generatedPaymentIsBoundToViewWithDefaultWithholdings
    // onCalculateClicked_physicalReceiverPerson_generatedPaymentIsBoundToView

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
}