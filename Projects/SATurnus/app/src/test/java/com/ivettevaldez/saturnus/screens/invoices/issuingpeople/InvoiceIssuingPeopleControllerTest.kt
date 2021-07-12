package com.ivettevaldez.saturnus.screens.invoices.issuingpeople

import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.testdata.PeopleTestData
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
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

    private val expectedPeople: List<Person> = PeopleTestData.getPeople()

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
    fun onStart_peopleSavedInDb_peopleBoundToView() {
        // Arrange
        peopleSavedInDb()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).bindPeople(expectedPeople)
    }

    @Test
    fun onStart_noPeopleSavedInDb_peopleNotBoundToView() {
        // Arrange
        noPeopleSavedInDb()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock, never()).bindPeople(any())
    }

    @Test
    fun onPersonClick_navigatesToInvoicesListScreen() {
        // Arrange
        // Act
        sut.onPersonClick(RFC)
        // Assert
        verify(screensNavigatorMock).toInvoicesList(RFC)
    }

    @Test
    fun bindPeople_beforeBindingPeople_progressIndicatorShown() {
        // Arrange
        peopleSavedInDb()
        // Act
        sut.bindPeople()
        // Assert
        verify(viewMvcMock).showProgressIndicator()
    }

    @Test
    fun bindPeople_afterBindingPeople_progressIndicatorHidden() {
        // Arrange
        peopleSavedInDb()
        // Act
        sut.bindPeople()
        // Assert
        verify(viewMvcMock).hideProgressIndicator()
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun peopleSavedInDb() {
        `when`(sut.getPeople()).thenReturn(expectedPeople)
    }

    private fun noPeopleSavedInDb() {
        `when`(sut.getPeople()).thenReturn(listOf())
    }
}