package com.ivettevaldez.saturnus.screens.people.list

import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetDialogEvent
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.testdata.PeopleTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyList
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@RunWith(MockitoJUnitRunner::class)
class PeopleListControllerTest {

    private lateinit var sut: PeopleListController

    @Mock
    private lateinit var viewMvcMock: IPeopleListViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var dialogsManagerMock: DialogsManager

    @Mock
    private lateinit var dialogsEventBusMock: DialogsEventBus

    @Mock
    private lateinit var personDaoMock: PersonDao

    @Mock
    private lateinit var deletePersonConfirmationDialogEventMock: PromptDialogEvent

    @Mock
    private lateinit var personOptionsDialogEventMock: PromptBottomSheetDialogEvent

    private val issuingPeople: List<Person> = PeopleTestData.getAllIssuing()
    private val receivers: List<Person> = PeopleTestData.getAllReceivers()

    companion object {

        private const val RFC: String = "rfc"
    }

    @Before
    fun setUp() {
        sut = PeopleListController(
            screensNavigatorMock,
            dialogsManagerMock,
            dialogsEventBusMock,
            personDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun bindClientType_typeIssuing_clientTypeIsBound() {
        // Arrange
        // Act
        sut.bindClientType(ClientType.Type.ISSUING)
        // Assert
        assertEquals(sut.clientType, ClientType.Type.ISSUING)
    }

    @Test
    fun bindClientType_typeReceiver_clientTypeIsBound() {
        // Arrange
        // Act
        sut.bindClientType(ClientType.Type.RECEIVER)
        // Assert
        assertEquals(sut.clientType, ClientType.Type.RECEIVER)
    }

    @Test
    fun onStart_issuingPeople_peopleListIsBoundToView() {
        // Arrange
        selectIssuingPeople()
        // Act
        sut.onStart()
        // Assert
        verify(personDaoMock).findAllIssuing()
        viewMvcMock.inOrder {
            verify().showProgressIndicator()
            verify().bindPeople(issuingPeople)
            verify().hideProgressIndicator()
        }
    }

    @Test
    fun onStart_receiverPeople_peopleListIsBoundToView() {
        // Arrange
        selectReceiverPeople()
        // Act
        sut.onStart()
        // Assert
        verify(personDaoMock).findAllReceivers()
        viewMvcMock.inOrder {
            verify().showProgressIndicator()
            verify().bindPeople(receivers)
            verify().hideProgressIndicator()
        }
    }

    @Test
    fun onStart_listenersRegistered() {
        // Arrange
        selectIssuingPeople()
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
    fun onPersonLongClick_selectedPersonRfcIsBoundAndPersonOptionsDialogIsShownWithCorrectTag() {
        // Arrange
        // Act
        sut.onPersonLongClick(RFC)
        // Assert
        assertEquals(sut.selectedPersonRfc, RFC)
        verify(dialogsManagerMock).showPersonOptionsDialog(
            PeopleListController.TAG_PERSON_OPTIONS_DIALOG
        )
    }

    @Test
    fun onDialogEvent_userSelectedOptionOneFromPersonOptionsDialog_navigatesToPersonFormPassingCorrectRfc() {
        // Arrange
        userSelectedOptionOneFromPersonOptionsDialog()
        // Act
        sut.onDialogEvent(personOptionsDialogEventMock)
        // Assert
        verify(screensNavigatorMock).toPersonForm(RFC)
    }

    @Test
    fun onDialogEvent_userSelectedOptionTwoFromPersonOptionsDialog_deletePersonConfirmationDialogIsShownWithCorrectTag() {
        // Arrange
        userSelectedOptionTwoFromPersonOptionsDialog()
        // Act
        sut.onDialogEvent(personOptionsDialogEventMock)
        // Assert
        assertEquals(sut.selectedPersonRfc, RFC)
        verify(dialogsManagerMock).showDeletePersonConfirmation(
            PeopleListController.TAG_DELETE_PERSON_CONFIRMATION_DIALOG
        )
    }

    @Test
    fun onDialogEvent_userSelectedPositiveButtonFromDeletePersonConfirmationDialog_personIsDeletedAndPeopleListIsBoundToView() {
        // Arrange
        userSelectedPositiveButtonFromDeletePersonConfirmationDialog()
        // Act
        sut.onDialogEvent(deletePersonConfirmationDialogEventMock)
        // Assert
        verify(personDaoMock).delete(RFC)
        viewMvcMock.inOrder {
            verify().showProgressIndicator()
            verify().bindPeople(anyList())
            verify().hideProgressIndicator()
        }
    }

    @Test
    fun onDialogEvent_userSelectedNegativeButtonFromDeletePersonConfirmationDialog_noPersonIsDeleted() {
        // Arrange
        userSelectedNegativeButtonFromDeletePersonConfirmationDialog()
        // Act
        sut.onDialogEvent(deletePersonConfirmationDialogEventMock)
        // Assert
        assertNull(sut.selectedPersonRfc)
        verify(personDaoMock, never()).delete(RFC)
    }

    @Test
    fun onDialogEvent_selectedPersonRfcIsNull_nothingHappens() {
        // Arrange
        sut.selectedPersonRfc = null
        // Act
        sut.onDialogEvent(personOptionsDialogEventMock)
        // Assert
        verifyNoMoreInteractions(personDaoMock)
        verifyNoMoreInteractions(screensNavigatorMock)
        verifyNoMoreInteractions(dialogsManagerMock)
        verifyNoMoreInteractions(viewMvcMock)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun findIssuing() {
        `when`(personDaoMock.findAllIssuing()).thenReturn(issuingPeople)
    }

    private fun findReceivers() {
        `when`(personDaoMock.findAllReceivers()).thenReturn(receivers)
    }

    private fun selectIssuingPeople() {
        sut.bindClientType(ClientType.Type.ISSUING)
        findIssuing()
    }

    private fun selectReceiverPeople() {
        sut.bindClientType(ClientType.Type.RECEIVER)
        findReceivers()
    }

    private fun getPersonOptionsDialogTag() {
        `when`(
            dialogsManagerMock.getShownDialogTag()
        ).thenReturn(
            PeopleListController.TAG_PERSON_OPTIONS_DIALOG
        )
    }

    private fun getDeletePersonConfirmationDialogTag() {
        `when`(
            dialogsManagerMock.getShownDialogTag()
        ).thenReturn(
            PeopleListController.TAG_DELETE_PERSON_CONFIRMATION_DIALOG
        )
    }

    private fun selectOptionOneFromPersonOptionsDialog() {
        `when`(
            personOptionsDialogEventMock.clickedButton
        ).thenReturn(
            PromptBottomSheetDialogEvent.Button.OPTION_ONE
        )
    }

    private fun selectOptionTwoFromPersonOptionsDialog() {
        `when`(
            personOptionsDialogEventMock.clickedButton
        ).thenReturn(
            PromptBottomSheetDialogEvent.Button.OPTION_TWO
        )
    }

    private fun selectPositiveButtonFromDeletePersonConfirmationDialog() {
        `when`(
            deletePersonConfirmationDialogEventMock.clickedButton
        ).thenReturn(
            PromptDialogEvent.Button.POSITIVE
        )
    }

    private fun selectNegativeButtonFromDeletePersonConfirmationDialog() {
        `when`(
            deletePersonConfirmationDialogEventMock.clickedButton
        ).thenReturn(
            PromptDialogEvent.Button.NEGATIVE
        )
    }

    private fun userSelectedOptionOneFromPersonOptionsDialog() {
        getPersonOptionsDialogTag()
        selectOptionOneFromPersonOptionsDialog()
        sut.selectedPersonRfc = RFC
    }

    private fun userSelectedOptionTwoFromPersonOptionsDialog() {
        getPersonOptionsDialogTag()
        selectOptionTwoFromPersonOptionsDialog()
        sut.selectedPersonRfc = RFC
    }

    private fun userSelectedPositiveButtonFromDeletePersonConfirmationDialog() {
        getDeletePersonConfirmationDialogTag()
        selectPositiveButtonFromDeletePersonConfirmationDialog()
        selectReceiverPeople()
        sut.selectedPersonRfc = RFC
    }

    private fun userSelectedNegativeButtonFromDeletePersonConfirmationDialog() {
        getDeletePersonConfirmationDialogTag()
        selectNegativeButtonFromDeletePersonConfirmationDialog()
        sut.selectedPersonRfc = RFC
    }
}