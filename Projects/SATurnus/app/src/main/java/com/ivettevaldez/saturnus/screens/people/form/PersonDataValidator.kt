package com.ivettevaldez.saturnus.screens.people.form

object PersonDataValidator {

    const val NAME_MIN_LENGTH = 4
    const val RFC_MORAL_PERSON_LENGTH = 12
    const val RFC_PHYSICAL_PERSON_LENGTH = 13

    fun String.isValidName(): Boolean {
        return this.nameHasValidLength() &&
                !this.isOnlySymbols() &&
                !this.isOnlyDigits()
    }

    fun String.isValidRfc(): Boolean {
        return this.rfcHasValidLength() &&
                !this.containsBlankSpace() &&
                !this.isOnlySymbols() &&
                !this.isOnlyDigits()
    }

    private fun String.nameHasValidLength(): Boolean {
        return this.length >= NAME_MIN_LENGTH
    }

    private fun String.rfcHasValidLength(): Boolean {
        return this.length == RFC_MORAL_PERSON_LENGTH || this.length == RFC_PHYSICAL_PERSON_LENGTH
    }

    private fun String.containsBlankSpace(): Boolean {
        return this.contains(" ", true)
    }

    private fun String.isOnlySymbols(): Boolean {
        return !this.any { it.isLetterOrDigit() }
    }

    private fun String.isOnlyDigits(): Boolean {
        return this.all { it.isDigit() }
    }
}