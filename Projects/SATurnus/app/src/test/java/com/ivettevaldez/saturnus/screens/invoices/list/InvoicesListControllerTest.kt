package com.ivettevaldez.saturnus.screens.invoices.list

import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
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
    private lateinit var personDaoMock: PersonDao

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    private val expectedInvoices: List<Invoice> = InvoiceTestData.getInvoicesList()
    private val expectedPerson: Person = PeopleTestData.getPhysicalPerson()
    private val rfc: String = expectedPerson.rfc

    companion object {

        private const val FOLIO: String = "folio"
    }

    @Before
    fun setUp() {
        sut = InvoicesListController(
            screensNavigatorMock,
            personDaoMock,
            invoiceDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun bindArguments_rfcIsNotNull() {
        // Arrange
        // Act
        sut.bindArguments(rfc)
        // Assert
        assertNotNull(sut.rfc)
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
    fun onNavigateUpClicked_navigatesUp() {
        // Arrange
        // Act
        sut.onNavigateUpClicked()
        // Assert
        verify(screensNavigatorMock).navigateUp()
    }

    @Test
    fun onAddNewInvoiceClicked_navigatesToInvoiceForm() {
        // Arrange
        sut.bindArguments(rfc)
        // Act
        sut.onAddNewInvoiceClicked()
        // Assert
        verify(screensNavigatorMock).toInvoiceForm(issuingRfc = rfc)
    }

    @Test
    fun onDetailsClicked_navigatesToInvoiceDetailsScreen() {
        // Arrange
        // Act
        sut.onDetailsClicked(FOLIO)
        // Assert
        verify(screensNavigatorMock).toInvoiceDetails(FOLIO)
    }

    @Test
    fun setToolbarTitle_setsPersonNameAsToolbarTitle() {
        // Arrange
        sut.bindArguments(rfc)
        foundPersonByRfc()
        // Act
        sut.setToolbarTitle()
        // Assert
        verify(personDaoMock).findByRfc(rfc)
        verify(viewMvcMock).setToolbarTitle(expectedPerson.name)
    }

    @Test
    fun bindInvoices_invoicesBoundToView() {
        // Arrange
        sut.bindArguments(rfc)
        foundInvoicesByIssuingRfc()
        // Act
        sut.bindInvoices()
        // Assert
        verify(invoiceDaoMock).findAllByIssuingRfc(rfc)
        viewMvcMock.inOrder {
            verify().showProgressIndicator()
            verify().bindInvoices(expectedInvoices)
            verify().hideProgressIndicator()
            verifyNoMoreInteractions()
        }
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
}