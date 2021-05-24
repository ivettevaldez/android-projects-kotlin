package com.ivettevaldez.saturnus.screens.people.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.WorkerThread
import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetDialogEvent
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PeopleListFragment : BaseFragment(),
    IPeopleListViewMvc.Listener,
    DialogsEventBus.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var dialogsEventBus: DialogsEventBus

    @Inject
    lateinit var peopleDao: PersonDao

    private lateinit var viewMvc: IPeopleListViewMvc
    private lateinit var clientType: ClientType.Type

    private var selectedPersonRfc: String? = null

    companion object {

        private const val ARG_CLIENT_TYPE = "ARG_CLIENT_TYPE"
        private const val TAG_PEOPLE_LIST_ACTIONS_DIALOG =
            "TAG_PEOPLE_LIST_ACTIONS_DIALOG"
        private const val TAG_PEOPLE_LIST_CONFIRMATION_DIALOG =
            "TAG_PEOPLE_LIST_CONFIRMATION_DIALOG"

        @JvmStatic
        fun newInstance(clientType: ClientType.Type) =
            PeopleListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CLIENT_TYPE, clientType)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            clientType = it.getSerializable(ARG_CLIENT_TYPE) as ClientType.Type
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newPeopleListViewMvc(parent)

        bindPeople()

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)
        dialogsEventBus.registerListener(this)
    }

    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
    }

    override fun onPersonClick(rfc: String) {
        // TODO:
    }

    override fun onPersonLongClick(rfc: String) {
        selectedPersonRfc = rfc
        dialogsManager.showPersonOptionsDialog(TAG_PEOPLE_LIST_ACTIONS_DIALOG)
    }

    override fun onDialogEvent(event: Any) {
        val shownDialogTag = dialogsManager.getShownDialogTag()

        if (shownDialogTag == TAG_PEOPLE_LIST_ACTIONS_DIALOG) {
            Thread {
                validateSelectedAction(
                    (event as PromptBottomSheetDialogEvent).clickedButton
                )
            }.start()
        } else if (shownDialogTag == TAG_PEOPLE_LIST_CONFIRMATION_DIALOG) {
            Thread {
                validatePersonDeletion(
                    (event as PromptDialogEvent).clickedButton
                )
            }.start()
        }
    }

    private fun getPeople(): List<Person> = when (clientType) {
        ClientType.Type.ISSUING -> peopleDao.findAllIssuing()
        ClientType.Type.RECEIVER -> peopleDao.findAllReceivers()
    }

    private fun bindPeople() {
        Thread {
            viewMvc.showProgressIndicator()
            viewMvc.bindPeople(getPeople())
            viewMvc.hideProgressIndicator()
        }.start()
    }

    @WorkerThread
    private fun validateSelectedAction(clickedButton: PromptBottomSheetDialogEvent.Button) {
        when (clickedButton) {
            PromptBottomSheetDialogEvent.Button.OPTION_ONE -> {
                editPerson()
            }
            PromptBottomSheetDialogEvent.Button.OPTION_TWO -> {
                dialogsManager.showDeletePersonConfirmationDialog(
                    TAG_PEOPLE_LIST_CONFIRMATION_DIALOG
                )
            }
        }
    }

    @WorkerThread
    private fun validatePersonDeletion(clickedButton: PromptDialogEvent.Button) {
        when (clickedButton) {
            PromptDialogEvent.Button.POSITIVE -> {
                deletePerson()
            }
            PromptDialogEvent.Button.NEGATIVE -> {
                // User canceled the action.
            }
        }
    }

    private fun editPerson() {
        if (selectedPersonRfc != null) {
            val person = peopleDao.findByRfc(selectedPersonRfc!!)
            selectedPersonRfc = null

            if (person != null) {
                screensNavigator.toPersonForm(person.rfc)
            }
        }
    }

    private fun deletePerson() {
        if (selectedPersonRfc != null) {
            peopleDao.delete(selectedPersonRfc!!)
            selectedPersonRfc = null
            bindPeople()
        }
    }
}