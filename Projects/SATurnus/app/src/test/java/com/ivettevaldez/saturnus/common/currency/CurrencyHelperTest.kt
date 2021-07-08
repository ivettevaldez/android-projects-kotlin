package com.ivettevaldez.saturnus.common.currency

import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.clean
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toCurrency
import com.ivettevaldez.saturnus.common.currency.CurrencyHelper.toDoubleValue
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CurrencyHelperTest {

    private lateinit var mxCurrencyRegex: Regex
    private lateinit var positiveOrNegativeNumberRegex: Regex

    companion object {

        private const val MX_CURRENCY_PATTERN = """^-?MX\$(\d{1,3}(,\d{3})*|(\d+))(\.\d{2})?$"""
        private const val POSITIVE_OR_NEGATIVE_NUMBER_PATTERN = """^-?\d+$"""

        private const val ZERO_DOUBLE: Double = 0.0
        private const val POSITIVE_NUMBER_DOUBLE: Double = 100000.99
        private const val NEGATIVE_NUMBER_DOUBLE: Double = -100000.99

        private const val ZERO_STRING: String = "0"
        private const val POSITIVE_NUMBER_STRING: String = "10000099"
        private const val NEGATIVE_NUMBER_STRING: String = "-10000099"

        private const val ZERO_FORMATTED_STRING: String = "$0.00"
        private const val POSITIVE_NUMBER_FORMATTED_STRING: String = "$100,000.99"
        private const val NEGATIVE_NUMBER_FORMATTED_STRING: String = "-$100,000.99"
    }

    @Before
    fun setUp() {
        mxCurrencyRegex = MX_CURRENCY_PATTERN.toRegex()
        positiveOrNegativeNumberRegex = POSITIVE_OR_NEGATIVE_NUMBER_PATTERN.toRegex()
    }

    @Test
    fun doubleToCurrency_numberZero_returnsStringWithCorrectCurrencyFormat() {
        // Arrange
        // Act
        val result = ZERO_DOUBLE.toCurrency()
        // Assert
        assertTrue(mxCurrencyRegex.matches(result))
    }

    @Test
    fun doubleToCurrency_positiveNumber_returnsStringWithCorrectCurrencyFormat() {
        // Arrange
        // Act
        val result = POSITIVE_NUMBER_DOUBLE.toCurrency()
        // Assert
        assertTrue(mxCurrencyRegex.matches(result))
    }

    @Test
    fun doubleToCurrency_negativeNumber_returnsStringWithCorrectCurrencyFormat() {
        // Arrange
        // Act
        val result = NEGATIVE_NUMBER_DOUBLE.toCurrency()
        // Assert
        assertTrue(mxCurrencyRegex.matches(result))
    }

    @Test
    fun charSequenceToCurrency_stringWithNumberZero_returnsStringWithCorrectCurrencyFormat() {
        // Arrange
        // Act
        val result = ZERO_STRING.toCurrency()
        // Assert
        assertTrue(mxCurrencyRegex.matches(result))
    }

    @Test
    fun charSequenceToCurrency_stringWithPositiveNumber_returnsStringWithCorrectCurrencyFormat() {
        // Arrange
        // Act
        val result = POSITIVE_NUMBER_STRING.toCurrency()
        // Assert
        assertTrue(mxCurrencyRegex.matches(result))
    }

    @Test
    fun charSequenceToCurrency_stringWithNegativeNumber_returnsStringWithCorrectCurrencyFormat() {
        // Arrange
        // Act
        val result = NEGATIVE_NUMBER_STRING.toCurrency()
        // Assert
        assertTrue(mxCurrencyRegex.matches(result))
    }

    @Test
    fun charSequenceToDoubleValue_stringWithHumberZero_returnsValidDouble() {
        // Arrange
        // Act
        val result = ZERO_FORMATTED_STRING.toDoubleValue()
        // Assert
        assertTrue(result == ZERO_DOUBLE)
    }

    @Test
    fun charSequenceToDoubleValue_stringWithPositiveNumber_returnsValidDouble() {
        // Arrange
        // Act
        val result = POSITIVE_NUMBER_FORMATTED_STRING.toDoubleValue()
        // Assert
        assertTrue(result == POSITIVE_NUMBER_DOUBLE)
    }

    @Test
    fun charSequenceToDoubleValue_stringWithNegativeNumber_returnsValidDouble() {
        // Arrange
        // Act
        val result = NEGATIVE_NUMBER_FORMATTED_STRING.toDoubleValue()
        // Assert
        assertTrue(result == NEGATIVE_NUMBER_DOUBLE)
    }

    @Test
    fun clean_stringWithNumberZero_returnsStringWithoutSymbols() {
        // Arrange
        // Act
        val result = ZERO_FORMATTED_STRING.clean()
        // Assert
        assertTrue(positiveOrNegativeNumberRegex.matches(result))
    }

    @Test
    fun clean_stringWithPositiveNumber_returnsStringWithoutSymbols() {
        // Arrange
        // Act
        val result = POSITIVE_NUMBER_FORMATTED_STRING.clean()
        // Assert
        assertTrue(positiveOrNegativeNumberRegex.matches(result))
    }

    @Test
    fun clean_stringWithNegativeNumber_returnsStringWithNegativeNumber() {
        // Arrange
        // Act
        val result = NEGATIVE_NUMBER_FORMATTED_STRING.clean()
        // Assert
        assertTrue(positiveOrNegativeNumberRegex.matches(result))
    }
}