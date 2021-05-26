package com.ivettevaldez.saturnus.screens.common.dialogs.personselector

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseBottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PersonSelectorBottomSheetDialog(
    private val listener: IPersonSelectorBottomSheetViewMvc.Listener
) : BaseBottomSheetDialog(),
    IPersonSelectorBottomSheetViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var dialogsEventBus: DialogsEventBus

    @Inject
    lateinit var personDao: PersonDao

    private lateinit var viewMvc: IPersonSelectorBottomSheetViewMvc

    companion object {

        @JvmStatic
        fun newPersonSelectorBottomSheetDialog(
            listener: IPersonSelectorBottomSheetViewMvc.Listener
        ) = PersonSelectorBottomSheetDialog(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewMvc = viewMvcFactory.newPersonSelectorBottomSheetDialogViewMvc(null)

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(viewMvc.getRootView())

        viewMvc.setTitle(getString(R.string.action_select_person))
        viewMvc.bindPeople(getReceivers())

        return dialog
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
    }

    override fun onPersonSelected(rfc: String) {
        dismiss()
        listener.onPersonSelected(rfc)
    }

    private fun getReceivers(): List<Person> = personDao.findAllReceivers()
}