package com.ivettevaldez.saturnus.screens.invoices.issuingpeople

import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.testdata.PersonTestData
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class InvoiceIssuingPeopleControllerTest {

    private lateinit var sut: InvoiceIssuingPeopleController

    @Mock
    private lateinit var viewMvcMock: IInvoiceIssuingPeopleViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var personDaoMock: PersonDao

    private val expectedPeople: List<Person> = PersonTestData.getPeople()

    companion object {

        private const val RFC: String = "rfc"
    }

    @Before
    fun setUp() {
        sut = InvoiceIssuingPeopleController(screensNavigatorMock, personDaoMock)
        sut.bindView(viewMvcMock)
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).registerListener(sut)
    }

    @Test
    fun onStop_listenersUnregistered() {
        // Arrange
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
    }

    @Test
    fun onStart_progressIndicatorShown() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).showProgressIndicator()
    }

    @Test
    fun onStart_progressIndicatorHidden() {
        // Arrange
        peopleSavedInDb()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).hideProgressIndicator()
    }

    @Test
    fun onStart_peopleBoundToView() {
        // Arrange
        peopleSavedInDb()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).bindPeople(expectedPeople)
    }

    @Test
    fun onAddNewPersonClicked_navigatesToPersonFormScreen() {
        // Arrange
        // Act
        sut.onAddNewPersonClicked()
        // Assert
        verify(screensNavigatorMock).toPersonForm(null)
    }

    @Test
    fun onPersonClicked_navigatesToInvoicesListScreen() {
        // Arrange
        // Act
        sut.onPersonClicked(RFC)
        // Assert
        verify(screensNavigatorMock).toInvoicesList(RFC)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun peopleSavedInDb() {
        `when`(sut.getPeople()).thenReturn(expectedPeople)
    }
}