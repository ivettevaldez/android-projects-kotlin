package com.ivettevaldez.saturnus.screens.invoices.details

import android.os.Handler
import android.view.View
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormChangeFragmentEvent
import com.ivettevaldez.saturnus.testdata.InvoiceTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class InvoiceDetailsControllerTest {

    private lateinit var sut: InvoiceDetailsController

    @Mock
    private lateinit var viewMvcMock: IInvoiceDetailsViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var dialogsEventBusMock: DialogsEventBus

    @Mock
    private lateinit var messagesHelperMock: MessagesHelper

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    @Mock
    private lateinit var viewMock: View

    @Mock
    private lateinit var uiHandlerMock: Handler

    @Mock
    private lateinit var promptDialogEventMock: PromptDialogEvent

    @Mock
    private lateinit var wrongEventMock: InvoiceFormChangeFragmentEvent

    private val expectedInvoice: Invoice = InvoiceTestData.getInvoice()
    private val folio: String = expectedInvoice.folio

    companion object {

        private const val DELETED_INVOICE_ID: Int = R.string.message_deleted_invoice
    }

    @Before
    fun setUp() {
        sut = InvoiceDetailsController(
            screensNavigatorMock,
            dialogsEventBusMock,
            messagesHelperMock,
            uiHandlerMock,
            invoiceDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun bindArguments_folioIsNotNull() {
        // Arrange
        // Act
        sut.bindArguments(folio)
        // Assert
        assertNotNull(sut.folio)
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).registerListener(sut)
        verify(dialogsEventBusMock).registerListener(sut)
    }

    @Test
    fun onStop_listenersUnregistered() {
        // Arrange
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
        verify(dialogsEventBusMock).unregisterListener(sut)
    }

    @Test
    fun onResume_editionMode_invoiceIsBoundToView() {
        // Arrange
        sut.bindArguments(folio)
        sut.editionMode = true
        foundInvoiceByFolio()
        // Act
        sut.onResume()
        // Assert
        verify(invoiceDaoMock).findByFolio(folio)
        viewMvcMock.inOrder {
            verify().showProgressIndicator()
            verify().bindInvoice(expectedInvoice)
            verify().hideProgressIndicator()
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun onResume_readOnlyMode_noInvoiceIsBoundToView() {
        // Arrange
        sut.editionMode = false
        // Act
        sut.onResume()
        // Assert
        verifyNoInteractions(viewMvcMock)
    }

    @Test
    fun onDialogEvent_eventIsNotPromptDialogEvent_nothingHappens() {
        // Arrange
        sut.bindArguments(folio)
        // Act
        sut.onDialogEvent(wrongEventMock)
        // Assert
        assert(wrongEventMock !is PromptDialogEvent)

        verifyNoInteractions(invoiceDaoMock)
        verifyNoInteractions(messagesHelperMock)
        verifyNoInteractions(uiHandlerMock)
        verifyNoInteractions(screensNavigatorMock)
    }

    @Test
    fun onDialogEvent_eventIsPromptDialogEventWithPositiveButtonClicked_invoiceIsDeleted() {
        // Arrange
        eventIsPromptDialogEventWithPositiveButtonClicked()
        // Act
        sut.onDialogEvent(promptDialogEventMock)
        // Assert
        assertEquals(promptDialogEventMock.clickedButton, PromptDialogEvent.Button.POSITIVE)

        val order = inOrder(invoiceDaoMock, messagesHelperMock, uiHandlerMock, screensNavigatorMock)
        order.verify(invoiceDaoMock).delete(folio)
        order.verify(messagesHelperMock).showShortMessage(viewMock, DELETED_INVOICE_ID)
        order.verify(uiHandlerMock).postDelayed(anyOrNull(), eq(Constants.SHOW_MESSAGE_DELAY))
        order.verify(screensNavigatorMock).navigateUp()
    }

    @Test
    fun onDialogEvent_eventIsPromptDialogEventWithNegativeButtonClicked_nothingHappens() {
        // Arrange
        eventIsPromptDialogEventWithNegativeButtonClicked()
        // Act
        sut.onDialogEvent(promptDialogEventMock)
        // Assert
        assertEquals(promptDialogEventMock.clickedButton, PromptDialogEvent.Button.NEGATIVE)

        verifyNoInteractions(invoiceDaoMock)
        verifyNoInteractions(messagesHelperMock)
        verifyNoInteractions(uiHandlerMock)
        verifyNoInteractions(screensNavigatorMock)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun foundInvoiceByFolio() {
        `when`(invoiceDaoMock.findByFolio(folio)).thenReturn(expectedInvoice)
    }

    private fun userClickedPositiveButtonFromPromptDialog() {
        `when`(promptDialogEventMock.clickedButton).thenReturn(PromptDialogEvent.Button.POSITIVE)
    }

    private fun userClickedNegativeButtonFromPromptDialog() {
        `when`(promptDialogEventMock.clickedButton).thenReturn(PromptDialogEvent.Button.NEGATIVE)
    }

    private fun getRootView() {
        `when`(viewMvcMock.getRootView()).thenReturn(viewMock)
    }

    private fun passedDelay() {
        `when`(uiHandlerMock.postDelayed(anyOrNull(), anyLong())).doAnswer {
            (it.arguments[0] as Runnable).run()
            null
        }
    }

    private fun eventIsPromptDialogEventWithPositiveButtonClicked() {
        sut.bindArguments(folio)
        userClickedPositiveButtonFromPromptDialog()
        passedDelay()
        getRootView()
    }

    private fun eventIsPromptDialogEventWithNegativeButtonClicked() {
        sut.bindArguments(folio)
        userClickedNegativeButtonFromPromptDialog()
    }
}