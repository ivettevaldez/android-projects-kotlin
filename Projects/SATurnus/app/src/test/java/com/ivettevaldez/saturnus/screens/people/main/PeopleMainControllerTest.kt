package com.ivettevaldez.saturnus.screens.people.main

import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class PeopleMainControllerTest {

    private lateinit var sut: PeopleMainController

    @Mock
    private lateinit var viewMvcMock: IPeopleMainViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var peopleMainPagerAdapterMock: PeopleMainPagerAdapter

    @Before
    fun setUp() {
        sut = PeopleMainController(screensNavigatorMock)

        sut.bindView(viewMvcMock)
    }

    @Test
    fun initViewPager_initializesViewPager() {
        // Arrange
        // Act
        sut.initViewPager(peopleMainPagerAdapterMock)
        // Assert
        verify(viewMvcMock).setViewPager(
            peopleMainPagerAdapterMock,
            PeopleMainPagerAdapter.TAB_CLIENT_TYPE_ISSUING
        )
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
    fun onAddNewPersonClicked_navigatesToPersonFormScreenPassingNullRfc() {
        // Arrange
        // Act
        sut.onAddNewPersonClicked()
        // Assert
        verify(screensNavigatorMock).toPersonForm(null)
    }

    @Test
    fun onNavigateUpClicked_navigatesUp() {
        // Arrange
        // Act
        sut.onNavigateUpClicked()
        // Assert
        verify(screensNavigatorMock).navigateUp()
    }
}