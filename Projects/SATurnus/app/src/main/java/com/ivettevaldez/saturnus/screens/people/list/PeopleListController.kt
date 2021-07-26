package com.ivettevaldez.saturnus.screens.people.list

import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetDialogEvent
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator

class PeopleListController(
    private val screensNavigator: ScreensNavigator,
    private val dialogsManager: DialogsManager,
    private val dialogsEventBus: DialogsEventBus,
    private val personDao: PersonDao
) : IPeopleListViewMvc.Listener,
    DialogsEventBus.Listener {

    private lateinit var viewMvc: IPeopleListViewMvc

    lateinit var clientType: ClientType.Type

    var selectedPersonRfc: String? = null

    companion object {

        const val TAG_PERSON_OPTIONS_DIALOG = "TAG_PERSON_OPTIONS_DIALOG"
        const val TAG_DELETE_PERSON_CONFIRMATION_DIALOG = "TAG_PEOPLE_LIST_CONFIRMATION_DIALOG"
    }

    fun bindView(viewMvc: IPeopleListViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindClientType(clientType: ClientType.Type) {
        this.clientType = clientType
    }

    fun onStart() {
        viewMvc.registerListener(this)
        dialogsEventBus.registerListener(this)

        bindPeople()
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
    }

    override fun onPersonClick(rfc: String) {
        // TODO: Open person details (add a new screen).
    }

    override fun onPersonLongClick(rfc: String) {
        selectedPersonRfc = rfc
        dialogsManager.showPersonOptionsDialog(TAG_PERSON_OPTIONS_DIALOG)
    }

    override fun onDialogEvent(event: Any) {
        if (selectedPersonRfc != null) {
            when (val shownDialogTag = dialogsManager.getShownDialogTag()) {
                TAG_PERSON_OPTIONS_DIALOG -> {
                    validateSelectedOption(
                        (event as PromptBottomSheetDialogEvent).clickedButton
                    )
                }
                TAG_DELETE_PERSON_CONFIRMATION_DIALOG -> {
                    validatePersonDeletion(
                        (event as PromptDialogEvent).clickedButton
                    )
                }
                else -> {
                    throw IllegalArgumentException("@@@@@ Unsupported dialog: $shownDialogTag")
                }
            }
        }
    }

    private fun getPeople(): List<Person> = when (clientType) {
        ClientType.Type.ISSUING -> personDao.findAllIssuing()
        ClientType.Type.RECEIVER -> personDao.findAllReceivers()
        else -> throw IllegalArgumentException("@@@@@ Unsupported client type: $clientType")
    }

    private fun bindPeople() {
        viewMvc.showProgressIndicator()
        viewMvc.bindPeople(getPeople())
        viewMvc.hideProgressIndicator()
    }

    private fun validateSelectedOption(clickedButton: PromptBottomSheetDialogEvent.Button) {
        when (clickedButton) {
            PromptBottomSheetDialogEvent.Button.OPTION_ONE -> {
                editSelectedPerson()
            }
            PromptBottomSheetDialogEvent.Button.OPTION_TWO -> {
                showPersonDeletionConfirmationDialog()
            }
            else -> {
                throw IllegalArgumentException("@@@@@ Unsupported clicked button: $clickedButton")
            }
        }
    }

    private fun validatePersonDeletion(clickedButton: PromptDialogEvent.Button) {
        when (clickedButton) {
            PromptDialogEvent.Button.POSITIVE -> {
                deleteSelectedPersonAndRefreshList()
            }
            PromptDialogEvent.Button.NEGATIVE -> {
                selectedPersonRfc = null
            }
            else -> {
                throw IllegalArgumentException("@@@@@ Unsupported clicked button: $clickedButton")
            }
        }
    }

    private fun editSelectedPerson() {
        screensNavigator.toPersonForm(selectedPersonRfc!!)
        selectedPersonRfc = null
    }

    private fun showPersonDeletionConfirmationDialog() {
        dialogsManager.showDeletePersonConfirmation(
            TAG_DELETE_PERSON_CONFIRMATION_DIALOG
        )
    }

    private fun deleteSelectedPersonAndRefreshList() {
        personDao.delete(selectedPersonRfc!!)
        selectedPersonRfc = null
        bindPeople()
    }
}