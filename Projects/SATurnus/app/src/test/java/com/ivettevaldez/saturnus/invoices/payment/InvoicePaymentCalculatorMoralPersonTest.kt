package com.ivettevaldez.saturnus.invoices.payment

import com.ivettevaldez.saturnus.invoices.payment.calculator.InvoicePaymentCalculatorMoralPerson
import com.ivettevaldez.saturnus.testdata.InvoiceTestData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InvoicePaymentCalculatorMoralPersonTest {

    private lateinit var sut: InvoicePaymentCalculatorMoralPerson

    companion object {

        private const val SUBTOTAL: Double = 10000.0
        private const val DELTA: Double = 0.0
    }

    @Before
    fun setUp() {
        sut = InvoicePaymentCalculatorMoralPerson()
    }

    @Test
    fun calculatePayment() {
        // Arrange
        val expectedPayment: InvoicePayment = InvoiceTestData.getMoralPersonPayment()
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