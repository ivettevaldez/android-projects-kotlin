package com.ivettevaldez.saturnus.common.dates

/* ktlint-disable no-wildcard-imports */

import com.ivettevaldez.saturnus.common.dates.DatesHelper.calendar
import com.ivettevaldez.saturnus.common.dates.DatesHelper.friendlyDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class DatesHelperTest {

    private lateinit var calendar: Calendar
    private lateinit var dateFormat: SimpleDateFormat

    companion object {

        private const val DAY_OF_MONTH: Int = 1
        private const val MONTH: Int = 0
        private const val YEAR: Int = 2000

        private const val WRONG_DAY_OF_MONTH: Int = 99
        private const val WRONG_MONTH: Int = 99
        private const val WRONG_YEAR: Int = 9999

        private const val VALID_DATE: String = "01/January/2000"
        private const val INVALID_DATE: String = "99/unknown/9999"
    }

    @Before
    fun setUp() {
        val dateProvider = DateProvider()
        calendar = dateProvider.getCalendar()
        dateFormat = dateProvider.getUserFriendlyFormat()
    }

    @Test
    fun calendarFromString_validDate_returnsCalendarWithGivenDate() {
        // Arrange
        calendar.set(YEAR, MONTH, DAY_OF_MONTH)
        val validYear = calendar.get(Calendar.YEAR)
        val validMonth = calendar.get(Calendar.MONTH)
        val validDay = calendar.get(Calendar.DAY_OF_MONTH)
        // Act
        val result = VALID_DATE.calendar()
        // Assert
        assertEquals(result.get(Calendar.YEAR), validYear)
        assertEquals(result.get(Calendar.MONTH), validMonth)
        assertEquals(result.get(Calendar.DAY_OF_MONTH), validDay)
    }

    @Test
    fun calendarFromString_invalidDate_returnsCalendarWithCurrentDate() {
        // Arrange
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        // Act
        val result = INVALID_DATE.calendar()
        // Assert
        assertEquals(result.get(Calendar.YEAR), currentYear)
        assertEquals(result.get(Calendar.MONTH), currentMonth)
        assertEquals(result.get(Calendar.DAY_OF_MONTH), currentDay)
    }

    @Test
    fun friendlyDateFromDate_success_returnsStringWithCorrectDateFormat() {
        // Arrange
        calendar.set(YEAR, MONTH, DAY_OF_MONTH)
        // Act
        val result = calendar.time.friendlyDate()
        // Assert
        assertEquals(result, VALID_DATE)
    }

    @Test
    fun friendlyDateFromInts_validParameters_returnsStringWithCorrectDateFormat() {
        // Arrange
        // Act
        val result = friendlyDate(YEAR, MONTH, DAY_OF_MONTH)
        // Assert
        assertEquals(result, VALID_DATE)
    }

    @Test
    fun friendlyDateFromInts_invalidParameters_returnsEmptyString() {
        // Arrange
        // Act
        val result = friendlyDate(WRONG_YEAR, WRONG_MONTH, WRONG_DAY_OF_MONTH)
        // Assert
        assertEquals(result, "")
    }
}