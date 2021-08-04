package com.ivettevaldez.saturnus.screens.common.dialogs.personselector

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.PersonSelectorBottomSheetDialog.PersonType
import com.ivettevaldez.saturnus.testdata.PersonTestData
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class PersonSelectorBottomSheetControllerTest {

    private lateinit var sut: PersonSelectorBottomSheetController

    @Mock
    private lateinit var bottomSheetDialogMock: BottomSheetDialog

    @Mock
    private lateinit var viewMvcMock: IPersonSelectorBottomSheetViewMvc

    @Mock
    private lateinit var viewMock: View

    @Mock
    private lateinit var contextMock: Context

    @Mock
    private lateinit var personDao: PersonDao

    @Mock
    private lateinit var listenerMock: IPersonSelectorBottomSheetViewMvc.Listener

    private val typeReceiver: PersonType = PersonType.RECEIVER
    private val receivers: List<Person> = PersonTestData.getPeople()

    companion object {

        private const val RECEIVERS_TITLE: String = "receiversTitle"
        private const val RFC: String = "rfc"
    }

    @Test
    fun bindDialogAndView_typeReceiver_contentViewIsSetToDialog() {
        // Arrange
        setReceiverPersonType()
        // Act
        bindDialogAndView()
        // Assert
        assertEquals(sut.dialog, bottomSheetDialogMock)
        assertEquals(sut.viewMvc, viewMvcMock)
        verify(bottomSheetDialogMock).setContentView(viewMock)
    }

    @Test
    fun onStart_typeReceiver_correctDataIsBoundToView() {
        // Arrange
        setReceiverPersonType()
        bindDialogAndView()
        getReceivers()
        getReceiversTitle()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).bindTitle(RECEIVERS_TITLE)
        verify(viewMvcMock).bindPeople(receivers)
    }

    @Test
    fun onStart_typeReceiver_listenersRegistered() {
        // Arrange
        setReceiverPersonType()
        bindDialogAndView()
        getReceivers()
        getReceiversTitle()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).registerListener(sut)
    }

    @Test
    fun onStop_typeReceiver_listenersUnregistered() {
        // Arrange
        setReceiverPersonType()
        bindDialogAndView()
        // Act
        sut.onStop()
        // Assert
        verify(viewMvcMock).unregisterListener(sut)
    }

    @Test
    fun onPersonSelected_typeReceiver_dialogDismissesAndListenerIsNotified() {
        // Arrange
        setReceiverPersonType()
        bindDialogAndView()
        // Act
        sut.onPersonSelected(RFC)
        // Assert
        verify(bottomSheetDialogMock).dismiss()
        verify(listenerMock).onPersonSelected(RFC)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun setReceiverPersonType() {
        sut = PersonSelectorBottomSheetController(
            typeReceiver,
            listenerMock,
            contextMock,
            personDao
        )
    }

    private fun getReceiversTitle() {
        `when`(contextMock.getString(R.string.action_select_person)).thenReturn(RECEIVERS_TITLE)
    }

    private fun getReceivers() {
        `when`(personDao.findAllReceivers()).thenReturn(receivers)
    }

    private fun getRootView() {
        `when`(viewMvcMock.getRootView()).thenReturn(viewMock)
    }

    private fun bindDialogAndView() {
        getRootView()
        sut.bindDialogAndView(bottomSheetDialogMock, viewMvcMock)
    }
}