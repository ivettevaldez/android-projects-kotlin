package com.ivettevaldez.saturnus.invoices.payment

class InvoicePaymentCalculatorPhysicalPerson : InvoicePaymentCalculator() {

    override fun calculatePayment(subtotal: Double): InvoicePayment {
        val isrWithholding = 0.0
        val ivaWithholding = 0.0
        val iva = calculateIva(subtotal)
        val total = calculateTotal(subtotal, iva, ivaWithholding, isrWithholding)

        return InvoicePayment(
            subtotal = subtotal,
            iva = iva,
            ivaWithholding = ivaWithholding,
            isrWithholding = isrWithholding,
            total = total
        )
    }
}