package com.ivettevaldez.saturnus.screens.people.form

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PersonFormFragment : BaseFragment(),
    IPersonFormViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var messagesHelper: MessagesHelper

    @Inject
    lateinit var personDao: PersonDao

    @Inject
    lateinit var uiHandler: Handler

    private lateinit var viewMvc: IPersonFormViewMvc

    private var rfc: String? = null

    companion object {

        const val MIN_NAME_LENGTH = 3

        private const val ARG_RFC = "ARG_RFC"

        @JvmStatic
        fun newInstance(rfc: String?) = PersonFormFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_RFC, rfc)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            rfc = it.getString(ARG_RFC)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newPersonFormViewMvc(parent)

        val toolbarTitle = if (rfc != null) {
            bindPerson()
            getString(R.string.action_editing)
        } else {
            getString(R.string.people_add_new_person)
        }

        viewMvc.setToolbarTitle(toolbarTitle)
        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onSaveClicked() {
        validateFields()
    }

    override fun onRfcChanged(rfc: String) {
        when (rfc.length) {
            Constants.RFC_MORAL_PERSON_LENGTH -> {
                viewMvc.setPersonType(Constants.MORAL_PERSON)
            }
            Constants.RFC_PHYSICAL_PERSON_LENGTH -> {
                viewMvc.setPersonType(Constants.PHYSICAL_PERSON)
            }
            else -> {
                viewMvc.setPersonType("")
            }
        }
    }

    private fun bindPerson() {
        val person = personDao.findByRfc(rfc!!)
        if (person != null) {
            viewMvc.bindPerson(person)
        }
    }

    private fun String.isValidName(): Boolean {
        return this.length > MIN_NAME_LENGTH
    }

    private fun String.isValidRfc(): Boolean {
        // TODO: Validate RFC format.
        return (this.length == Constants.RFC_MORAL_PERSON_LENGTH ||
                this.length == Constants.RFC_PHYSICAL_PERSON_LENGTH) &&
                !this.contains(" ", true)
    }

    private fun validateFields() {
        val name = viewMvc.getName()
        val rfc = viewMvc.getRfc()
        val personType = viewMvc.getPersonType()
        val clientType = viewMvc.getClientType()

        when {
            name.isBlank() || rfc.isBlank() || personType.isBlank() || clientType.isBlank() -> {
                dialogsManager.showMissingFieldsError(null)
            }
            !name.isValidName() -> {
                dialogsManager.showInvalidPersonNameError(null)
            }
            !rfc.isValidRfc() -> {
                dialogsManager.showInvalidRfcError(null)
            }
            else -> {
                savePerson(name, rfc, personType, clientType)
            }
        }
    }

    private fun savePerson(name: String, rfc: String, type: String, clientType: String) {
        val saved = personDao.save(
            Person(
                name = name,
                rfc = rfc,
                personType = type,
                clientType = clientType
            )
        )

        if (saved) {
            viewMvc.cleanFields()
            messagesHelper.showLongMessage(viewMvc.getRootView(), R.string.message_saved)
            close()
        } else {
            dialogsManager.showSavePersonError(null)
        }
    }

    private fun close() {
        // FIXME: Refresh PeopleList when a new person is saved.
        uiHandler.postDelayed({
            screensNavigator.navigateUp()
        }, Constants.SHOW_MESSAGE_DELAY)
    }
}