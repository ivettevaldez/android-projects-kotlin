package com.ivettevaldez.saturnus.invoices.payment

import com.ivettevaldez.saturnus.common.Constants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GenerateInvoicePaymentUseCaseTest {

    private lateinit var sut: GenerateInvoicePaymentUseCase

    companion object {

        private const val DELTA: Double = 0.0
        private const val DEFAULT_ZERO: Double = 0.0

        private const val SUBTOTAL: Double = 10000.0
        private const val EXPECTED_IVA: Double = SUBTOTAL * Constants.IVA
        private const val EXPECTED_IVA_WITHHOLDING: Double = SUBTOTAL * Constants.IVA_WITHHOLDING
        private const val EXPECTED_ISR_WITHHOLDING: Double = SUBTOTAL * Constants.ISR_WITHHOLDING
        private const val EXPECTED_TOTAL: Double =
            SUBTOTAL + EXPECTED_IVA - EXPECTED_IVA_WITHHOLDING - EXPECTED_ISR_WITHHOLDING
    }

    @Before
    fun setUp() {
        sut = GenerateInvoicePaymentUseCase()
    }

    @Test
    fun calculateIva_returnsSubtotalMultipliedByIva() {
        // Arrange
        // Act
        val result = sut.calculateIva(SUBTOTAL)
        // Assert
        assertEquals(result, EXPECTED_IVA, DELTA)
    }

    @Test
    fun calculateIvaWithholding_returnsSubtotalMultipliedByIvaWithholding() {
        // Arrange
        // Act
        val result = sut.calculateIvaWithholding(SUBTOTAL)
        // Assert
        assertEquals(result, EXPECTED_IVA_WITHHOLDING, DELTA)
    }

    @Test
    fun calculateIsrWithholding_returnsSubtotalMultipliedByIsrWithholding() {
        // Arrange
        // Act
        val result = sut.calculateIsrWithholding(SUBTOTAL)
        // Assert
        assertEquals(result, EXPECTED_ISR_WITHHOLDING, DELTA)
    }

    @Test
    fun calculateTotal_returnsCorrectTotal() {
        // Arrange
        // Act
        val result = sut.calculateTotal(
            SUBTOTAL,
            EXPECTED_IVA,
            EXPECTED_IVA_WITHHOLDING,
            EXPECTED_ISR_WITHHOLDING
        )
        // Assert
        assertEquals(result, EXPECTED_TOTAL, DELTA)
    }

    @Test
    fun generatePayment_moralReceiverPerson_returnsInvoicePayment() {
        // Arrange
        val expectedPayment = getExpectedPayment(EXPECTED_IVA_WITHHOLDING, EXPECTED_ISR_WITHHOLDING)
        // Act
        val result = sut.generatePayment(SUBTOTAL, Constants.MORAL_PERSON)
        // Assert
        assertEquals(result.subtotal, expectedPayment.subtotal, DELTA)
        assertEquals(result.iva, expectedPayment.iva, DELTA)
        assertEquals(result.ivaWithholding, expectedPayment.ivaWithholding, DELTA)
        assertEquals(result.isrWithholding, expectedPayment.isrWithholding, DELTA)
        assertEquals(result.total, expectedPayment.total, DELTA)
    }

    @Test
    fun generatePayment_physicalReceiverPerson_returnsInvoicePaymentWithDefaultWithholdings() {
        // Arrange
        val expectedPayment = getExpectedPayment(DEFAULT_ZERO, DEFAULT_ZERO)
        // Act
        val result = sut.generatePayment(SUBTOTAL, Constants.PHYSICAL_PERSON)
        // Assert
        assertEquals(result.subtotal, expectedPayment.subtotal, DELTA)
        assertEquals(result.iva, expectedPayment.iva, DELTA)
        assertEquals(result.ivaWithholding, expectedPayment.ivaWithholding, DELTA)
        assertEquals(result.isrWithholding, expectedPayment.isrWithholding, DELTA)
        assertEquals(result.total, expectedPayment.total, DELTA)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun getExpectedPayment(ivaWithholding: Double, isrWithholding: Double): InvoicePayment {
        return InvoicePayment(
            subtotal = SUBTOTAL,
            iva = EXPECTED_IVA,
            ivaWithholding = ivaWithholding,
            isrWithholding = isrWithholding,
            total = SUBTOTAL + EXPECTED_IVA - ivaWithholding - isrWithholding
        )
    }
}