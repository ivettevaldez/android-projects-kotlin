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
class InfoDialogControllerTest {

    private lateinit var sut: InfoDialogController

    @Mock
    private lateinit var dialogMock: Dialog

    @Mock
    private lateinit var viewMvcMock: IInfoDialogViewMvc

    @Mock
    private lateinit var viewMock: View

    companion object {

        private const val TITLE: String = "title"
        private const val MESSAGE: String = "message"
        private const val BUTTON_CAPTION: String = "caption"
    }

    @Before
    fun setUp() {
        sut = InfoDialogController()
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
    fun bindData_dataIsBoundToView() {
        // Arrange
        bindDialogAndView()
        // Act
        sut.bindData(TITLE, MESSAGE, BUTTON_CAPTION)
        // Assert
        verify(viewMvcMock).setTitle(TITLE)
        verify(viewMvcMock).setMessage(MESSAGE)
        verify(viewMvcMock).setButtonCaption(BUTTON_CAPTION)
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
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
    fun onButtonClick_dialogIsDismissed() {
        // Arrange
        bindDialogAndView()
        // Act
        sut.onButtonClicked()
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
}