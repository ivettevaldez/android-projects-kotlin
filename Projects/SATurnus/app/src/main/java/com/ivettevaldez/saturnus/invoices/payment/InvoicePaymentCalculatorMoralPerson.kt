package com.ivettevaldez.saturnus.invoices.payment

class InvoicePaymentCalculatorMoralPerson : InvoicePaymentCalculator() {

    companion object {

        private const val ISR_WITHHOLDING = 0.1
        private const val IVA_WITHHOLDING = 0.106667
    }

    override fun calculatePayment(subtotal: Double): InvoicePayment {
        val isrWithholding = calculateIsrWithholding(subtotal)
        val ivaWithholding = calculateIvaWithholding(subtotal)
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

    private fun calculateIsrWithholding(subtotal: Double): Double {
        return subtotal * ISR_WITHHOLDING
    }

    private fun calculateIvaWithholding(subtotal: Double): Double {
        return subtotal * IVA_WITHHOLDING
    }
}