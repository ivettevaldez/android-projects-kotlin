package com.ivettevaldez.saturnus.screens.common.dialogs.prompt

import android.app.Dialog
import android.view.View
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class PromptControllerTest {

    private lateinit var sut: PromptController

    @Mock
    private lateinit var dialogMock: Dialog

    @Mock
    private lateinit var viewMvcMock: IPromptViewMvc

    @Mock
    private lateinit var dialogsEventBusMock: DialogsEventBus

    @Mock
    private lateinit var viewMock: View

    @Captor
    private val eventCaptor: ArgumentCaptor<PromptDialogEvent> =
        ArgumentCaptor.forClass(PromptDialogEvent::class.java)

    companion object {

        private const val TITLE: String = "title"
        private const val MESSAGE: String = "message"
        private const val POSITIVE_CAPTION: String = "positiveCaption"
        private const val NEGATIVE_CAPTION: String = "negativeCaption"
    }

    @Before
    fun setUp() {
        sut = PromptController(dialogsEventBusMock)
    }

    @Test
    fun bindArguments_argumentsAreBound() {
        // Arrange
        // Act
        bindArguments()
        // Assert
        assertEquals(sut.title, TITLE)
        assertEquals(sut.message, MESSAGE)
        assertEquals(sut.positiveCaption, POSITIVE_CAPTION)
        assertEquals(sut.negativeCaption, NEGATIVE_CAPTION)
    }

    @Test
    fun bindDialogAndView_contentViewIsSetToDialog() {
        // Arrange
        // Act
        bindDialogAndView()
        // Assert
        assertEquals(sut.dialog, dialogMock)
        assertEquals(sut.viewMvc, viewMvcMock)
        verify(dialogMock).setContentView(viewMock)
    }

    @Test
    fun onStart_dataIsBoundToView() {
        // Arrange
        bindArguments()
        bindDialogAndView()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).setTitle(TITLE)
        verify(viewMvcMock).setMessage(MESSAGE)
        verify(viewMvcMock).setPositiveCaption(POSITIVE_CAPTION)
        verify(viewMvcMock).setNegativeCaption(NEGATIVE_CAPTION)
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        bindArguments()
        bindDialogAndView()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).registerListener(sut)
    }

    @Test
    fun onStop_listenersUnregistered() {
        // Arrange
        bindDialogAndView()
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
    }

    @Test
    fun onPositiveButtonClicked_dialogDismissesAndPromptDialogEventIsPosted() {
        // Arrange
        bindDialogAndView()
        // Act
        sut.onPositiveButtonClicked()
        // Assert
        verify(dialogMock).dismiss()
        verify(dialogsEventBusMock).postEvent(capture(eventCaptor))

        val clickedButton = eventCaptor.value.clickedButton
        assertEquals(clickedButton, PromptDialogEvent.Button.POSITIVE)
    }

    @Test
    fun onNegativeButtonClicked_dialogDismissesAndPromptDialogEventIsPosted() {
        // Arrange
        bindDialogAndView()
        // Act
        sut.onNegativeButtonClicked()
        // Assert
        verify(dialogMock).dismiss()
        verify(dialogsEventBusMock).postEvent(capture(eventCaptor))

        val clickedButton = eventCaptor.value.clickedButton
        assertEquals(clickedButton, PromptDialogEvent.Button.NEGATIVE)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun getRootView() {
        `when`(viewMvcMock.getRootView()).thenReturn(viewMock)
    }

    private fun bindDialogAndView() {
        getRootView()
        sut.bindDialogAndView(dialogMock, viewMvcMock)
    }

    private fun bindArguments() {
        sut.bindArguments(TITLE, MESSAGE, POSITIVE_CAPTION, NEGATIVE_CAPTION)
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}