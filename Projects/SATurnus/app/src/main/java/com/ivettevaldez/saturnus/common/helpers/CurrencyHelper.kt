package com.ivettevaldez.saturnus.common.helpers

import java.text.NumberFormat

object CurrencyHelper {

    fun CharSequence.toCurrency(): String {
        return this.toDoubleValue().toCurrency()
    }

    fun Double.toCurrency(): String {
        return NumberFormat.getCurrencyInstance().format((this))
    }

    fun CharSequence.toDoubleValue(): Double {
        return this.clean().toDouble() / 100
    }

    private fun CharSequence.clean(): String {
        return this.replace("""[$,.]""".toRegex(), "")
    }
}