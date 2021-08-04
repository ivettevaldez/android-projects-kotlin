package com.ivettevaldez.saturnus.screens.common.dialogs.personselector

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.PersonSelectorBottomSheetDialog.PersonType

class PersonSelectorBottomSheetController(
    val personType: PersonType,
    private val listener: IPersonSelectorBottomSheetViewMvc.Listener,
    private val context: Context,
    private val personDao: PersonDao
) : IPersonSelectorBottomSheetViewMvc.Listener {

    lateinit var dialog: BottomSheetDialog
    lateinit var viewMvc: IPersonSelectorBottomSheetViewMvc

    fun bindDialogAndView(dialog: BottomSheetDialog, viewMvc: IPersonSelectorBottomSheetViewMvc) {
        this.dialog = dialog
        this.viewMvc = viewMvc

        dialog.setContentView(viewMvc.getRootView())
    }

    fun onStart() {
        viewMvc.registerListener(this)

        bindTitle()
        bindPeople()
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    override fun onPersonSelected(rfc: String) {
        dialog.dismiss()
        listener.onPersonSelected(rfc)
    }

    private fun getTitle(): String = when (personType) {
        PersonType.RECEIVER -> context.getString(R.string.action_select_person)
        else -> throw IllegalArgumentException("@@@@@ Unsupported person type: $personType")
    }

    private fun getPeople(): List<Person> = when (personType) {
        PersonType.RECEIVER -> personDao.findAllReceivers()
        else -> throw IllegalArgumentException("@@@@@ Unsupported person type: $personType")
    }

    private fun bindTitle() {
        viewMvc.bindTitle(getTitle())
    }

    private fun bindPeople() {
        viewMvc.bindPeople(getPeople())
    }
}