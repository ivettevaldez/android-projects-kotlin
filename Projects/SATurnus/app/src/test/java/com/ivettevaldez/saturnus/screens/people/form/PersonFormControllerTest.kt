package com.ivettevaldez.saturnus.screens.people.form

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import android.os.Handler
import android.view.View
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.people.form.PersonFormControllerTest.FakeFieldValues.*
import com.ivettevaldez.saturnus.testdata.PeopleTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class PersonFormControllerTest {

    private lateinit var sut: PersonFormController

    @Mock
    private lateinit var viewMvcMock: IPersonFormViewMvc

    @Mock
    private lateinit var contextMock: Context

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var dialogsManager: DialogsManager

    @Mock
    private lateinit var messagesHelper: MessagesHelper

    @Mock
    private lateinit var uiHandlerMock: Handler

    @Mock
    private lateinit var personDaoMock: PersonDao

    @Mock
    private lateinit var viewMock: View

    @Captor
    private val personCaptor: ArgumentCaptor<Person> = ArgumentCaptor.forClass(Person::class.java)

    private val person: Person = PeopleTestData.getPhysicalPerson()

    companion object {

        private const val TITLE_NEW_PERSON: String = "Nueva persona"
        private const val TITLE_EDITING: String = "Editando…"
        private const val MESSAGE_SAVED_ID: Int = R.string.message_saved

        private const val RFC_MORAL_PERSON: String = "XXXXXXXXXXXX"
        private const val RFC_PHYSICAL_PERSON: String = "XXXXXXXXXXXXX"
        private const val RFC_OTHER: String = "XXXXXXXXXXXXXX"
    }

    enum class FakeFieldValues {

        ALL_FILLED_WITH_CORRECT_VALUES,
        LAST_FIELD_IS_BLANK,
        INVALID_PERSON_NAME,
        INVALID_RFC
    }

    @Before
    fun setUp() {
        sut = PersonFormController(
            contextMock,
            screensNavigatorMock,
            dialogsManager,
            messagesHelper,
            uiHandlerMock,
            personDaoMock
        )

        sut.bindView(viewMvcMock)
    }

    @Test
    fun bindRfc_newPerson_rfcIsNull() {
        // Arrange
        // Act
        sut.bindRfc(null)
        // Assert
        assertNull(sut.rfc)
    }

    @Test
    fun bindRfc_editingPerson_rfcIsBound() {
        // Arrange
        // Act
        sut.bindRfc(person.rfc)
        // Assert
        assertEquals(sut.rfc, person.rfc)
    }

    @Test
    fun setToolbarTitle_newPerson_correctTitleIsSet() {
        // Arrange
        newPerson()
        getTitleNewPerson()
        // Act
        sut.setToolbarTitle()
        // Assert
        verify(viewMvcMock).setToolbarTitle(TITLE_NEW_PERSON)
    }

    @Test
    fun setToolbarTitle_editingPerson_correctTitleIsSet() {
        // Arrange
        editingPerson()
        getTitleEditingPerson()
        // Act
        sut.setToolbarTitle()
        // Assert
        verify(viewMvcMock).setToolbarTitle(TITLE_EDITING)
    }

    @Test
    fun onStart_newPerson_noPersonIsBoundToView() {
        // Arrange
        newPerson()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock, never()).bindPerson(anyOrNull())
    }

    @Test
    fun onStart_editingPerson_personIsBoundToView() {
        // Arrange
        editingPerson()
        foundPersonByRfc()
        // Act
        sut.onStart()
        // Assert
        verify(viewMvcMock).bindPerson(person)
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
    fun onNavigateUpClicked_navigatesUp() {
        // Arrange
        // Act
        sut.onNavigateUpClicked()
        // Assert
        verify(screensNavigatorMock).navigateUp()
    }

    @Test
    fun onRfcChanged_moralPerson_correctPersonTypeIsSet() {
        // Arrange
        // Act
        sut.onRfcChanged(RFC_MORAL_PERSON)
        // Assert
        verify(viewMvcMock).setPersonType(Constants.MORAL_PERSON)
    }

    @Test
    fun onRfcChanged_physicalPerson_correctPersonTypeIsSet() {
        // Arrange
        // Act
        sut.onRfcChanged(RFC_PHYSICAL_PERSON)
        // Assert
        verify(viewMvcMock).setPersonType(Constants.PHYSICAL_PERSON)
    }

    @Test
    fun onRfcChanged_otherType_personTypeIsBlank() {
        // Arrange
        // Act
        sut.onRfcChanged(RFC_OTHER)
        // Assert
        verify(viewMvcMock).setPersonType("")
    }

    @Test
    fun onSaveClicked_missingFieldsError_missingFieldsErrorDialogIsShown() {
        // Arrange
        missingFieldsError()
        // Act
        sut.onSaveClicked()
        // Assert
        verify(dialogsManager).showMissingFieldsError(null)
    }

    @Test
    fun onSaveClicked_invalidPersonNameError_invalidPersonNameErrorDialogIsShown() {
        // Arrange
        invalidPersonNameError()
        // Act
        sut.onSaveClicked()
        // Assert
        verify(dialogsManager).showInvalidPersonNameError(null)
    }

    @Test
    fun onSaveClicked_invalidRfcError_invalidRfcErrorDialogIsShown() {
        // Arrange
        invalidRfcError()
        // Act
        sut.onSaveClicked()
        // Assert
        verify(dialogsManager).showInvalidRfcError(null)
    }

    @Test
    fun onSaveClicked_noFormErrorsAndSavedSuccessfully_personIsSavedAndFeedbackIsShownWithDelay() {
        // Arrange
        noFormErrorsAndSavedSuccessfully()
        // Act
        sut.onSaveClicked()
        // Assert
        verify(personDaoMock).save(capture(personCaptor))
        verify(viewMvcMock).cleanFields()
        verify(messagesHelper).showLongMessage(viewMock, MESSAGE_SAVED_ID)
        verify(uiHandlerMock).postDelayed(anyOrNull(), eq(Constants.SHOW_MESSAGE_DELAY))
        verify(screensNavigatorMock).navigateUp()

        val savedPerson = personCaptor.value
        assertEquals(savedPerson.name, person.name)
        assertEquals(savedPerson.rfc, person.rfc)
        assertEquals(savedPerson.personType, person.personType)
        assertEquals(savedPerson.clientType, person.clientType)
    }

    @Test
    fun onSaveClicked_noFormErrorsAndFailedSaving_savePersonDialogIsShown() {
        // Arrange
        noFormErrorsAndFailedSaving()
        // Act
        sut.onSaveClicked()
        // Assert
        verify(personDaoMock).save(anyOrNull())
        verify(dialogsManager).showSavePersonError(null)
    }

    // -----------------------------------------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------------------------------------

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun newPerson() {
        sut.bindRfc(null)
    }

    private fun editingPerson() {
        sut.bindRfc(person.rfc)
    }

    private fun getTitleNewPerson() {
        `when`(contextMock.getString(R.string.people_add_new_person)).thenReturn(TITLE_NEW_PERSON)
    }

    private fun getTitleEditingPerson() {
        `when`(contextMock.getString(R.string.action_editing)).thenReturn(TITLE_EDITING)
    }

    private fun foundPersonByRfc() {
        `when`(personDaoMock.findByRfc(person.rfc)).thenReturn(person)
    }

    private fun passedDelay() {
        `when`(uiHandlerMock.postDelayed(anyOrNull(), anyLong())).doAnswer {
            (it.arguments[0] as Runnable).run()
            null
        }
    }

    private fun missingFieldsError() {
        setFieldsReturnValues(LAST_FIELD_IS_BLANK)
    }

    private fun invalidPersonNameError() {
        setFieldsReturnValues(INVALID_PERSON_NAME)
    }

    private fun invalidRfcError() {
        setFieldsReturnValues(INVALID_RFC)
    }

    private fun noFormErrorsAndSavedSuccessfully() {
        `when`(personDaoMock.save(anyOrNull())).thenReturn(true)
        `when`(viewMvcMock.getRootView()).thenReturn(viewMock)

        passedDelay()

        setFieldsReturnValues(ALL_FILLED_WITH_CORRECT_VALUES)
    }

    private fun noFormErrorsAndFailedSaving() {
        `when`(personDaoMock.save(anyOrNull())).thenReturn(false)

        setFieldsReturnValues(ALL_FILLED_WITH_CORRECT_VALUES)
    }

    private fun setFieldsReturnValues(fakeFieldValues: FakeFieldValues) {
        when (fakeFieldValues) {
            LAST_FIELD_IS_BLANK -> person.clientType = ""
            INVALID_PERSON_NAME -> person.name = "X"
            INVALID_RFC -> person.rfc = "X"
            ALL_FILLED_WITH_CORRECT_VALUES -> {
                // Nothing to do here.
            }
        }

        `when`(viewMvcMock.getName()).thenReturn(person.name)
        `when`(viewMvcMock.getRfc()).thenReturn(person.rfc)
        `when`(viewMvcMock.getPersonType()).thenReturn(person.personType)
        `when`(viewMvcMock.getClientType()).thenReturn(person.clientType)
    }
}