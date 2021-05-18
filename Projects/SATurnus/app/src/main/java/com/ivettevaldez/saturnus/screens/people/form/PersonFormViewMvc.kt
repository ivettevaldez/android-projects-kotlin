package com.ivettevaldez.saturnus.screens.people.form

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IPersonFormViewMvc : IObservableViewMvc<IPersonFormViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
        fun onSaveClicked()
    }

    fun bindPerson(person: Person)
    fun getName(): String
    fun getRfc(): String
    fun getPersonType(): String
    fun cleanFields()
    fun enableButtonSave(enabled: Boolean)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class PersonFormViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IPersonFormViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_person_form
), IPersonFormViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.person_form_toolbar)
    private val layoutProgress: FrameLayout = findViewById(R.id.person_form_progress)
    private val buttonSave: Button = findViewById(R.id.person_form_button_save)
    private val inputName: TextInputLayout = findViewById(R.id.person_form_input_name)
    private val inputRfc: TextInputLayout = findViewById(R.id.person_form_input_rfc)
    private val inputType: TextInputLayout = findViewById(R.id.person_form_input_type)
    private val editName: TextInputEditText =
        inputName.findViewById(R.id.text_input_edit_text_simple)
    private val editRfc: TextInputEditText =
        inputRfc.findViewById(R.id.text_input_edit_text_simple)
    private val editPersonType: TextInputEditText =
        inputType.findViewById(R.id.text_input_edit_text_simple)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    init {

        initToolbar()
        initFields()
        setListenerEvents()
        enableButtonSave(false)
    }

    override fun bindPerson(person: Person) {
        editName.setText(person.name)
        editRfc.setText(person.rfc)
        editPersonType.setText(person.personType)
    }

    override fun getName(): String = editName.text.toString().trim()

    override fun getRfc(): String = editRfc.text.toString().trim()

    override fun getPersonType(): String = editPersonType.text.toString().trim()

    override fun cleanFields() {
        editName.setText("")
        editRfc.setText("")
        editPersonType.setText("")

        buttonSave.requestFocus()
        enableButtonSave(false)
    }

    override fun enableButtonSave(enabled: Boolean) {
        buttonSave.isEnabled = enabled
    }

    override fun showProgressIndicator() {
        layoutProgress.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        layoutProgress.visibility = View.GONE
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(
            context.getString(R.string.people_add_new_person)
        )

        toolbarViewMvc.enableNavigateUpAndListen(object : IToolbarViewMvc.NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        toolbar.addView(toolbarViewMvc.getRootView())
    }

    private fun initFields() {
        inputName.hint = context.getString(R.string.people_name)
        inputRfc.hint = context.getString(R.string.people_rfc)
        inputType.hint = context.getString(R.string.people_type)

        editName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        editRfc.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        editPersonType.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
    }

    private fun setListenerEvents() {
        buttonSave.setOnClickListener {
            for (listener in listeners) {
                listener.onSaveClicked()
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val allFieldsFilled = editName.text.toString().isNotBlank() &&
                        editRfc.text.toString().isNotBlank() &&
                        editPersonType.text.toString().isNotBlank()

                enableButtonSave(allFieldsFilled)
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        editName.addTextChangedListener(textWatcher)
        editRfc.addTextChangedListener(textWatcher)
        editPersonType.addTextChangedListener(textWatcher)
    }
}