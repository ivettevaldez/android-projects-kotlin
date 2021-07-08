package com.ivettevaldez.saturnus.common.currency

import java.text.NumberFormat

object CurrencyHelper {

    fun Double.toCurrency(): String {
        return NumberFormat.getCurrencyInstance().format((this))
    }

    fun CharSequence.toCurrency(): String {
        return this.toDoubleValue().toCurrency()
    }

    fun CharSequence.toDoubleValue(): Double {
        return this.clean().toDouble() / 100
    }

    fun CharSequence.clean(): String {
        return this.replace("""[$,.]""".toRegex(), "")
    }
}