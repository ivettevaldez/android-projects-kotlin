package com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet

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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class PromptBottomSheetControllerTest {

    private lateinit var sut: PromptBottomSheetController

    @Mock
    private lateinit var dialogMock: Dialog

    @Mock
    private lateinit var viewMvcMock: IPromptBottomSheetViewMvc

    @Mock
    private lateinit var dialogsEventBusMock: DialogsEventBus

    @Mock
    private lateinit var viewMock: View

    @Captor
    private val eventCaptor: ArgumentCaptor<PromptBottomSheetDialogEvent> =
        ArgumentCaptor.forClass(PromptBottomSheetDialogEvent::class.java)

    companion object {

        private const val TITLE: String = "title"
        private const val OPTION_ONE_CAPTION: String = "optionOneCaption"
        private const val OPTION_TWO_CAPTION: String = "optionTwoCaption"
    }

    @Before
    fun setUp() {
        sut = PromptBottomSheetController(dialogsEventBusMock)
    }

    @Test
    fun bindArguments_argumentsAreBound() {
        // Arrange
        // Act
        bindArguments()
        // Assert
        assertEquals(sut.title, TITLE)
        assertEquals(sut.optionOneCaption, OPTION_ONE_CAPTION)
        assertEquals(sut.optionTwoCaption, OPTION_TWO_CAPTION)
    }

    @Test
    fun bindDialogAndView_contentIsSetToDialog() {
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
        verify(viewMvcMock).setOptionOneCaption(OPTION_ONE_CAPTION)
        verify(viewMvcMock).setOptionTwoCaption(OPTION_TWO_CAPTION)
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
    fun onOptionOneClicked_dialogDismissesAndPromptBottomSheetDialogEventWithCorrectDataIsPosted() {
        // Arrange
        bindDialogAndView()
        // Act
        sut.onOptionOneClicked()
        // Assert
        verify(dialogMock).dismiss()
        verify(dialogsEventBusMock).postEvent(capture(eventCaptor))

        val clickedButton = eventCaptor.value.clickedButton
        assertEquals(clickedButton, PromptBottomSheetDialogEvent.Button.OPTION_ONE)
    }

    @Test
    fun onOptionTwoClicked_dialogDismissesAndPromptBottomSheetDialogEventWithCorrectDataIsPosted() {
        // Arrange
        bindDialogAndView()
        // Act
        sut.onOptionTwoClicked()
        // Assert
        verify(dialogMock).dismiss()
        verify(dialogsEventBusMock).postEvent(capture(eventCaptor))

        val clickedButton = eventCaptor.value.clickedButton
        assertEquals(clickedButton, PromptBottomSheetDialogEvent.Button.OPTION_TWO)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun getRootView() {
        Mockito.`when`(viewMvcMock.getRootView()).thenReturn(viewMock)
    }

    private fun bindDialogAndView() {
        getRootView()
        sut.bindDialogAndView(dialogMock, viewMvcMock)
    }

    private fun bindArguments() {
        sut.bindArguments(TITLE, OPTION_ONE_CAPTION, OPTION_TWO_CAPTION)
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}