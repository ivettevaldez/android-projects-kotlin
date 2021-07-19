package com.ivettevaldez.saturnus.invoices.payment

import com.ivettevaldez.saturnus.common.Constants

open class GenerateInvoicePaymentUseCase {

    open fun generatePayment(subtotal: Double, receiverPersonType: String): InvoicePayment {
        var isrWithholding = 0.0
        var ivaWithholding = 0.0

        if (receiverPersonType == Constants.MORAL_PERSON) {
            ivaWithholding = calculateIvaWithholding(subtotal)
            isrWithholding = calculateIsrWithholding(subtotal)
        }

        val iva = calculateIva(subtotal)

        return InvoicePayment(
            subtotal = subtotal,
            iva = iva,
            ivaWithholding = ivaWithholding,
            isrWithholding = isrWithholding,
            total = calculateTotal(subtotal, iva, ivaWithholding, isrWithholding)
        )
    }

    fun calculateIva(subtotal: Double): Double {
        return subtotal * Constants.IVA
    }

    fun calculateIvaWithholding(subtotal: Double): Double {
        return subtotal * Constants.IVA_WITHHOLDING
    }

    fun calculateIsrWithholding(subtotal: Double): Double {
        return subtotal * Constants.ISR_WITHHOLDING
    }

    fun calculateTotal(
        subtotal: Double,
        iva: Double,
        ivaWithholding: Double,
        isrWithholding: Double
    ): Double {
        return subtotal + iva - ivaWithholding - isrWithholding
    }
}