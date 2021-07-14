package com.ivettevaldez.saturnus.screens.invoices.form.main

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import android.os.Handler
import android.view.View
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.invoices.Invoice
import com.ivettevaldez.saturnus.invoices.InvoiceDao
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceFormChangeFragmentEvent
import com.ivettevaldez.saturnus.screens.invoices.form.payment.InvoiceFormPaymentFragmentEvent
import com.ivettevaldez.saturnus.testdata.InvoiceTestData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class InvoiceFormMainControllerTest {

    private lateinit var sut: InvoiceFormMainController

    @Mock
    private lateinit var contextMock: Context

    @Mock
    private lateinit var viewMock: View

    @Mock
    private lateinit var viewMvcMock: IInvoiceFormMainViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var fragmentsEventBusMock: FragmentsEventBus

    @Mock
    private lateinit var dialogsEventBusMock: DialogsEventBus

    @Mock
    private lateinit var dialogsManagerMock: DialogsManager

    @Mock
    private lateinit var messagesHelperMock: MessagesHelper

    @Mock
    private lateinit var uiHandlerMock: Handler

    @Mock
    private lateinit var invoiceDaoMock: InvoiceDao

    @Mock
    private lateinit var invoiceFormChangeFragmentEventMock: InvoiceFormChangeFragmentEvent

    @Mock
    private lateinit var invoiceFormPaymentFragmentEventMock: InvoiceFormPaymentFragmentEvent

    @Mock
    private lateinit var promptDialogEventMock: PromptDialogEvent

    private val fakeInvoice: Invoice = InvoiceTestData.getInvoice()

    companion object {

        private const val ISSUING_RFC: String = "issuingRfc"
        private const val FOLIO: String = "folio"

        private const val TITLE_NEW_INVOICE: String = "Nueva factura"
        private const val TITLE_EDITING: String = "Editando…"
        private const val TITLE_ANY_STRING: String = "fakeString"

        private const val ID_NEW_INVOICE: Int = R.string.invoices_new
        private const val ID_EDITING_INVOICE: Int = R.string.action_editing
        private const val ID_SAVED_CHANGES_MESSAGE: Int = R.string.message_saved
    }

    @Before
    fun setUp() {
        sut = InvoiceFormMainController(
            contextMock,
            screensNavigatorMock,
            fragmentsEventBusMock,
            dialogsEventBusMock,
            dialogsManagerMock,
            messagesHelperMock,
            uiHandlerMock,
            invoiceDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun findInvoice_queryTheDatabaseAndSetInvoiceAndIssuingRfcIfInvoiceIsFound() {
        // Arrange
        `when`(invoiceDaoMock.findByFolio(FOLIO)).thenReturn(fakeInvoice)
        sut.folio = FOLIO
        // Act
        sut.findInvoiceAndIssuingRfc()
        // Assert
        verify(invoiceDaoMock).findByFolio(FOLIO)
        assert(sut.issuingRfc != null)
    }

    @Test
    fun bindArguments_newInvoice_invoiceIsNull() {
        // Arrange
        // Act
        newInvoice()
        // Assert
        assertTrue(sut.newInvoice)
        assertFalse(sut.editingInvoice)
        assert(sut.invoice == null)
    }

    @Test
    fun bindArguments_editingInvoice_invoiceAndIssuingRfcAreNotNull() {
        // Arrange
        // Act
        editingInvoice()
        // Assert
        assertFalse(sut.newInvoice)
        assertTrue(sut.editingInvoice)
        assert(sut.invoice != null)
        assert(sut.issuingRfc != null)
    }

    @Test
    fun getToolbarTitle_newInvoice_returnsNewInvoiceString() {
        // Arrange
        newInvoice()
        returnToolbarTitle()
        // Act
        val result = sut.getToolbarTitle()
        // Assert
        assertEquals(result, TITLE_NEW_INVOICE)
    }

    @Test
    fun getToolbarTitle_editingInvoice_returnsEditingInvoiceString() {
        // Arrange
        editingInvoice()
        returnToolbarTitle()
        // Act
        val result = sut.getToolbarTitle()
        // Assert
        assertEquals(result, TITLE_EDITING)
    }

    @Test
    fun setToolbarTitle_toolbarTitleIsSet() {
        // Arrange
        returnAnyTitle()
        // Act
        sut.setToolbarTitle()
        // Assert
        verify(viewMvcMock).setToolbarTitle(any())
    }

    @Test
    fun onStart_stepperInitialized() {
        // Arrange
        // Act
        sut.initStepper()
        // Assert
        verify(viewMvcMock).initStepper(anyOrNull(), anyOrNull())
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).registerListener(sut)
        verify(fragmentsEventBusMock).registerListener(sut)
        verify(dialogsEventBusMock).registerListener(sut)
    }

    @Test
    fun onStop_listenersUnregistered() {
        // Arrange
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
        verify(fragmentsEventBusMock).unregisterListener(sut)
        verify(dialogsEventBusMock).unregisterListener(sut)
    }

    @Test
    fun onNavigateUpClicked_hasFormChanges_showsExitWithoutSavingChangesConfirmationDialog() {
        // Arrange
        sut.hasFormChanges = true
        // Act
        sut.onNavigateUpClicked()
        // Assert
        verify(dialogsManagerMock).showExitWithoutSavingChangesConfirmation(null)
    }

    @Test
    fun onNavigateUpClicked_hasNotFormChanges_navigatesUp() {
        // Arrange
        sut.hasFormChanges = false
        // Act
        sut.onNavigateUpClicked()
        // Assert
        verify(screensNavigatorMock).navigateUp()
    }

    @Test
    fun onFragmentEvent_eventIsInvoiceFormChangeFragmentEvent_hasFormChangesSetToTrue() {
        // Arrange
        // Act
        sut.onFragmentEvent(invoiceFormChangeFragmentEventMock)
        // Assert
        assertTrue(sut.hasFormChanges)
    }

    @Test
    fun onFragmentEvent_eventIsInvoiceFormPaymentFragmentEvent_invoiceIsSet() {
        // Arrange
        setInvoiceFormPaymentFragmentEvent()
        // Act
        sut.onFragmentEvent(invoiceFormPaymentFragmentEventMock)
        // Assert
        assertTrue(sut.invoice != null)
    }

    @Test
    fun onDialogEvent_positiveButtonOfDialogClicked_navigatesUp() {
        // Arrange
        positiveButtonOfDialogClicked()
        // Act
        sut.onDialogEvent(promptDialogEventMock)
        // Assert
        verify(screensNavigatorMock).navigateUp()
    }

    @Test
    fun onDialogEvent_negativeButtonOfDialogClicked_doesNotNavigate() {
        // Arrange
        negativeButtonOfDialogClicked()
        // Act
        sut.onDialogEvent(promptDialogEventMock)
        // Assert
        verifyNoMoreInteractions(screensNavigatorMock)
    }

    @Test
    fun onCompletedSteps_newInvoice_invoiceIsSaved() {
        // Arrange
        newInvoiceChanges()
        // Act
        sut.onCompletedSteps()
        // Assert
        verify(invoiceDaoMock).save(any())
    }

    @Test
    fun onCompletedSteps_editingInvoice_invoiceIsSaved() {
        // Arrange
        editingInvoice()
        // Act
        sut.onCompletedSteps()
        // Assert
        verify(invoiceDaoMock).save(any())
    }

    @Test
    fun onCompletedSteps_successMessageIsShownAndNavigatesAfterShowMessageDelay() {
        // Arrange
        newInvoiceChanges()
        returnRootView()
        delayPassed()
        // Act
        sut.onCompletedSteps()
        // Assert
        verify(messagesHelperMock).showShortMessage(any(), eq(ID_SAVED_CHANGES_MESSAGE))
        verify(uiHandlerMock).postDelayed(any(), eq(Constants.SHOW_MESSAGE_DELAY))
        verify(screensNavigatorMock).navigateUp()
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun newInvoice() {
        sut.bindArguments(ISSUING_RFC, null)
    }

    private fun editingInvoice() {
        `when`(invoiceDaoMock.findByFolio(FOLIO)).thenReturn(fakeInvoice)
        sut.bindArguments(null, FOLIO)
    }

    private fun returnToolbarTitle() {
        when {
            sut.newInvoice -> {
                `when`(contextMock.getString(ID_NEW_INVOICE)).thenReturn(TITLE_NEW_INVOICE)
            }
            sut.editingInvoice -> {
                `when`(contextMock.getString(ID_EDITING_INVOICE)).thenReturn(TITLE_EDITING)
            }
            else -> {
                throw RuntimeException("Ain't new invoice neither editing invoice")
            }
        }
    }

    private fun returnAnyTitle() {
        `when`(contextMock.getString(any())).thenReturn(TITLE_ANY_STRING)
    }

    private fun returnRootView() {
        `when`(viewMvcMock.getRootView()).thenReturn(viewMock)
    }

    private fun newInvoiceChanges() {
        newInvoice()
        setInvoiceFormPaymentFragmentEvent()
        sut.onFragmentEvent(invoiceFormPaymentFragmentEventMock)
    }

    private fun setInvoiceFormPaymentFragmentEvent() {
        `when`(invoiceFormPaymentFragmentEventMock.invoice).thenReturn(fakeInvoice)
    }

    private fun positiveButtonOfDialogClicked() {
        `when`(promptDialogEventMock.clickedButton).thenReturn(PromptDialogEvent.Button.POSITIVE)
    }

    private fun negativeButtonOfDialogClicked() {
        `when`(promptDialogEventMock.clickedButton).thenReturn(PromptDialogEvent.Button.NEGATIVE)
    }

    private fun delayPassed() {
        `when`(
            uiHandlerMock.postDelayed(any(), any())
        ).doAnswer {
            it.getArgument<Runnable>(0).run()
            null
        }
    }
}