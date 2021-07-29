package com.ivettevaldez.saturnus.screens.people.form

import com.ivettevaldez.saturnus.screens.people.form.PersonDataValidator.isValidName
import com.ivettevaldez.saturnus.screens.people.form.PersonDataValidator.isValidRfc
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonDataValidatorTest {

    companion object {

        private const val VALID_NAME = "Ivette Valdez"
        private const val EMPTY_STRING = " "
        private const val ONE_CHAR_STRING = "X"
        private const val ONLY_DIGITS_STRING = "1234567890"
        private const val ONLY_SYMBOLS_STRING = "±!@#$%^&*()_+"
        private const val EXCEEDED_LENGTH_STRING = "XXXXXXXXXXXXXXX"

        private const val VALID_RFC_MORAL_PERSON = "HR&120903317"
        private const val RFC_MORAL_PERSON_WITH_ONLY_DIGITS = "123456789012"
        private const val RFC_MORAL_PERSON_WITH_BLANK_SPACE = "HR&1 0903317"
        private const val RFC_MORAL_PERSON_WITH_ONLY_SYMBOLS = "±!@#$%^&*()_"

        private const val VALID_RFC_PHYSICAL_PERSON = "VAES000101XX0"
        private const val RFC_PHYSICAL_PERSON_WITH_ONLY_DIGITS = "1234567890123"
        private const val RFC_PHYSICAL_PERSON_WITH_BLANK_SPACE = "HR&1 09033178"
        private const val RFC_PHYSICAL_PERSON_WITH_ONLY_SYMBOLS = "±!@#$%^&*()_+"
    }

    @Test
    fun isValidName_emptyString_returnsFalse() {
        // Arrange
        // Act
        val result = EMPTY_STRING.isValidName()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidName_oneCharString_returnsFalse() {
        // Arrange
        // Act
        val result = ONE_CHAR_STRING.isValidName()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidName_minLengthMinusOneString_returnsFalse() {
        // Arrange
        val name = getNameWithMinLengthMinusOne()
        // Act
        val result = name.isValidName()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidName_correctLengthButContainsOnlySymbols_returnsFalse() {
        // Arrange
        // Act
        val result = ONLY_SYMBOLS_STRING.isValidName()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidName_correctLengthButContainsOnlyDigits_returnsFalse() {
        // Arrange
        // Act
        val result = ONLY_DIGITS_STRING.isValidName()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidName_validName_returnsTrue() {
        // Arrange
        // Act
        val result = VALID_NAME.isValidName()
        // Assert
        assertTrue(result)
    }

    @Test
    fun isValidRfc_emptyString_returnsFalse() {
        // Arrange
        // Act
        val result = EMPTY_STRING.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_oneCharString_returnsFalse() {
        // Arrange
        // Act
        val result = ONE_CHAR_STRING.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_minLengthMinusOneString_returnsFalse() {
        // Arrange
        val rfc = getRfcWithMinLengthMinusOne()
        // Act
        val result = rfc.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_moralPersonLengthButContainsOnlySymbols_returnsFalse() {
        // Arrange
        // Act
        val result = RFC_MORAL_PERSON_WITH_ONLY_SYMBOLS.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_moralPersonLengthButContainsOnlyDigits_returnsFalse() {
        // Arrange
        // Act
        val result = RFC_MORAL_PERSON_WITH_ONLY_DIGITS.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_validMoralPersonRfc_returnsTrue() {
        // Arrange
        // Act
        val result = VALID_RFC_MORAL_PERSON.isValidRfc()
        // Assert
        assertTrue(result)
    }

    @Test
    fun isValidRfc_physicalPersonLengthButContainsOnlySymbols_returnsFalse() {
        // Arrange
        // Act
        val result = RFC_PHYSICAL_PERSON_WITH_ONLY_SYMBOLS.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_physicalPersonLengthButContainsOnlyDigits_returnsFalse() {
        // Arrange
        // Act
        val result = RFC_PHYSICAL_PERSON_WITH_ONLY_DIGITS.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_validPhysicalPersonRfc_returnsTrue() {
        // Arrange
        // Act
        val result = VALID_RFC_PHYSICAL_PERSON.isValidRfc()
        // Assert
        assertTrue(result)
    }

    @Test
    fun isValidRfc_exceededLengthString_returnsFalse() {
        // Arrange
        // Act
        val result = EXCEEDED_LENGTH_STRING.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_moralPersonLengthButContainsBlankSpace_returnsFalse() {
        // Arrange
        // Act
        val result = RFC_MORAL_PERSON_WITH_BLANK_SPACE.isValidRfc()
        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidRfc_physicalPersonLengthButContainsBlankSpace_returnsFalse() {
        // Arrange
        // Act
        val result = RFC_PHYSICAL_PERSON_WITH_BLANK_SPACE.isValidRfc()
        // Assert
        assertFalse(result)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun getNameWithMinLengthMinusOne(): String {
        return ONE_CHAR_STRING.repeat(PersonDataValidator.NAME_MIN_LENGTH - 1)
    }

    private fun getRfcWithMinLengthMinusOne(): String {
        return ONE_CHAR_STRING.repeat(PersonDataValidator.RFC_MORAL_PERSON_LENGTH - 1)
    }
}