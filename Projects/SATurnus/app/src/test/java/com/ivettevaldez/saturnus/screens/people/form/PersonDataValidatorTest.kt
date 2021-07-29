package com.ivettevaldez.saturnus.screens.people.form

import com.ivettevaldez.saturnus.screens.people.form.PersonDataValidator.isValidName
import com.ivettevaldez.saturnus.screens.people.form.PersonDataValidator.isValidRfc
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonDataValidatorTest {

    companion object {

        private const val EMPTY_STRING = " "
        private const val ONE_CHAR_STRING = "X"
        private const val EXCEEDED_LENGTH_STRING = "XXXXXXXXXXXXXXX"
        private const val ONLY_SYMBOLS_STRING = "±!@#$%^&*()_+"
        private const val ONLY_SYMBOLS_MORAL_PERSON_RFC = "±!@#$%^&*()_"
        private const val ONLY_SYMBOLS_PHYSICAL_PERSON_STRING = "±!@#$%^&*()_+"
        private const val ONLY_NUMBERS_STRING = "1234567890"
        private const val ONLY_NUMBERS_MORAL_PERSON_RFC = "123456789012"
        private const val ONLY_NUMBERS_PHYSICAL_PERSON_RFC = "1234567890123"
        private const val VALID_NAME = "Ivette Valdez"
        private const val VALID_MORAL_PERSON_RFC = "HR&120903317"
        private const val VALID_PHYSICAL_PERSON_RFC = "HR&1209033178"
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

//    @Test
//    fun isValidName_correctLengthButOnlySymbols_returnsFalse() {
//        // Arrange
//        // Act
//        val result = ONLY_SYMBOLS_STRING.isValidName()
//        // Assert
//        assertFalse(result)
//    }
//
//    @Test
//    fun isValidName_correctLengthButOnlyNumbers_returnsFalse() {
//        // Arrange
//        // Act
//        val result = ONLY_NUMBERS_STRING.isValidName()
//        // Assert
//        assertFalse(result)
//    }

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

//    @Test
//    fun isValidRfc_moralPersonLengthButOnlySymbols_returnsFalse() {
//        // Arrange
//        // Act
//        val result = ONLY_SYMBOLS_MORAL_PERSON_RFC.isValidRfc()
//        // Assert
//        assertFalse(result)
//    }
//
//    @Test
//    fun isValidRfc_moralPersonLengthButOnlyNumbers_returnsFalse() {
//        // Arrange
//        // Act
//        val result = ONLY_NUMBERS_MORAL_PERSON_RFC.isValidRfc()
//        // Assert
//        assertFalse(result)
//    }

    @Test
    fun isValidRfc_validMoralPersonRfc_returnsTrue() {
        // Arrange
        // Act
        val result = VALID_MORAL_PERSON_RFC.isValidRfc()
        // Assert
        assertTrue(result)
    }

//    @Test
//    fun isValidRfc_physicalPersonLengthButOnlySymbols_returnsFalse() {
//        // Arrange
//        // Act
//        val result = ONLY_SYMBOLS_PHYSICAL_PERSON_STRING.isValidRfc()
//        // Assert
//        assertFalse(result)
//    }
//
//    @Test
//    fun isValidRfc_physicalPersonLengthButOnlyNumbers_returnsFalse() {
//        // Arrange
//        // Act
//        val result = ONLY_NUMBERS_PHYSICAL_PERSON_RFC.isValidRfc()
//        // Assert
//        assertFalse(result)
//    }

    @Test
    fun isValidRfc_validPhysicalPersonRfc_returnsTrue() {
        // Arrange
        // Act
        val result = VALID_PHYSICAL_PERSON_RFC.isValidRfc()
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