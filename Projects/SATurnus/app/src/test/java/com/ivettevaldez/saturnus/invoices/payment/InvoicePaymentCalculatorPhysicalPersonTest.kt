package com.ivettevaldez.saturnus.invoices.payment

import com.ivettevaldez.saturnus.testdata.InvoiceTestData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InvoicePaymentCalculatorPhysicalPersonTest {

    private lateinit var sut: InvoicePaymentCalculatorPhysicalPerson

    companion object {

        private const val SUBTOTAL: Double = 10000.0
        private const val DELTA: Double = 0.0
    }

    @Before
    fun setUp() {
        sut = InvoicePaymentCalculatorPhysicalPerson()
    }

    @Test
    fun calculatePayment_returnsCorrectPayment() {
        // Arrange
        val expectedPayment: InvoicePayment = InvoiceTestData.getPhysicalPersonPayment()
        // Act
        val result: InvoicePayment = sut.calculatePayment(SUBTOTAL)
        // Assert
        assertEquals(result.subtotal, expectedPayment.subtotal, DELTA)
        assertEquals(result.iva, expectedPayment.iva, DELTA)
        assertEquals(result.ivaWithholding, expectedPayment.ivaWithholding, DELTA)
        assertEquals(result.isrWithholding, expectedPayment.isrWithholding, DELTA)
        assertEquals(result.total, expectedPayment.total, DELTA)
    }
}