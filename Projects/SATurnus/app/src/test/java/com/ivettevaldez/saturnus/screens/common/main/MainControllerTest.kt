package com.ivettevaldez.saturnus.screens.common.main

/* ktlint-disable no-wildcard-imports */

import android.widget.FrameLayout
import com.ivettevaldez.saturnus.screens.common.navigation.NavDrawerViewMvcImpl
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class MainControllerTest {

    private lateinit var sut: MainController

    @Mock
    private lateinit var viewMvcMock: NavDrawerViewMvcImpl

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var frameLayoutMock: FrameLayout

    companion object {

        private const val COPYRIGHT: String = "copyright"
    }

    @Before
    fun setUp() {
        sut = MainController(screensNavigatorMock)
        sut.bindView(viewMvcMock)
    }

    @Test
    fun toSplash_nullSavedInstanceState_navigatesToSplashScreen() {
        // Arrange
        // Act
        sut.toSplash()
        // Assert
        verify(screensNavigatorMock).toSplash()
    }

    @Test
    fun bindCopyright_copyrightBoundToView() {
        // Arrange
        // Act
        sut.bindCopyright(COPYRIGHT)
        // Assert
        verify(viewMvcMock).bindCopyright(any())
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
    fun onStart_listenersUnregistered() {
        // Arrange
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
    }

    @Test
    fun getFragmentFrame_returnsFragmentFrame() {
        // Arrange
        `when`(viewMvcMock.getFragmentFrame()).thenReturn(frameLayoutMock)
        // Act
        val fragmentFrame = sut.getFragmentFrame()
        // Assert
        assertEquals(fragmentFrame, frameLayoutMock)
    }

    @Test
    fun isDrawerOpen_openedDrawer_returnsTrue() {
        // Arrange
        openedDrawer()
        // Act
        val result = sut.isDrawerOpen()
        // Assert
        assertTrue(result)
    }

    @Test
    fun isDrawerOpen_closedDrawer_returnsFalse() {
        // Arrange
        closedDrawer()
        // Act
        val result = sut.isDrawerOpen()
        // Assert
        assertFalse(result)
    }

    @Test
    fun openDrawer_opensDrawer() {
        // Arrange
        // Act
        sut.openDrawer()
        // Assert
        verify(viewMvcMock).openDrawer()
    }

    @Test
    fun closeDrawer_closesDrawer() {
        // Arrange
        // Act
        sut.closeDrawer()
        // Assert
        verify(viewMvcMock).closeDrawer()
    }

    @Test
    fun onPeopleClicked_navigatesToPeopleScreen() {
        // Arrange
        // Act
        sut.onPeopleClicked()
        // Assert
        verify(screensNavigatorMock).toPeople()
    }

    @Test
    fun onInvoicingClicked_navigatesToInvoiceIssuingPeople() {
        // Arrange
        // Act
        sut.onInvoicingClicked()
        // Assert
        verify(screensNavigatorMock).toInvoiceIssuingPeople()
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun openedDrawer() {
        `when`(viewMvcMock.isDrawerOpen()).thenReturn(true)
    }

    private fun closedDrawer() {
        `when`(viewMvcMock.isDrawerOpen()).thenReturn(false)
    }
}