package com.ivettevaldez.saturnus.screens.common.dialogs.info

import android.app.Dialog
import android.view.View
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class InfoControllerTest {

    private lateinit var sut: InfoController

    @Mock
    private lateinit var dialogMock: Dialog

    @Mock
    private lateinit var viewMvcMock: IInfoViewMvc

    @Mock
    private lateinit var viewMock: View

    companion object {

        private const val TITLE: String = "title"
        private const val MESSAGE: String = "message"
        private const val BUTTON_CAPTION: String = "caption"
    }

    @Before
    fun setUp() {
        sut = InfoController()
    }

    @Test
    fun bindDialogAndView_contentViewIsSetToDialog() {
        // Arrange
        // Act
        bindDialogAndView()
        // Assert
        assertEquals(sut.viewMvc, viewMvcMock)
        assertEquals(sut.dialog, dialogMock)
        verify(dialogMock).setContentView(viewMock)
    }

    @Test
    fun bindArguments_argumentsAreBound() {
        // Arrange
        // Act
        bindArguments()
        // Assert
        assertEquals(sut.title, TITLE)
        assertEquals(sut.message, MESSAGE)
        assertEquals(sut.positiveCaption, BUTTON_CAPTION)
    }

    @Test
    fun onStart_dataIsBoundToView() {
        // Arrange
        bindDialogAndView()
        bindArguments()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).setTitle(TITLE)
        verify(viewMvcMock).setMessage(MESSAGE)
        verify(viewMvcMock).setPositiveButtonCaption(BUTTON_CAPTION)
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        bindDialogAndView()
        bindArguments()
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
    fun onButtonClick_dialogIsDismissed() {
        // Arrange
        bindDialogAndView()
        // Act
        sut.onPositiveButtonClicked()
        // Assert
        verify(dialogMock).dismiss()
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
        sut.bindArguments(TITLE, MESSAGE, BUTTON_CAPTION)
    }
}