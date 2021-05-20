package com.ivettevaldez.saturnus.screens.people.form

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
        fun onRfcChanged(rfc: String)
        fun onSaveClicked()
    }

    fun bindPerson(person: Person)
    fun getName(): String
    fun getRfc(): String
    fun getPersonType(): String
    fun getClientType(): String
    fun cleanFields()
    fun showProgressIndicator()
    fun hideProgressIndicator()
    fun setPersonType(type: String)
    fun setClientType(type: String)
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
    private val inputPersonType: TextInputLayout = findViewById(R.id.person_form_input_person_type)
    private val inputClientType: TextInputLayout = findViewById(R.id.person_form_input_client_type)
    private val editName: TextInputEditText =
        inputName.findViewById(R.id.text_input_edit_text_simple)
    private val editRfc: TextInputEditText =
        inputRfc.findViewById(R.id.text_input_edit_text_simple)
    private val editPersonType: TextInputEditText =
        inputPersonType.findViewById(R.id.text_input_edit_text_simple)
    private val editClientType: TextInputEditText =
        inputClientType.findViewById(R.id.text_input_edit_text_simple)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    private val rfcTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            for (listener in listeners) {
                listener.onRfcChanged(editRfc.text.toString().trim())
            }
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    init {

        initToolbar()
        initFields()
        setListenerEvents()
    }

    override fun bindPerson(person: Person) {
        editName.setText(person.name)
        editRfc.setText(person.rfc)
        editPersonType.setText(person.personType)
        editClientType.setText(person.clientType)
    }

    override fun getName(): String = editName.text.toString().trim()

    override fun getRfc(): String = editRfc.text.toString().trim()

    override fun getPersonType(): String = editPersonType.text.toString().trim()

    override fun getClientType(): String = editClientType.text.toString().trim()

    override fun cleanFields() {
        editName.setText("")
        editRfc.setText("")
        editPersonType.setText("")
        editClientType.setText("")
        buttonSave.requestFocus()
    }

    override fun showProgressIndicator() {
        layoutProgress.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        layoutProgress.visibility = View.GONE
    }

    override fun setPersonType(type: String) {
        editPersonType.setText(type)
    }

    override fun setClientType(type: String) {
        editClientType.setText(type)
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
        // Hints
        inputName.hint = context.getString(R.string.people_name)
        inputRfc.hint = context.getString(R.string.people_rfc)
        inputPersonType.hint = context.getString(R.string.people_type)
        inputClientType.hint = context.getString(R.string.people_client_type)

        // Other
        editName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS

        editRfc.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        editRfc.imeOptions = EditorInfo.IME_ACTION_DONE

        editPersonType.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        editPersonType.isEnabled = false

        editClientType.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        editClientType.isEnabled = false
    }

    private fun setListenerEvents() {
        buttonSave.setOnClickListener {
            for (listener in listeners) {
                listener.onSaveClicked()
            }
        }

        editRfc.addTextChangedListener(rfcTextWatcher)
    }
}