package com.ivettevaldez.saturnus.invoices.payment.calculator

import com.ivettevaldez.saturnus.invoices.payment.InvoicePayment

abstract class InvoicePaymentCalculator {

    companion object {

        private const val IVA = 0.16
    }

    abstract fun calculatePayment(subtotal: Double): InvoicePayment

    protected fun calculateIva(subtotal: Double): Double {
        return subtotal * IVA
    }

    protected fun calculateTotal(
        subtotal: Double,
        iva: Double,
        ivaWithholding: Double,
        isrWithholding: Double
    ): Double {
        return subtotal + iva - ivaWithholding - isrWithholding
    }
}