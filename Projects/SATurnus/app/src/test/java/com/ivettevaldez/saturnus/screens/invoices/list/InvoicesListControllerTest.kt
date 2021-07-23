package com.ivettevaldez.saturnus.screens.invoices.list

import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.testdata.InvoiceTestData
import com.ivettevaldez.saturnus.testdata.PeopleTestData
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class InvoicesListControllerTest {

    private lateinit var sut: InvoicesListController

    @Mock
    private lateinit var viewMvcMock: IInvoicesListViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var dialogsManagerMock: DialogsManager

    @Mock
    private lateinit var personDaoMock: PersonDao

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    private val expectedInvoices: List<Invoice> = InvoiceTestData.getInvoicesList()
    private val expectedPerson: Person = PeopleTestData.getPhysicalPerson()
    private val expectedReceivers: List<Person> = PeopleTestData.getPeople()
    private val rfc: String = expectedPerson.rfc

    companion object {

        private const val FOLIO: String = "folio"
    }

    @Before
    fun setUp() {
        sut = InvoicesListController(
            screensNavigatorMock,
            dialogsManagerMock,
            personDaoMock,
            invoiceDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun bindRfc_rfcIsNotNull() {
        // Arrange
        // Act
        sut.bindRfc(rfc)
        // Assert
        assertNotNull(sut.rfc)
    }

    @Test
    fun setToolbarTitle_personNameIsSetAsToolbarTitle() {
        // Arrange
        sut.bindRfc(rfc)
        foundPersonByRfc()
        // Act
        sut.setToolbarTitle()
        // Assert
        verify(personDaoMock).findByRfc(rfc)
        verify(viewMvcMock).setToolbarTitle(expectedPerson.name)
    }

    @Test
    fun onStart_invoicesAreBoundToView() {
        // Arrange
        sut.bindRfc(rfc)
        foundInvoicesByIssuingRfc()
        // Act
        sut.onStart()
        // Assert
        verify(invoiceDaoMock).findAllByIssuingRfc(rfc)
        viewMvcMock.inOrder {
            verify().showProgressIndicator()
            verify().bindInvoices(expectedInvoices)
            verify().hideProgressIndicator()
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        sut.bindRfc(rfc)
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
    fun onNavigateUpClicked_navigatesUp() {
        // Arrange
        // Act
        sut.onNavigateUpClicked()
        // Assert
        verify(screensNavigatorMock).navigateUp()
    }

    @Test
    fun onAddNewInvoiceClicked_thereAreReceiversInDb_navigatesToInvoiceForm() {
        // Arrange
        sut.bindRfc(rfc)
        notEmptyReceiversList()
        // Act
        sut.onAddNewInvoiceClicked()
        // Assert
        verify(screensNavigatorMock).toInvoiceForm(issuingRfc = rfc)
    }

    @Test
    fun onAddNewInvoiceClicked_thereAreNoReceiversInDb_showsErrorDialog() {
        // Arrange
        sut.bindRfc(rfc)
        emptyReceiversList()
        // Act
        sut.onAddNewInvoiceClicked()
        // Assert
        verify(dialogsManagerMock).showMissingReceiversError(null)
    }

    @Test
    fun onDetailsClicked_navigatesToInvoiceDetailsScreen() {
        // Arrange
        // Act
        sut.onDetailsClicked(FOLIO)
        // Assert
        verify(screensNavigatorMock).toInvoiceDetails(FOLIO)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun foundPersonByRfc() {
        `when`(personDaoMock.findByRfc(rfc)).thenReturn(expectedPerson)
    }

    private fun foundInvoicesByIssuingRfc() {
        `when`(invoiceDaoMock.findAllByIssuingRfc(rfc)).thenReturn(expectedInvoices)
    }

    private fun emptyReceiversList() {
        `when`(personDaoMock.findAllReceivers()).thenReturn(listOf())
    }

    private fun notEmptyReceiversList() {
        `when`(personDaoMock.findAllReceivers()).thenReturn(expectedReceivers)
    }
}