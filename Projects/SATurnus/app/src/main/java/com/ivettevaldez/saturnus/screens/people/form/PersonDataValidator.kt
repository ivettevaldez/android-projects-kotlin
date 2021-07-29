package com.ivettevaldez.saturnus.screens.people.form

object PersonDataValidator {

    const val NAME_MIN_LENGTH = 4
    const val RFC_MORAL_PERSON_LENGTH = 12
    const val RFC_PHYSICAL_PERSON_LENGTH = 13

    fun String.isValidName(): Boolean = this.length >= NAME_MIN_LENGTH

    // TODO: Add more validations to RFC format.
    fun String.isValidRfc(): Boolean {
        return (this.length == RFC_MORAL_PERSON_LENGTH ||
                this.length == RFC_PHYSICAL_PERSON_LENGTH) &&
                !this.contains(" ", true)
    }
}