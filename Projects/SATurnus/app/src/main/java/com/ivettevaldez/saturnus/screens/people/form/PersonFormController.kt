package com.ivettevaldez.saturnus.screens.people.form

import android.content.Context
import android.os.Handler
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.messages.MessagesHelper
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.people.form.PersonDataValidator.isValidName
import com.ivettevaldez.saturnus.screens.people.form.PersonDataValidator.isValidRfc

class PersonFormController(
    private val context: Context,
    private val screensNavigator: ScreensNavigator,
    private val dialogsManager: DialogsManager,
    private val messagesHelper: MessagesHelper,
    private val uiHandler: Handler,
    private val personDao: PersonDao
) : IPersonFormViewMvc.Listener {

    private lateinit var viewMvc: IPersonFormViewMvc

    var rfc: String? = null

    fun bindView(viewMvc: IPersonFormViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindRfc(rfc: String?) {
        this.rfc = rfc
    }

    fun setToolbarTitle() {
        val title = if (rfc == null) {
            context.getString(R.string.people_add_new_person)
        } else {
            context.getString(R.string.action_editing)
        }

        viewMvc.setToolbarTitle(title)
    }

    fun onStart() {
        viewMvc.registerListener(this)

        if (rfc != null) {
            bindPerson()
        }
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    private fun findPerson(): Person? = personDao.findByRfc(rfc!!)

    private fun bindPerson() {
        val person = findPerson()
        if (person != null) {
            viewMvc.bindPerson(person)
        }
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onRfcChanged(rfc: String) {
        when (rfc.length) {
            PersonDataValidator.RFC_MORAL_PERSON_LENGTH -> {
                viewMvc.setPersonType(Constants.MORAL_PERSON)
            }
            PersonDataValidator.RFC_PHYSICAL_PERSON_LENGTH -> {
                viewMvc.setPersonType(Constants.PHYSICAL_PERSON)
            }
            else -> {
                viewMvc.setPersonType("")
            }
        }
    }

    override fun onSaveClicked() {
        validateFields()
    }

    private fun validateFields() {
        val name: String = viewMvc.getName()
        val rfc: String = viewMvc.getRfc()
        val personType: String = viewMvc.getPersonType()
        val clientType: String = viewMvc.getClientType()

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

    private fun savePerson(name: String, rfc: String, personType: String, clientType: String) {
        val saved = personDao.save(
            Person(
                name = name,
                rfc = rfc,
                personType = personType,
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

    // FIXME: Refresh PeopleList when a new person is saved.
    private fun close() {
        uiHandler.postDelayed({
            screensNavigator.navigateUp()
        }, Constants.SHOW_MESSAGE_DELAY)
    }
}
